package gcd.simplecache.business.geocaching;

import java.util.List;

/**
 * Represents a geocaching service. This class can be extended to provide
 * connection to different geocaching services, such as geocaching.com and
 * opencaching.com.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 14.04.13
 */
abstract public class GeocachingService {
  protected String error;

  /* Constructors */
  protected GeocachingService() {
    error = "";
  }

  /* Methods */

  /**
   * Logs in to the geocaching service.
   * If the login wasn't successful, an error message can be called
   * with {@link #getError()}.
   * @return True if it was successful, else false.
   */
  abstract public boolean login();

  /**
   * Logs the current user out from the geocaching service.
   */
  abstract public void logout();

  /**
   * Gets the whole information for a geocache. This might be something like
   * the cache description, position, difficulty, etc...
   * In case of an error, null will be returned. The error can then be fetched with
   * with {@link #getError()}.
   * @param cacheID Identifier for the geocache.
   * @return A Geocache representing the whole geocache.
   */
  abstract public Geocache getCacheInfo(String cacheID);

  /**
   * Not yet sure, how to implement it and what parameters are needed in general.
   * Gets geocache information for a certain criteria.
   * In case of an error, null will be returned. The error can then be fetched with
   * with {@link #getError()}.
   * @return
   */
  abstract public List<Geocache> fetchDatabase();

  /* Getter and Setter */

  public String getError() {
    return error;
  }
}
