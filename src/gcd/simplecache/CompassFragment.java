package gcd.simplecache;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



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
