package gcd.simplecache;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
  private FragmentTabHost mTabHost;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

    /* Add tab for the cache map */
    mTabHost.addTab(mTabHost.newTabSpec("map").setIndicator("Map"),
        Fragment.class, null);
    /* Add tab for the compass */
    mTabHost.addTab(mTabHost.newTabSpec("compass").setIndicator("Compass"),
        Fragment.class, null);
  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}