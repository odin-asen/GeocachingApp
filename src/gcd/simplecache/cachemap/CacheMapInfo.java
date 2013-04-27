package gcd.simplecache.cachemap;

import gcd.simplecache.business.geocaching.GeocachingPoint;

/**
 * Container for cache map information.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 27.04.13
 */
public interface CacheMapInfo {
  public boolean isNavigating();
  public GeocachingPoint getUserPoint();
  public GeocachingPoint getAimPoint();
}
