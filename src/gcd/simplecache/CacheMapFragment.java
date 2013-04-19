package gcd.simplecache;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.business.map.MapObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

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
  private ArrayList<MapObject> mapObjects;

  public CacheMapFragment() {
    mapObjects = null;
    lastZoomLevel = 13;
    lastPoint = new GeoPoint(0.0,0.0);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_cachemap, container, false);
  }

  public void updateUserPosition(Location location) {
    GeoCoordinateConverter converter = new GeoCoordinateConverter();
    GeoPoint currentPoint =
        converter.geocachingToGeoPoint(converter.locationToGeocaching(location));

    /* set the user object to the map */
    Resources res = getActivity().getResources();
    MapObject userObject = new MapObject("Geocacher", "Current Position", currentPoint);
    userObject.setMarker(res.getDrawable(R.drawable.position_cross));
    removeUserObject();
    mapObjects.add(userObject);

    saveLastPointAndZoom(currentPoint);
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

  /* Removes the first user object from the map array */
  private void removeUserObject() {
    if(mapObjects == null)
      return;
    for (int index = 0; index < mapObjects.size(); index++) {
      if(mapObjects.get(index).getType().isUser())
        mapObjects.remove(index);
    }
  }

  /**
   * <b>This is not yet functional.</b>
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void loadMapObjects(List<Object> cacheList) {

  }
}
