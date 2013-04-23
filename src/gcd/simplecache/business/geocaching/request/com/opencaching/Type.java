package gcd.simplecache.business.geocaching.request.com.opencaching;

import android.text.TextUtils;
import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * List of the types of geocaches to be returned.
 * If no type parameter is specified, all types are returned.
 * Otherwise, only the listed types are returned.

 Traditional Cache

 Multi-cache

 Unknown Cache - aka Puzzle

 Virtual Cache

 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Type extends ComOpencachingParameter {
  private static final String SEPARATOR = "&";
  public static final String TRADITIONAL = "Traditional%20Cache";
  public static final String MULTI = "Multi-cache";
  public static final String RIDDLE = "Unknown%20Cache";
  public static final String VIRTUAL = "Virtual%20Cache";

  /* Constructors */
  /**
   * Creates the type parameter. A status can be one or more of
   * these values:<br/>
   * <ul>
   *   <li>Traditional cache</li>
   *   <li>Multi cache</li>
   *   <li>Puzzle or Riddle cache</li>
   *   <li>Virtual cache</li>
   * </ul>
   * @param values Array of values for the type. Use the class constants.
   */
  public Type(String...values) {
    super("","");
    setValues(values);
  }

  private void setValues(String[] values) {
    value = "";
    for (String s : values) {
      if(!TextUtils.isEmpty(value))
        value = value + SEPARATOR;
      value = value + s;
    }
  }

  /* Methods */
  @Override
  public String formatString() {
    return value;
  }
}
