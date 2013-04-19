package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Returns the hint of the cache depending on the parameter.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Hint extends ComOpencachingParameter {
  private static final String NAME = "hint";
  private static final String EQUALS = "=";

  /* Constructors */
  /**
   * Creates the hint parameter.
   * @param hint Specifies whether to get the hint of the caches or not.
   */
  public Hint(boolean hint) {
    super(NAME, Boolean.toString(hint));
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
