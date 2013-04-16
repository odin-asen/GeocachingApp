package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Limits returned geocaches to those containing a string somewhere in their title.
 * The search string does not have to start at the beginning of a word in the title.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Name extends ComOpencachingParameter {
  private static final String NAME = "name";
  private static final String EQUALS = "=";

  /* Constructors */
  /**
   * Creates the name parameter.
   * @param value String that is in the title of a cache.
   */
  public Name(String value) {
    super(NAME, value);
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
