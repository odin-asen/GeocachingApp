package gcd.simplecache;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.business.map.MapObject;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;

import java.util.ArrayList;
import java.util.List;

/**
 * Map view fragment. This map view shows the caches on a map.
 */
public class CacheMapFragment extends Fragment {
  private static final String LAST_POINT = "point";
  private static final String LAST_ZOOM = "zoom";

  /* saved values */
  private GeoPoint mLastPoint;
  private int mLastZoomLevel;

  private MapView mMapView;
  private MapController mController;
  private boolean mNavigationEnabled;

  /* Map overlay variables */
  private ItemizedIconOverlay<MapObject> mUserOverlay;
  private ItemizedOverlay<MapObject> mCacheOverlay;
  private ItemizedOverlay<MapObject> mAimOverlay;

  /****************/
  /* Constructors */
  /****************/

  public CacheMapFragment() {
    mUserOverlay = null;
    mCacheOverlay = null;
    mAimOverlay = null;
    mLastZoomLevel = 0;
    mLastPoint = new GeoPoint(0.0,0.0);
  }

  /***********/
  /* Methods */
  /***********/

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cachemap, container, false);
    mMapView = (MapView) view.findViewById(R.id.cachemap);
    mController = mMapView.getController();

    /* Save zoom level after user input */
    mMapView.setMapListener(new DelayedMapListener(new ScrollZoomListener(), 500L));

    return view;
  }

  public void updateUserPosition(Location location) {
    mMapView.getOverlayManager().remove(mUserOverlay);

    final GeoCoordinateConverter converter = new GeoCoordinateConverter();
    final GeoPoint currentPoint =
        converter.geocachingToGeoPoint(converter.locationToGeocaching(location));

    /* set the user object to the map */
    MapObject userObject = new MapObject("Geocacher", "Current Position", currentPoint);
    userObject.setType(MapObject.ObjectType.USER);
    userObject.setMarker(getActivity().getResources().getDrawable(R.drawable.position_cross));
    final List<MapObject> objectList = new ArrayList<MapObject>(1);
    objectList.add(userObject);

    /* add overlay to the map */
    mUserOverlay = new ItemizedOverlayWithFocus<MapObject>(
        getActivity(), objectList, new MapItemListener());
    mUserOverlay.addItem(userObject);
    mMapView.getOverlayManager().add(mUserOverlay);

    saveLastPointAndZoom(currentPoint);
    mMapView.invalidate();
  }

  /**
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void updateGeocacheObjects(List<Geocache> cacheList) {
    if(cacheList == null)
      return;
    mMapView.getOverlayManager().remove(mCacheOverlay);

    final List<MapObject> objectList = new ArrayList<MapObject>(cacheList.size());
    fillMapObjectList(cacheList, objectList);

    /* add overlay to the map */
    mCacheOverlay = new ItemizedOverlayWithFocus<MapObject>(
        getActivity(), objectList, null);
    mMapView.getOverlayManager().add(mCacheOverlay);

    mMapView.postInvalidate();
  }

  @Override
  public void onActivityCreated(Bundle savedInstance) {
    super.onActivityCreated(savedInstance);

    if(savedInstance != null) {
      if(mLastPoint != null)
        mLastPoint = (GeoPoint) savedInstance.getSerializable(LAST_POINT);
      mLastZoomLevel = savedInstance.getInt(LAST_ZOOM);
    }
    initMap();
  }

  @Override
  public void onStart() {
    super.onStart();

    /* Go to the last point */
    mController.setZoom(mLastZoomLevel);
    mController.setCenter(mLastPoint);

//    mMapView.invalidate();
    /* Test code */
    Location location = new Location("Test");
    location.setLatitude(45.0);
    location.setLongitude(9.0);
    updateUserPosition(location);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(LAST_POINT, mLastPoint);
    outState.putInt(LAST_ZOOM, mLastZoomLevel);
    Log.d("map", "save instance");
  }

  /*******************/
  /* private methods */
  /*******************/

  /* Initialise settings for MapView object */
  private void initMap() {
    mMapView.setTileSource(TileSourceFactory.MAPNIK);
    mMapView.setBuiltInZoomControls(true);
    mMapView.setMultiTouchControls(true);
  }

  /* save last point and zoom to instance variables */
  private void saveLastPointAndZoom(GeoPoint geoPoint) {
    mLastZoomLevel = mMapView.getZoomLevel();
    mLastPoint = geoPoint;
  }

  /* fills a map object list with geocache objects */
  private void fillMapObjectList(List<Geocache> cacheList, List<MapObject> objectList) {
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();
    for (Geocache geocache : cacheList) {
      final MapObject cacheObject = new MapObject(
          geocache.getName(),
          getCacheMapObjectDescription(geocache),
          converter.geocachingToGeoPoint(geocache.getPoint()));
      cacheObject.setMarker(getActivity().getResources().getDrawable(R.drawable.treasure));
      cacheObject.setType(MapObject.ObjectType.TRADITIONAL);
      objectList.add(cacheObject);
    }
  }

  /** formats the string for the description of a map object */
  private String getCacheMapObjectDescription(Geocache geocache) {
    return geocache.getId()+" - "+geocache.getOwner()+"\n"
        +"Difficulty: "+geocache.getDifficulty()+"\n"
        +"Terrain: "+geocache.getTerrain();
  }

  /*********************/
  /* Getter and Setter */
  /*********************/

  public void setNavigationEnabled(boolean enabled) {
    this.mNavigationEnabled = enabled;
  }

  public boolean isNavigationEnabled() {
    return mNavigationEnabled;
  }

  /* Inner classes */
  /* Reacts on touching event on the map objects */
  private class MapItemListener implements ItemizedIconOverlay.OnItemGestureListener<MapObject> {

    @Override
    public boolean onItemSingleTapUp(int i, MapObject mapObject) {
      SearchCacheDialog dialog = new SearchCacheDialog();
      dialog.show(getFragmentManager(), "");
      /* Return true to prevent calling this method */
      /* for a second magic item */
      return true;
    }

    @Override
    public boolean onItemLongPress(int i, MapObject mapObject) {
      return false;
    }
  }

  private class ScrollZoomListener implements MapListener {
    public boolean onScroll(ScrollEvent scrollEvent) {
      mLastPoint = (GeoPoint) mMapView.getMapCenter();
      return true;
    }

    public boolean onZoom(ZoomEvent zoomEvent) {
      mLastZoomLevel = zoomEvent.getZoomLevel();
      return true;
    }
  }
}
