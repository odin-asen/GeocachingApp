package gcd.simplecache;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.map.MapObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.util.List;

/**
 * Map view fragment. This map view shows the caches on a map.
 */
public class CacheMapFragment extends Fragment {
  private MapView mapView;
  private MapController controller;
  private GeoPoint lastPoint;
  private int lastZoomLevel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_cachemap, container, false);
  }

  public void updateUserPosition(Location location) {

  }

  @Override
  public void onActivityCreated(Bundle savedInstance) {
    super.onActivityCreated(savedInstance);
    lastPoint = null;
    initMap();
  }

  @Override
  public void onStart() {
    super.onStart();

    /* Go to the last point */
    if(lastPoint != null) {
      controller.setZoom(lastZoomLevel);
      controller.setCenter(lastPoint);
    }
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

  /**
   * <b>This is not yet functional.</b>
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void loadMapObjects(List<Object> cacheList) {

  }
}

class MapObjectFactory {
  static MapObject createAim() {
    return null;
  }

  static MapObject createMultiCache() {
    return null;
  }

  static MapObject createRiddleCache() {
    return null;
  }

  static MapObject createTraditionalCache() {
    return null;
  }

  static MapObject createUserObject() {
    return null;
  }
}
