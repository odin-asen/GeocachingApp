package gcd.simplecache.cachemap;

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
import gcd.simplecache.R;
import gcd.simplecache.SearchCacheDialog;
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
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

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
  private static final String LOG_TAG = CacheMapFragment.class.getName();
  private static final int ZOOM_LEVEL_UPDATE_LIMIT = 8;
  private static final int FETCH_LIMIT = 500;

  private ProgressBar mProgressBar;
  private TextView mProgressText;
  private CacheMapViewContainer mContainer;

  private CacheMapInfo mCacheMapInfo;

  /****************/
  /* Constructors */

  public CacheMapFragment() {
    mContainer = new CacheMapViewContainer(
        new ScrollZoomListener(), new MapItemListener());
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
    mContainer.setMapView((MapView) view.findViewById(R.id.cache_map_view));

    return view;
  }

  public void updateUserPosition(Location location) {
    mContainer.updateUserPosition(location);
  }

  @Override
  public void onActivityCreated(Bundle savedInstance) {
    super.onActivityCreated(savedInstance);

    mContainer.setContext(getActivity());
  }

  @Override
  public void onStart() {
    super.onStart();

    refresh();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof CacheMapInfo) {
      mCacheMapInfo = (CacheMapInfo) activity;
    } else {
      throw new ClassCastException(activity.toString()
          + " must implement "+CacheMapInfo.class.getName());
    }
  }

  /**
   * This method sets the destination given by the CachMapInfo object
   * and refreshes the route if the navigation is enabled.
   * Only the aim, the user and the route to the aim will
   * be displayed on the map.<br/>
   * When the method is called and the navigation is disabled,
   * the map will destroy this destination.<br/>
   * It should be called in an own thread.
   */
  public void showAim() {
    if(!mCacheMapInfo.isNavigating())
      return;

    /* Get the route if possible */
    final RoutingService service = new RoutingService();
    final List<DTOLocation> path;
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();
    final GeoPoint userPoint =
        converter.geocachingToGeoPoint(mCacheMapInfo.getUserPoint());
    final GeoPoint destination =
        converter.geocachingToGeoPoint(mCacheMapInfo.getAimPoint());

    if(userPoint == null)
      path = null;
    else {
      path = service.getPath(userPoint, destination);
      if(path == null)
        new ViewInThreadHandler().showToast("Could not set route path", Toast.LENGTH_LONG);
    }

    mContainer.refreshRoute(mCacheMapInfo.getCurrentCache(), path);
    mContainer.updateOverlays();
  }

  public void refresh() {
    mContainer.setView(!mCacheMapInfo.isNavigating(), mContainer.getZoomLevel(),
        mContainer.getLastPoint());
  }

  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

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
      mContainer.updateGeocaches(list);
      Log.d(LOG_TAG, "finished update");
    } else {
      Log.d(LOG_TAG, "error fetching");
      new ViewInThreadHandler().showToast(service.getError(), Toast.LENGTH_LONG);
    }
  }

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */
  /*       End         */
  /*********************/

  /*****************/
  /* Inner classes */

  /* Reacts on touching event on the map objects */
  class MapItemListener implements ItemizedIconOverlay.OnItemGestureListener<MapObject> {
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
      mContainer.setLastPoint((GeoPoint) scrollEvent.getSource().getMapCenter());
      mapUpdate(true);
      return true;
    }

    public boolean onZoom(ZoomEvent zoomEvent) {
      final boolean zoomingIn = mContainer.getZoomLevel() < zoomEvent.getZoomLevel();
      mContainer.setZoomLevel(zoomEvent.getZoomLevel());
      mapUpdate(!zoomingIn);
      return true;
    }

    /** Fetch cache database when allowed */
    private void mapUpdate(boolean notZoomIn) {
      if(!mCacheMapInfo.isNavigating()
          && (mContainer.getZoomLevel() > ZOOM_LEVEL_UPDATE_LIMIT)
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

      final float[] mapBounds = mContainer.getMapBounds();
      collection.addParameter(new BBox(false,
          mapBounds[2], mapBounds[3], mapBounds[0], mapBounds[1]));
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
