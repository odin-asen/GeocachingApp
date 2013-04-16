package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.Parameter;

/**
 * Limits returned geocaches to those caches that have an oxcode that is in the list.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class CacheID extends Parameter {
  private static final String NAME = "oxcode";
  private static final String SEPARATOR = ",";
  private static final String EQUALS = "=";
  private boolean mExclude;

  /* Constructors */
  /**
   * Creates the oxcode parameter. The constructor takes one or more oxcodes.
   * @param ids Array of cache IDs.
   */
  public CacheID(String...ids) {
    super(NAME, "");
    setValue(ids);
  }

  private void setValue(String[] ids) {
    String result = "";
    for (String id : ids) {
      if(!result.isEmpty())
        result = result+SEPARATOR;
      result = result+id;
    }
    value = result;
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
