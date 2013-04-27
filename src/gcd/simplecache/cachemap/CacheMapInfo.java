package gcd.simplecache.cachemap;

import gcd.simplecache.business.geocaching.Geocache;
import gcd.simplecache.business.geocaching.GeocachingPoint;

/**
 * Container for cache map information.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 27.04.13
 */
public interface CacheMapInfo {
  boolean isNavigating();
  GeocachingPoint getUserPoint();
  GeocachingPoint getAimPoint();
  Geocache getCurrentCache();
}
