package gcd.simplecache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends FragmentActivity  {
  private static final String ID_TS_MAP = "map";
  private static final String ID_TS_COMPASS = "compass";
  private FragmentTabHost mTabHost;
  private GPSTracker gps;
  newLocation myReceiver = null;
  Boolean myReceiverIsRegistered = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.activity_main);
    mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);  

    /* Add map and compass tabs */
    addTab(ID_TS_MAP, this.getString(R.string.tab_title_map), null, Fragment.class);
    addTab(ID_TS_COMPASS, this.getString(R.string.tab_title_compass), null, CompassFragment.class); 
    
    myReceiver = new newLocation();
    
    gps = new GPSTracker(this);
    
}

  	@Override
  	protected void onPause() {
  		super.onPause();
  		if(myReceiverIsRegistered) {
  			unregisterReceiver(myReceiver);
  		}
  	}
  	
 	@Override
  	protected void onResume() {
 		super.onResume();
 		if(!myReceiverIsRegistered) {
  			registerReceiver(myReceiver, new IntentFilter("bla"));
  		}
  	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

  /* Helper method that creates and adds a tab to the TabHost tabHost */
  private void addTab(String id, String title, Drawable icon, Class fragmentClass) {
    TabHost.TabSpec tabSpec = mTabHost.newTabSpec(id);
    tabSpec.setIndicator(title, icon);
    mTabHost.addTab(tabSpec, fragmentClass, null);
  }
  
  public class newLocation extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("get","intent");
			Bundle extras = intent.getExtras();
			Double lat = extras.getDouble("lat");
			if(mTabHost.getCurrentTabTag() == "compass") {
			CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(ID_TS_COMPASS);
			System.out.println(compass);
			compass.update(lat);
			}
		}
	}
}


  
  
  