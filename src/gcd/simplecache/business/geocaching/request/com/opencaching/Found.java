package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * A user must be logged in for this parameter to have any affect.
 * Fetches caches depending on the found state of the user for a geocache.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Found extends ComOpencachingParameter {
  private static final String NAME = "found";
  private static final String EQUALS = "=";
  private boolean mExclude;

  /* Constructors */
  /**
   * Creates the found parameter. To fetch caches independent of the found criteria,
   * do not create this parameter.
   * @param found Specifies whether found or not found caches should be fetched.
   */
  public Found(boolean found) {
    super(NAME, Boolean.toString(found));
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
