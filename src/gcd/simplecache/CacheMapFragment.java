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

  /* Map overlay variables */
  private ItemizedIconOverlay<MapObject> userOverlay;
  private ItemizedOverlay<MapObject> cacheOverlay;
  private ItemizedOverlay<MapObject> aimOverlay;

  public CacheMapFragment() {
    userOverlay = null;
    cacheOverlay = null;
    aimOverlay = null;
    mLastZoomLevel = 0;
    mLastPoint = new GeoPoint(0.0,0.0);
  }

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
    mMapView.getOverlayManager().remove(userOverlay);

    GeoCoordinateConverter converter = new GeoCoordinateConverter();
    GeoPoint currentPoint =
        converter.geocachingToGeoPoint(converter.locationToGeocaching(location));

    /* set the user object to the map */
    MapObject userObject = new MapObject("Geocacher", "Current Position", currentPoint);
    userObject.setMarker(getActivity().getResources().getDrawable(R.drawable.position_cross));
    final List<MapObject> objectList = new ArrayList<MapObject>(1);
    objectList.add(userObject);

    /* add overlay to the map */
    userOverlay = new ItemizedOverlayWithFocus<MapObject>(
        getActivity(), objectList, null);
    userOverlay.addItem(userObject);
    mMapView.getOverlayManager().add(userOverlay);

    saveLastPointAndZoom(currentPoint);
    mMapView.invalidate();
  }

  /**
   * <b>This is not yet functional.</b>
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void updateGeocacheObjects(List<Geocache> cacheList) {

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

    mMapView.invalidate();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(LAST_POINT, mLastPoint);
    outState.putInt(LAST_ZOOM, mLastZoomLevel);
    Log.d("map", "save instance");
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

  /* Inner classes */
  /* Reacts on touching event on the map objects */
  private class MapItemListener implements ItemizedIconOverlay.OnItemGestureListener<MapObject> {
    @Override
    public boolean onItemSingleTapUp(int i, MapObject mapObject) {
      return false;
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
