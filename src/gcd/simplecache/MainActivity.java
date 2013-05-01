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
import gcd.simplecache.business.geocaching.ComOpencachingService;
import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.geocaching.GeocachingPoint;
import gcd.simplecache.business.geocaching.GeocachingService;
import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;
import gcd.simplecache.business.geocaching.request.ComOpencachingRequestCollection;
import gcd.simplecache.business.geocaching.request.RequestCollection;
import gcd.simplecache.business.geocaching.request.com.opencaching.Description;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;

public class MainActivity extends FragmentActivity
    implements IntentActions, CacheNavigationInfo {
  /* TabSpec IDs */
  private static final String TAG_TS_MAP = "map";
  private static final String TAG_TS_COMPASS = "compass";
  private static final String TAG_TS_DETAILS = "details";

  private FragmentTabHost mTabHost;
  
  private GPSService gps;
  private CompassService compass;
  private MessageReceiver receiver;
  private Boolean receiverRegistered;

  /* CacheMapFragment variables */
  private boolean mNavigating;
  private GeocachingPoint mUserPoint;
  private GeocachingPoint mAimPoint;
  private Geocache mCurrentCache;

  public MainActivity() {
    mNavigating = false;
    mUserPoint = new GeocachingPoint();
    mAimPoint = new GeocachingPoint();
    mCurrentCache = new Geocache();
  }

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
    addTab(TAG_TS_DETAILS, this.getString(R.string.tab_title_details), null, DetailsFragment.class);

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
      registerReceiver(receiver, new IntentFilter(ACTION_ID_DETAILS));
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
        mNavigating = false;
        if(mTabHost.getCurrentTabTag().equals(TAG_TS_MAP))
          ((CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP)).refresh();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /**********************************/
  /* Implements CacheNavigationInfo */
  /**********************************/

  @Override
  public boolean isNavigating() {
    return mNavigating;
  }

  @Override
  public GeocachingPoint getUserPoint() {
    return mUserPoint;
  }

  @Override
  public GeocachingPoint getAimPoint() {
    return mAimPoint;
  }

  @Override
  public Geocache getCurrentCache() {
    return mCurrentCache;
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
          if(compass != null)
            compass.updateCompass(azimuth);
        }
        else {}
			} else if (action.equals(ACTION_ID_NAVIGATION)) {
        /* Change navigation and go to compass tab */
        String destination = changeNavigation(intent);
        updateCacheDetails(mCurrentCache.getId());
        Log.d("Navigation", "Changed to "+destination);
      } else if (action.equals(ACTION_ID_DETAILS)) {
        /* update details of the current cache */
        updateCacheDetails(intent.getExtras().getString(DETAILS_ID));
      }
		}

    /* Run a thread to update the current cache variable */
    private void updateCacheDetails(final String id) {
      /* set the details for the current cache */
      new Thread(new Runnable() {
        public void run() {
          GeocachingService service = new ComOpencachingService();
          RequestCollection<ComOpencachingParameter> collection =
              new ComOpencachingRequestCollection();
          collection.addParameter(new Description(Description.HTML));
          collection.addParameter(
              new gcd.simplecache.business.geocaching.request.com.opencaching.Log(0,false));
          mCurrentCache = service.getCacheInfo(id, collection);
        }
      }).start();
    }

    /* update compass and map for location change */
    private void changeLocation(Intent intent) {
      final String currentTabTag = mTabHost.getCurrentTabTag();
      final Bundle extras = intent.getExtras();
      final Location location = extras.getParcelable("loc");
      mUserPoint = new GeoCoordinateConverter().locationToGeocaching(location);

      if (currentTabTag.equals(TAG_TS_COMPASS)) {
        CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
        compass.updateCurrent(location);
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        map.updateUserPosition();
      }
    }

    /* update compass and map for navigation change */
    /* returns a string representation of the destination */
    private String changeNavigation(Intent intent) {
      final String currentTabTag = mTabHost.getCurrentTabTag();

      /* get/set navigation properties */
      final Bundle extras = intent.getExtras();
      mNavigating = extras.getBoolean(NAVIGATION_ENABLED);
      mCurrentCache = Geocache.toGeocache(
          (DTOGeocache) extras.getSerializable(NAVIGATION_DESTINATION));
      mAimPoint = mCurrentCache.getPoint();
      final double[] coordinates = new GeoCoordinateConverter().getGeoDecimal(mAimPoint);

      if (currentTabTag.equals(TAG_TS_COMPASS)) {
        CompassFragment compass = (CompassFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_COMPASS);
        Location location = new Location("a");
        location.setLongitude(coordinates[1]);
        location.setLatitude(coordinates[0]);
        compass.updateDestination(location);
      } else if(currentTabTag.equals(TAG_TS_MAP)) {
        final CacheMapFragment map = (CacheMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_TS_MAP);
        map.refresh();
      }

      return new DTOLocation(coordinates[0], coordinates[1]).toString();
    }
  }
}


  
  
  