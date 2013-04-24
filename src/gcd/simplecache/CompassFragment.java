package gcd.simplecache;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



	/**
	* Compass Fragment. This view shows the compass
	*/
	public class CompassFragment extends Fragment {
	
	  CompassView cView;
	  

	  	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		  //View view = inflater.inflate(R.layout.fragment_compass, container, false);
		  cView = new CompassView(getActivity());
		  return cView;
	}
	  

	  
	  public void update (Location location) {
		  cView.updateCurrentLoc(location);
	  }
  
	  
	  	  	
}
