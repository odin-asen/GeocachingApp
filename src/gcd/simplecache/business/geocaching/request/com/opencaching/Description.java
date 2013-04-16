package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Fetch the description for the cache or the caches in the list.
 * Avoid fetching descriptions unless you really need the description
 * for every geocache. Getting the geocache descriptions can triple
 * (or more) the size of the returned data. When possible get a list of geocaches
 * without descriptions, and then get the descriptions for individual geocaches
 * as necessary.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Description extends ComOpencachingParameter {
  private static final String NAME = "description";
  private static final String EQUALS = "=";
  public static final String HTML = "html";
  public static final String USER = "user";
  public static final String NONE = "none";

  /* Constructors */
  /**
   * Creates the description parameter. The description can be on of the three
   * values:<br/>
   * <ul>
   *   <li>
   *     <b>html</b><br/>
   *     Cache descriptions are returned as HTML for all geocaches.
   *   </li>
   *   <li>
   *     <b>user</b><br/>
   *     Cache descriptions are returned as entered by the geocache
   *     owner entered it for all geocaches.
   *   </li>
   *   <li>
   *     <b>none</b><br/>
   *     Cache descriptions are not returned.
   *   </li>
   * </ul>
   * @param value Value for the description parameter. Use one of the class constants.
   */
  public Description(String value) {
    super(NAME, value);
  }

  /* Methods */

  @Override
  public String formatString() {
    return name+ EQUALS + value;
  }
}
