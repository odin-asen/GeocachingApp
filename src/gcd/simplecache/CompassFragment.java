package gcd.simplecache;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;



	/**
	* Compass Fragment. This view shows the compass
	*/
	public class CompassFragment extends Fragment {
	
	  CompassView view;

	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                           Bundle savedInstanceState) {
		  
		  //View view = inflater.inflate(R.layout.fragment_compass, container, false);
		  view = new CompassView(getActivity());
		  return view;
	}
	  
//	  @Override
//	  public void onActivityCreated(Bundle savedInstanceState) {
//		  
//		  super.onActivityCreated(savedInstanceState);
//		  GPSTracker gps = new GPSTracker(getActivity());
//		  double bla = gps.getLatitude();
//		  double blub = gps.getLongitude();
//		  CompassView view = new CompassView(getActivity());
//		  view.setLatitude(bla);
//		  view.setLongitude(blub);
//	        
//	  }
	  
	  public void update (Double lat) {
		  view.setLatitude(lat);
	  }
  
	  
	  	  	
}
