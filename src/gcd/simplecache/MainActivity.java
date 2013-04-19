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
  
  private static final String ACTION_ID_GPS = "LocationChanged";
  private static final String ACTION_ID_COMPASS = "SensorChanged";  
  
  private FragmentTabHost mTabHost;
  private GPSService gps;
  private CompassService compass;
  messageReceiver receiver = null;
  Boolean receiverRegistered = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.activity_main);
    mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);  

    /* Add map and compass tabs */
    addTab(ID_TS_MAP, this.getString(R.string.tab_title_map), null, Fragment.class);
    addTab(ID_TS_COMPASS, this.getString(R.string.tab_title_compass), null, CompassFragment.class); 
    
    receiver = new messageReceiver();
    
    gps = new GPSService(this);
    compass = new CompassService(this);
    
}

  	@Override
  	protected void onPause() {
  		super.onPause();
  		if(receiverRegistered) {
  			unregisterReceiver(receiver);
  		}
  	}
  	
 	@Override
  	protected void onResume() {
 		super.onResume();
 		if(!receiverRegistered) {
  			registerReceiver(receiver, new IntentFilter("LocationChanged"));
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
  
  public class messageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction() == ACTION_ID_GPS){
				Log.d("Loc","Changed");
			} if (intent.getAction() == ACTION_ID_COMPASS) {	
				Log.d("Sensor", "Changed");
			}
		}
	}
}


  
  
  