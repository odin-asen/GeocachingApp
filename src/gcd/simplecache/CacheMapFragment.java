package gcd.simplecache;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.business.map.MapObject;
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

  private MapView mapView;
  private MapController controller;
  private GeoPoint lastPoint;
  private int lastZoomLevel;

  /* Map overlay variables */
  private ItemizedIconOverlay<MapObject> userOverlay;
  private ItemizedOverlay<MapObject> cacheOverlay;
  private ItemizedOverlay<MapObject> aimOverlay;

  public CacheMapFragment() {
    userOverlay = null;
    cacheOverlay = null;
    aimOverlay = null;
    lastZoomLevel = 13;
    lastPoint = new GeoPoint(0.0,0.0);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_cachemap, container, false);
  }

  public void updateUserPosition(Location location) {
    mapView.getOverlayManager().remove(userOverlay);

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
        getActivity(), objectList, new MapItemListener());
    userOverlay.addItem(userObject);
    mapView.getOverlayManager().add(userOverlay);

    saveLastPointAndZoom(currentPoint);
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
    if(lastPoint != null)
      lastPoint = (GeoPoint) savedInstance.getSerializable(LAST_POINT);
    lastZoomLevel = savedInstance.getInt(LAST_ZOOM);
    initMap();
  }

  @Override
  public void onStart() {
    super.onStart();

    /* Go to the last point */
    controller.setZoom(lastZoomLevel);
    controller.setCenter(lastPoint);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(LAST_POINT, lastPoint);
    outState.putInt(LAST_ZOOM, lastZoomLevel);
  }

  /* Initialise MapView and MapController objects */
  private void initMap() {
    mapView = (MapView) getActivity().findViewById(R.id.cachemap);
    controller = mapView.getController();
    int zoom = 13;

    mapView.setTileSource(TileSourceFactory.MAPNIK);
    mapView.setBuiltInZoomControls(true);
    mapView.setMultiTouchControls(true);
    controller.setZoom(zoom);
  }

  /* save last point and zoom to instance variables */
  private void saveLastPointAndZoom(GeoPoint geoPoint) {
    lastZoomLevel = mapView.getZoomLevel();
    lastPoint = geoPoint;
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
}
