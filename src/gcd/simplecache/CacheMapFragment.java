package gcd.simplecache;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gcd.simplecache.business.map.MapObject;

import java.util.List;

/**
 * Map view fragment. This map view shows the caches on a map.
 */
public class CacheMapFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_cachemap, container, false);
  }

  /**
   * <b>This is not yet functional.</b>
   * This method takes a list of cache information and displays it on the map.
   * @param cacheList List with caches to show on the map.
   */
  public void loadMapObjects(List<Object> cacheList) {

  }
}

class MapObjectFactory {
  static MapObject createAim() {
    return null;
  }

  static MapObject createMultiCache() {
    return null;
  }

  static MapObject createRiddleCache() {
    return null;
  }

  static MapObject createTraditionalCache() {
    return null;
  }

  static MapObject createUserObject() {
    return null;
  }
}
