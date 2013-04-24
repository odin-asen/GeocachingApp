package gcd.simplecache;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import gcd.simplecache.business.geocaching.ComOpencachingService;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.geocaching.GeocachingService;
import gcd.simplecache.business.geocaching.request.ComOpencachingRequestCollection;
import gcd.simplecache.business.geocaching.request.com.opencaching.BBox;
import gcd.simplecache.business.geocaching.request.com.opencaching.Limit;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.business.map.MapObject;
import gcd.simplecache.business.map.RoutingService;
import gcd.simplecache.dto.geocache.DTOLocation;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
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
 * Map view fragment.<br/>
 * The fragment displays cache map and fetches automatically cache information from
 * opencaching.com. If it is set, the fragment shows the user's position on the map.
 * The fragment has a navigation mode. The navigation mode indicates that the user
 * wants to go to a cache. The map will only display the user icon and the aim icon
 * with a path to it, when navigation mode is enabled. It will not refresh the cache
 * icons on the map. In particularly, these icons will be destroyed.
 * If navigation mode is disabled the cache icons will be refreshed on the map. This
 * happens also at special conditions. If the zoom level is higher than a specified limit
 * (you can see a small area on the map) the cache icons will be refreshed when the user
 * moves the map or zooms out. Nothing happens if the user zooms in.
 */
public class CacheMapFragment extends Fragment {
  private static final String LAST_POINT = "point";
  private static final String LAST_ZOOM = "zoom";
  private static final String LOG_TAG = CacheMapFragment.class.getName();
  private static final int ZOOM_LEVEL_UPDATE_LIMIT = 8;
  private static final int FETCH_LIMIT = 500;

  /* saved values */
  private GeoPoint mLastPoint;
  private int mLastZoomLevel;

  private ProgressBar mProgressBar;
  private TextView mProgressText;
  private MapView mMapView;
  private MapController mController;
  private boolean mNavigationEnabled;
  private MapObject mDestination;
  private MapObject mUser;

  /* Map overlay variables */
  private ItemizedIconOverlay<MapObject> mUserOverlay;
  private ItemizedOverlay<MapObject> mCacheOverlay;
  private ItemizedOverlay<OverlayItem> mAimOverlay;
  private final MapItemListener mMapItemListener;

  /****************/
  /* Constructors */

  public CacheMapFragment() {
    initialiseOverlays();
    mLastZoomLevel = 8;
    mLastPoint = new GeoPoint(50.0,10.0);
    mNavigationEnabled = false;
    mDestination = new MapObject("", "", new GeoPoint(0.0,0.0));
    mUser = new MapObject("", "", new GeoPoint(0.0,0.0));
    mMapItemListener = new MapItemListener();
  }

  /*      End     */
  /****************/

  /***********/
  /* Methods */

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cachemap, container, false);
    mProgressBar = (ProgressBar) view.findViewById(R.id.map_progress);
    mProgressBar.setVisibility(ProgressBar.GONE);
    mProgressText = (TextView) view.findViewById(R.id.map_progress_text);
    mMapView = (MapView) view.findViewById(R.id.cache_map_view);
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
    mUser = new MapObject(getString(R.string.map_user_title),
        getString(R.string.map_user_here), currentPoint);
    mUser.setType(MapObject.ObjectType.USER);
    mUser.setMarker(getActivity().getResources().getDrawable(R.drawable.position_cross));
    final List<MapObject> objectList = new ArrayList<MapObject>(1);
    objectList.add(mUser);

    /* add overlay to the map */
    mUserOverlay = new ItemizedOverlayWithFocus<MapObject>(
        getActivity(), objectList, mMapItemListener);
    mUserOverlay.addItem(mUser);
    mMapView.getOverlayManager().add(mUserOverlay);

    saveLastPointAndZoom(currentPoint);
    mMapView.invalidate();
  }

  /**
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void updateCacheOverlay(List<Geocache> cacheList) {
    if(cacheList == null)
      return;
    mMapView.getOverlayManager().remove(mCacheOverlay);

    final List<MapObject> objectList = new ArrayList<MapObject>(cacheList.size());
    fillMapObjectList(cacheList, objectList);

    /* add overlay to the map */
    mCacheOverlay = new ItemizedOverlayWithFocus<MapObject>(
        getActivity(), objectList, mMapItemListener);
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

    /* initialise stuff that needs a context object */
    initialiseContextStuff(savedInstance);
    initMap();
  }

  @Override
  public void onStart() {
    super.onStart();

    /* Go to the last point */
    mController.setZoom(mLastZoomLevel);
    mController.setCenter(mLastPoint);

    setOverlays();
    mMapView.invalidate();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(LAST_POINT, mLastPoint);
    outState.putInt(LAST_ZOOM, mLastZoomLevel);
    Log.d("map", "save instance");
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

  private void initialiseContextStuff(Bundle savedInstance) {
//    mDestination.setMarker(getResources().getDrawable(R.drawable.goal_flag));
//    mUser.setMarker(getResources().getDrawable(R.drawable.position_cross));
  }

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
      cacheObject.setGeocache(geocache);
      objectList.add(cacheObject);
    }
  }

  /** formats the string for the description of a map object */
  private String getCacheMapObjectDescription(Geocache geocache) {
    return geocache.getId()+" - "+geocache.getOwner()+"\n"
        +"Size: "+geocache.getSize()+"\n"
        +"Difficulty: "+geocache.getDifficulty()+"\n"
        +"Terrain: "+geocache.getTerrain();
  }

  private void refreshRoute() {
    mMapView.getOverlayManager().remove(mAimOverlay);

    /* Get the route if possible */
    final RoutingService service = new RoutingService();
    final List<DTOLocation> path;
    if(mUser == null || mDestination == null)
      path = null;
    else path = service.getPath(mUser.getPoint(), mDestination.getPoint());

    final List<OverlayItem> routeList = initialiseRouteList(path);

    /* add overlay to the map */
    mAimOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
        getActivity(), routeList, null);
    mMapView.getOverlayManager().add(mAimOverlay);
  }

  private List<OverlayItem> initialiseRouteList(List<DTOLocation> path) {
    final List<OverlayItem> list;
    if(path == null) {
      list = new ArrayList<OverlayItem>(1);
      list.add(mDestination);
      new ViewInThreadHandler().showToast("Could not set route path", Toast.LENGTH_LONG);
    } else {
      list = new ArrayList<OverlayItem>(path.size()+1);
      list.add(mDestination);

      for (DTOLocation point : path)
        list.add(new OverlayItem("","",new GeoPoint(point.latitude, point.longitude)));
    }

    return list;
  }

  /**
   * Fetches cache information from the cache
   * database and refreshes the map.
   */
  private synchronized void updateGeocacheMap() {

    /* set up service for database request and fetch data */
    final CacheDataFetcher fetcher = new CacheDataFetcher().invoke();
    final List<Geocache> list = fetcher.getList();
    GeocachingService service = fetcher.getService();

    if (fetcher.wasSuccessful()) {
      Log.d(LOG_TAG, "fetched...start updating");
      updateCacheOverlay(list);
      Log.d(LOG_TAG, "finished update");
    } else {
      Log.d(LOG_TAG, "error fetching");
      new ViewInThreadHandler().showToast(service.getError(), Toast.LENGTH_LONG);
    }
  }

  /**
   * Sets the saved overlay variables on the map.
   * Depending on the navigation mode it sets specific overlays to null
   * to save memory.
   */
  private void setOverlays() {
    mMapView.getOverlayManager().remove(mCacheOverlay);
    mMapView.getOverlayManager().remove(mAimOverlay);
    mMapView.getOverlayManager().remove(mUserOverlay);

    if(mNavigationEnabled) {
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

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */

  public void setNavigationEnabled(boolean enabled) {
    this.mNavigationEnabled = enabled;
  }

  public boolean isNavigationEnabled() {
    return mNavigationEnabled;
  }

  /**
   * This method sets a new destination and refreshes the route
   * if the navigation is enabled. Only the aim, the user and
   * the route to the aim will be displayed on the map.<br/>
   * When the method is called and the navigation is disabled,
   * the map will destroy this destination.<br/>
   * It should be called in an own thread.
   * @param destination New destination point.
   */
  public void setDestination(Geocache destination) {
    mDestination = new MapObject(
        destination.getName(),
        getCacheMapObjectDescription(destination),
        new GeoCoordinateConverter().geocachingToGeoPoint(
            destination.getPoint()));
    mDestination.setGeocache(destination);
    mDestination.setMarker(getActivity().getResources().getDrawable(R.drawable.goal_flag));

    if(mNavigationEnabled) {
      mMapView.getOverlayManager().remove(mCacheOverlay);
      refreshRoute();
    } else {
      mMapView.getOverlayManager().remove(mAimOverlay);
    }

    mMapView.postInvalidate();
  }

  /*       End         */
  /*********************/

  /*****************/
  /* Inner classes */

  /* Reacts on touching event on the map objects */
  private class MapItemListener implements ItemizedIconOverlay.OnItemGestureListener<MapObject> {
    public static final String SEARCH_DLG_TAG = "search dialog";

    public boolean onItemSingleTapUp(int i, MapObject mapObject) {
      final Geocache cache = mapObject.getGeocache();
      if(cache != null) {
        SearchCacheDialog dialog = new SearchCacheDialog(cache, mapObject.getDrawable());
        dialog.show(getFragmentManager(), SEARCH_DLG_TAG);
      }

      /* Return true to prevent calling this method */
      /* for a second magic item */
      return true;
    }

    public boolean onItemLongPress(int i, MapObject mapObject) {
      return false;
    }
  }

  private class ScrollZoomListener implements MapListener {
    public boolean onScroll(ScrollEvent scrollEvent) {
      mLastPoint = (GeoPoint) mMapView.getMapCenter();
      mapUpdate(true);
      return true;
    }

    public boolean onZoom(ZoomEvent zoomEvent) {
      final boolean zoomingIn = mLastZoomLevel < zoomEvent.getZoomLevel();
      mLastZoomLevel = zoomEvent.getZoomLevel();
      mapUpdate(!zoomingIn);
      return true;
    }

    /** Fetch cache database when allowed */
    private void mapUpdate(boolean notZoomIn) {
      if(!mNavigationEnabled && mLastZoomLevel > ZOOM_LEVEL_UPDATE_LIMIT
          && notZoomIn) {
        /* Run a thread to fetch the cache database */
        runUpdateThread();
      }
    }

    private void runUpdateThread() {
      final ViewInThreadHandler handler = new ViewInThreadHandler();
      new Thread(new Runnable() {
        public void run() {
          handler.setProgressState(ProgressBar.VISIBLE,
              getString(R.string.progress_fetch_cache));
          updateGeocacheMap();
          handler.setProgressState(ProgressBar.GONE,
              getString(R.string.progress_ready));
        }
      }).start();
    }
  }

  /** Provides methods to display or change UI components */
  private class ViewInThreadHandler {
    /** Displays a toast in the activity context running in a thread */
    private void showToast(final String message, final int duration) {
      final Activity activity = getActivity();
      activity.runOnUiThread(new Runnable() {
        public void run() {
          Toast.makeText(activity.getApplicationContext(), message, duration).show();
        }
      });
    }

    /**
     * Change the message of the progress text and visibility of the progress bar.
     */
    private void setProgressState(final int visibility, final String text) {
      getActivity().runOnUiThread(new Runnable() {
        public void run() {
          mProgressBar.setVisibility(visibility);
          mProgressText.setText(text);
        }
      });
    }
  }

  /** Fetch geocaching data and make results accessible */
  private class CacheDataFetcher {
    private GeocachingService mService;
    private List<Geocache> mList;
    private boolean mSuccessful;

    private CacheDataFetcher() {
      mService = new ComOpencachingService();
      mList = null;
      mSuccessful = false;
    }

    /** prepare request and start fetching */
    public CacheDataFetcher invoke() {
      final ComOpencachingRequestCollection collection =
          new ComOpencachingRequestCollection();
      final GeoCoordinateConverter converter = new GeoCoordinateConverter();

      final BoundingBoxE6 bbox = mMapView.getProjection().getBoundingBox();
      collection.addParameter(new BBox(false,
          (float) converter.microToDecimalDegree(bbox.getLatSouthE6()),
          (float) converter.microToDecimalDegree(bbox.getLonWestE6()),
          (float) converter.microToDecimalDegree(bbox.getLatNorthE6()),
          (float) converter.microToDecimalDegree(bbox.getLonEastE6())));
      collection.addParameter(new Limit(FETCH_LIMIT));

      mList = mService.fetchDatabase(collection);
      mSuccessful = mList != null;

      return this;
    }

    public boolean wasSuccessful() {
      return mSuccessful;
    }

    public GeocachingService getService() {
      return mService;
    }

    public List<Geocache> getList() {
      return mList;
    }
  }

  /*      End      */
  /*****************/
}
