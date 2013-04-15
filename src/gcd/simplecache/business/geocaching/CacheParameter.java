package gcd.simplecache.business.geocaching;

/**
 * An object of this class specifies the parameters for a request to a geocaching
 * service for a geo cache. These are preferences like a certain, area of the cache,
 * difficulty minimum, if the description should be requested, etc...
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
abstract public class CacheParameter {
  /* Constructors */
  /* Methods */

  abstract public String getRequestParameter();

  /* Getter and Setter */
}
