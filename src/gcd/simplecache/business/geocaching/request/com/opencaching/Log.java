package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Limits the number of logs that will be returned with each geocache.
 * It can also enable or disable log the log descriptions from the returned logs.
 * This is useful if only the information that a cache was found or not found is important
 * without the description of the log.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Log extends ComOpencachingParameter {
  private static final String NAME_LIMIT = "log_limit";
  private static final String NAME_COMMENT = "log_comment";
  private static final String SEPARATOR = "&";
  private static final String EQUALS = "=";
  private boolean mDescription;

  /* Constructors */
  /**
   * Creates the log_limit and log_comment parameters.
   * {@code limit} defines the amount of logs per cache.
   * {@code description} Enables or disables the return of the description of
   * the cache logs.
   * @param limit Limit for the number of logs returned for a cache.
   * @param description Send or do not send the description of the logs.
   */
  public Log(int limit, boolean description) {
    super(NAME_LIMIT, Integer.toString(limit));
    mDescription = description;
  }

  /* Methods */

  @Override
  public String formatString() {
    return name + EQUALS + value
        + SEPARATOR + NAME_COMMENT + EQUALS
        + Boolean.toString(mDescription);
  }
}
