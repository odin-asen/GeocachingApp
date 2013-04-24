package gcd.simplecache.business.geocaching.request.com.opencaching;

import android.text.TextUtils;
import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * status
 * List of the statuses of geocaches to be returned.
 * If no status parameter is specified, then only active geocaches are returned.
 * Otherwise, only geocaches that have the status matching what is listed are returned.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Status extends ComOpencachingParameter {
  private static final String SEPARATOR = "&";
  public static final String ACTIVE = "Active";
  public static final String ARCHIVED = "Archived";
  public static final String REVIEW = "Review";

  /* Constructors */
  /**
   * Creates the status parameter. A status can be one or more of
   * these values:<br/>
   * <ul>
   *   <li>Active</li>
   *   <li>Archived</li>
   *   <li>Review</li>
   * </ul>
   * Note: Review cannot be used in conjunction with Active and/or Archived.
   * @param values Array of values for the status. Use the class constants.
   */
  public Status(String...values) {
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
