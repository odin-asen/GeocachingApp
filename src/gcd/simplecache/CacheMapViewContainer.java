package gcd.simplecache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.business.map.MapObject;
import gcd.simplecache.dto.geocache.DTOLocation;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the map view. An object can call cache map related methods.
 * The method setContext must be executed before calling another method that
 * might need the context.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 24.04.13
 */
public class CacheMapViewContainer {
  private static final String LOG_TAG = CacheMapViewContainer.class.getName();
  private Context mContext;

  /* saved values */
  private GeoPoint mLastPoint;
  private int mLastZoomLevel;
  private MapObject mDestination;
  private MapObject mUser;

  private MapView mMapView;
  private MapController mController;
  private MapListener mMapListener;

  /* Map overlay variables */
  private ItemizedIconOverlay<MapObject> mUserOverlay;
  private ItemizedOverlay<MapObject> mCacheOverlay;
  private ItemizedOverlay<OverlayItem> mAimOverlay;
  private final CacheMapFragment.MapItemListener mMapItemListener;

  /****************/
  /* Constructors */

  public CacheMapViewContainer(MapListener mapListener,
                               CacheMapFragment.MapItemListener mapItemListener) {
    initialiseOverlays();
    mMapItemListener = mapItemListener;
    mMapListener = mapListener;
    mDestination = null;
    mUser = new MapObject("", "", new GeoPoint(0.0,0.0));

    /* Set the map to the middle of Europe */
    mLastZoomLevel = 2;
    mLastPoint = new GeoPoint(53.330873,10.722656);
  }

  /*     End      */
  /****************/

  /***********/
  /* Methods */

  /** setContext must have been called before */
  public void updateUserPosition(Location location) {
    mMapView.getOverlayManager().remove(mUserOverlay);

    final GeoCoordinateConverter converter = new GeoCoordinateConverter();
    final GeoPoint currentPoint =
        converter.geocachingToGeoPoint(converter.locationToGeocaching(location));

    /* set the user object to the map */
    mUser = new MapObject(mContext.getString(R.string.map_user_title),
        mContext.getString(R.string.map_user_here), currentPoint);
    mUser.setType(MapObject.ObjectType.USER);
    mUser.setMarker(mContext.getResources().getDrawable(R.drawable.position_cross));
    final List<MapObject> objectList = new ArrayList<MapObject>(1);
    objectList.add(mUser);

    /* add overlay to the map */
    mUserOverlay = new ItemizedOverlayWithFocus<MapObject>(
        mContext, objectList, mMapItemListener);
    mUserOverlay.addItem(mUser);
    mMapView.getOverlayManager().add(mUserOverlay);

    mController.setCenter(currentPoint);
    mController.setZoom(10);
//    setLastView(currentPoint, 10);
    mMapView.invalidate();
  }

  /**
   * This method takes a list of cache information and displays it on the map.
   * setContext must have been called before.
   * @param cacheList List with caches to show on the map.
   */
  public void updateGeocaches(List<Geocache> cacheList) {
    if(cacheList == null)
      return;
    mMapView.getOverlayManager().remove(mCacheOverlay);

    final List<MapObject> objectList = new ArrayList<MapObject>(cacheList.size());
    fillMapObjectList(cacheList, objectList);

    /* add overlay to the map */
    mCacheOverlay = new ItemizedOverlayWithFocus<MapObject>(
        mContext, objectList, mMapItemListener);
    mMapView.getOverlayManager().add(mCacheOverlay);

    mMapView.postInvalidate();
  }

  /**
   * setContext must have been called before.
   * Set path and aim icons on the map. If path is null, only the aim will be set.
   * @param aim Aim where to go. Must not be null.
   * @param path List of intermediate location points.
   */
  public void refreshRoute(Geocache aim, List<DTOLocation> path) {
    mMapView.getOverlayManager().remove(mAimOverlay);

    mDestination = new MapObject(aim);
    mDestination.setMarker(mContext.getResources().getDrawable(R.drawable.goal_flag));

    final List<OverlayItem> routeList = initialiseRouteList(path);

    /* add overlay to the map */
    mAimOverlay = new ItemizedOverlayWithFocus<OverlayItem>(mContext, routeList, null);
    mMapView.getOverlayManager().add(mAimOverlay);
  }

  /**
   * Initialises the last state of the map view.
   * @param showCaches If true, the caches overlay will be shown and the aim overlay
   *                   with the route will be destroyed. If false the cache overlay
   *                   will be destroyed and the aim overlay with the route will be
   *                   displayed, instead.
   */
  public void initialiseLastState(boolean showCaches) {
    /* Go to the last point */
    mController.setZoom(mLastZoomLevel);
    mController.setCenter(mLastPoint);

    setOverlays(showCaches);
    mMapView.postInvalidate();
  }

  public void updateOverlays() {
    setOverlays(mDestination == null);
    mMapView.postInvalidate();
  }
  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

  private void initialiseOverlays() {
    mUserOverlay = null;
    mCacheOverlay = null;
    mAimOverlay = null;
  }

  /* Initialise settings for the MapView object */
  private void initMap() {
    mMapView.setMapListener(new DelayedMapListener(mMapListener, 500L));
    mMapView.setTileSource(TileSourceFactory.MAPNIK);
    mMapView.setBuiltInZoomControls(true);
    mMapView.setMultiTouchControls(true);
  }

  /**
   * Returns an overlay item list with the destination and the path.
   * Path can be null to set only the destination.
   */
  private List<OverlayItem> initialiseRouteList(List<DTOLocation> path) {
    final List<OverlayItem> list;
    if(path == null) {
      list = new ArrayList<OverlayItem>(1);
    } else {
      list = new ArrayList<OverlayItem>(path.size()+1);

      for (DTOLocation point : path)
        list.add(new OverlayItem("","",new GeoPoint(point.latitude, point.longitude)));
    }

    list.add(mDestination);

    return list;
  }

  /**
   * Sets the saved overlay variables on the map.
   * Depending on the navigation mode it sets specific overlays to null
   * to save memory.
   * @param showCaches see {@link #initialiseLastState(boolean)}
   */
  private void setOverlays(boolean showCaches) {
    mMapView.getOverlayManager().remove(mCacheOverlay);
    mMapView.getOverlayManager().remove(mAimOverlay);
    mMapView.getOverlayManager().remove(mUserOverlay);

    if(!showCaches) {
      mCacheOverlay = null;
      if(mAimOverlay != null)
        mMapView.getOverlayManager().add(mAimOverlay);
    } else {
      if(mCacheOverlay != null)
        mMapView.getOverlayManager().add(mCacheOverlay);
      mAimOverlay = null;
    }

    if(mUserOverlay != null)
      mMapView.getOverlayManager().add(mUserOverlay);
  }

  /* fills a map object list with geocache objects */
  private void fillMapObjectList(List<Geocache> cacheList, List<MapObject> objectList) {
    for (Geocache geocache : cacheList) {
      final MapObject cacheObject = new MapObject(geocache);
      cacheObject.setMarker(getMapObjectDrawable(cacheObject.getType()));
      objectList.add(cacheObject);
    }
  }

  private Drawable getMapObjectDrawable(MapObject.ObjectType type) {
    int resourceId = R.drawable.goal_flag;

    if(type.isUser())
      resourceId = R.drawable.position_cross;
    else if(type.isGeocache()) {
      if(type.equals(MapObject.ObjectType.TRADITIONAL))
        resourceId = R.drawable.treasure;
      else if(type.equals(MapObject.ObjectType.MULTI))
        resourceId = R.drawable.multi_treasure;
      else if(type.equals(MapObject.ObjectType.RIDDLE))
        resourceId = R.drawable.question_mark;
      else if(type.equals(MapObject.ObjectType.VIRTUAL))
        resourceId = R.drawable.question_mark;
    }

    return mContext.getResources().getDrawable(resourceId);
  }

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */

  public void setMapView(MapView mapView) {
    mMapView = mapView;
    if(mMapView != null) {
      mController = mMapView.getController();
      initMap();
    } else mController = null;
  }

  public void setLastView() {
    setLastView((GeoPoint) mMapView.getMapCenter(), mLastZoomLevel);
  }

  public void setLastView(GeoPoint lastPoint, int lastZoomLevel) {
    if(!mLastPoint.equals(lastPoint))
      mLastPoint = lastPoint;
    if(mLastZoomLevel != lastZoomLevel)
      mLastZoomLevel = lastZoomLevel;
  }

  public int getLastZoomLevel() {
    return mLastZoomLevel;
  }

  public GeoPoint getLastPoint() {
    return new GeoPoint(mLastPoint);
  }

  public GeoPoint getUserPoint() {
    if(mUser == null)
      return null;
    else return mUser.getPoint();
  }

  /**
   * Returns the corner coordinates als float array in the following
   * sequence:<br/>
   * Latitude of north, longitude of east, latitude of south, longitude of west.
   */
  public float[] getMapBounds() {
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();

    BoundingBoxE6 box = mMapView.getProjection().getBoundingBox();
    return new float[]{
        (float) converter.microToDecimalDegree(box.getLatNorthE6()),
        (float) converter.microToDecimalDegree(box.getLonEastE6()),
        (float) converter.microToDecimalDegree(box.getLatSouthE6()),
        (float) converter.microToDecimalDegree(box.getLonWestE6())
    };
  }

  /** Must not be null. */
  public void setContext(Context context) {
    if(context == null)
      throw new NullPointerException("The context for CacheMapViewContainer must not be null.");
    mContext = context;
  }
  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */
  /*     End       */
  /*****************/
}
