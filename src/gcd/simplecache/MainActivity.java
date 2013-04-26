package gcd.simplecache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;

public class MainActivity extends FragmentActivity implements IntentActions {
  /* TabSpec IDs */
  private static final String TAG_TS_MAP = "map";
  private static final String TAG_TS_COMPASS = "compass";
  private static final String NAV_DLG_TAG = "navigatetodialog";
  
  private static final int DIALOG_ALERT = 10;

  private FragmentTabHost mTabHost;
  
  private GPSService gps;
  private CompassService compass;
  private MessageReceiver receiver;
  private Boolean receiverRegistered;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
    setContentView(R.layout.activity_main);
    mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

    /* Add map and compass tabs */
    addTab(TAG_TS_MAP, this.getString(R.string.tab_title_map), null, CacheMapFragment.class);
    addTab(TAG_TS_COMPASS, this.getString(R.string.tab_title_compass), null, CompassFragment.class);

    receiver = new MessageReceiver();
    receiverRegistered = false;
    
    gps = new GPSService(this);
    compass = new CompassService(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if(receiverRegistered) {
      unregisterReceiver(receiver);
      receiverRegistered = false;
    }
  }
  	
 	@Override
  protected void onResume() {
 		super.onResume();
 		if(!receiverRegistered) {
      registerReceiver(receiver, new IntentFilter(ACTION_ID_GPS));
      registerReceiver(receiver, new IntentFilter(ACTION_ID_COMPASS));
      registerReceiver(receiver, new IntentFilter(ACTION_ID_DESCRIPTION));
      registerReceiver(receiver, new IntentFilter(ACTION_ID_NAVIGATION));
      receiverRegistered = true;
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
  

@Override
  public boolean onOptionsItemSelected(MenuItem item) {

      switch (item.getItemId()) {
          case R.id.navigate_to:
        	  NavigateToDialog dialog = new NavigateToDialog();
        	  dialog.show(getSupportFragmentManager(), "Nav Dialog");
              return true;
          case R.id.stop_navigation:
        	  return true;
          default:
              return super.onOptionsItemSelected(item);
      }
  }
  
   
  public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();

      /* process intent action */
      if (action.equals(ACTION_ID_GPS)){
        changeLocation(intent);
        Log.d("Loc","Changed");
			} else if (action.equals(ACTION_ID_COMPASS)) {
				 if (mTabHost.getCurrentTabTag().equals(TAG_TS_COMPASS)) {
					 	Bundle extras = intent.getExtras();
					 	float azimuth = extras.getFloat("azimuth");
				        CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
				        compass.updateCompass(azimuth);
				 }
				 else {}
			} else if (action.equals(ACTION_ID_NAVIGATION)) {
        /* Change navigation and go to compass tab */
        String destination = changeNavigation(intent);
	      mTabHost.setCurrentTabByTag(TAG_TS_COMPASS);
        Log.d("Navigation", "Changed to "+destination);
      } else if (action.equals(ACTION_ID_DESCRIPTION)) {

      }
		}

    /* update compass and map for location change */
    private void changeLocation(Intent intent) {
      final String currentTabTag = mTabHost.getCurrentTabTag();
      final Bundle extras = intent.getExtras();
      final Location location = extras.getParcelable("loc");

      if (currentTabTag.equals(TAG_TS_COMPASS)) {
        CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
        compass.updateCurrent(location);
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        map.updateUserPosition(location);
      }
    }

    /* update compass and map for navigation change */
    /* returns a string representation of the destination */
    private String changeNavigation(Intent intent) {
      final String currentTabTag = mTabHost.getCurrentTabTag();

      /* get navigation properties */
      final Bundle extras = intent.getExtras();
      final boolean enabled = extras.getBoolean(NAVIGATION_ENABLED);
      final DTOGeocache destination =
          (DTOGeocache) extras.getSerializable(NAVIGATION_DESTINATION);

      if (currentTabTag.equals(TAG_TS_COMPASS)) {
          CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
          Location location = new Location("a");
          location.setLongitude(destination.location.longitude);
          location.setLatitude(destination.location.latitude);
          compass.updateDestination(location);
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        final CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        map.setNavigationEnabled(enabled);
        new Thread(new Runnable() {
          public void run() {
            map.setDestination(Geocache.toGeocache(destination));
          }
        }).start();
      }

      if(destination != null && destination.location != null)
        return destination.location.toString();
      else return new DTOLocation(0.0,0.0).toString();
    }
  }
}


  
  
  