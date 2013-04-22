package gcd.simplecache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;

public class MainActivity extends FragmentActivity implements IntentActions {
  /* TabSpec IDs */
  private static final String TAG_TS_MAP = "map";
  private static final String TAG_TS_COMPASS = "compass";

  private FragmentTabHost mTabHost;
  private GPSService gps;
  private CompassService compass;
  private MessageReceiver receiver;
  private Boolean receiverRegistered;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
  
  public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();

      /* process intent action */
      if (action.equals(ACTION_ID_GPS)){
        changeLocation(intent);
        Log.d("Loc","Changed");
			} else if (action.equals(ACTION_ID_COMPASS)) {
				Log.d("Sensor", "Changed");
			} else if (action.equals(ACTION_ID_NAVIGATION)) {
        String destination = changeNavigation(intent);
        Log.d("Navigation", "Changed to "+destination);
      } else if (action.equals(ACTION_ID_DESCRIPTION)) {

      }
		}

    /* update compass and map for location change */
    private void changeLocation(Intent intent) {
      final String currentTabTag = mTabHost.getCurrentTabTag();
      final Bundle extras = intent.getExtras();
      final Double lat = extras.getDouble("lat");
      final Double lon = extras.getDouble("lon");

      if (currentTabTag.equals(TAG_TS_COMPASS)) {
        CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
        compass.update(lat, lon);
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        Location location = new Location(getString(R.string.app_name));
        location.setLatitude(lat);
        location.setLongitude(lon);
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
        //Do compass related stuff
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        map.setNavigationEnabled(enabled);
        map.setDestination(Geocache.toGeocache(destination));
      }

      if(destination != null && destination.location != null)
        return destination.location.toString();
      else return new DTOLocation(0.0,0.0).toString();
    }
  }
}


  
  
  