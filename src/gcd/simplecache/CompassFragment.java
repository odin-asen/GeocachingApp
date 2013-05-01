package gcd.simplecache;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.map.GeoCoordinateConverter;


/**
* Compass Fragment. This view shows the compass
*/
public class CompassFragment extends Fragment {
  CompassView cView;
  private boolean isNav = false;
  private CacheNavigationInfo mNavigationInfo;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //View view = inflater.inflate(R.layout.fragment_compass, container, false);
    cView = new CompassView(getActivity());
    return cView;
	}

  @Override
  public void onStart() {
    super.onStart();

    refresh();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof CacheNavigationInfo) {
      mNavigationInfo = (CacheNavigationInfo) activity;
    } else {
      throw new ClassCastException(activity.toString()
          + " must implement "+CacheNavigationInfo.class.getName());
    }
  }
	  
  public void updateCompass (float azimuth) {
    cView.updateCompass(azimuth);
  }

  public void updateCurrent (Location location) {
    cView.updateCurrentLoc(location);
    if (mNavigationInfo.isNavigating()) {
      cView.updateNavigation(location);
    }
  }
	  
  public void updateDestination(Location location) {
    cView.updateDestLoc(location);
  }

  /**
   * Applies the current position of the user to refresh
   * the compass.
   */
  public void refresh() {
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();

    /* set current location */
    double[] coordinates = converter.getGeoDecimal(mNavigationInfo.getUserPoint());
    Location dummyLoc = new Location("");
    dummyLoc.setLatitude(coordinates[0]);
    dummyLoc.setLongitude(coordinates[1]);
    updateCurrent(dummyLoc);
  }
}
