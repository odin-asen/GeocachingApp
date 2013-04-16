package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Sets the maximum number of geocaches that will be returned.
 * Defaults to 100 if no limit is specified.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Limit extends ComOpencachingParameter {
  private static final String NAME = "limit";
  private static final String EQUALS = "=";

  /* Constructors */
  /**
   * Creates the limit parameter. Can be between 1 and 5000.
   * @param limit Specifies the limit of caches.
   */
  public Limit(int limit) {
    super(NAME, Integer.toString(limit));
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
