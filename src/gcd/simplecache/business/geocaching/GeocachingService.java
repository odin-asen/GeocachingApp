package gcd.simplecache.business.geocaching;

import gcd.simplecache.business.geocaching.request.RequestCollection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Represents a geocaching service. This class can be extended to provide
 * connection to different geocaching services, such as
 * <a href=http://www.geocaching.com>geocaching.com</a> and
 * <a href=http://www.opencaching.com>opencaching.com</a>.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 14.04.13
 */
abstract public class GeocachingService {
  protected String mError;
  protected HttpURLConnection mHttpConn;

  /* Constructors */
  protected GeocachingService() {
    mError = "";
    mHttpConn = null;
  }

  /* Methods */

  /**
   * Opens an http connection to a specified url
   * and stores it to {@link #mHttpConn}.<br/>
   * The method first creates an {@link HttpURLConnection} object,
   * sets values to follow redirects and the request method to 'GET'.
   * The a connection will be established.
   * @param urlString Valid http url.
   * @throws IOException {@code urlString} is no valid http url.
   */
  protected void openHttpConnection(String urlString) throws IOException {
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();

    if (!(conn instanceof HttpURLConnection))
      throw new IOException("Not an HTTP connection");

    /* setup http connection */
    try {
      mHttpConn = (HttpURLConnection) conn;
      mHttpConn.setInstanceFollowRedirects(true);
      mHttpConn.setRequestMethod("GET");
      mHttpConn.connect();
    } catch (IOException e) {
      throw new IOException("Error during connect.");
    }
  }

  /**
   * Disconnects the http connection. Does nothing if no connection is
   * established. {@link #mHttpConn} is null after this method was called.
   */
  protected void closeHttpConnection() {
    if(mHttpConn != null) {
      mHttpConn.disconnect();
      mHttpConn = null;
    }
  }

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
   * Gets the the specified information for a geocache. This might be something like
   * the cache description, position, difficulty, etc...
   * In case of an error, null will be returned. The error can then be fetched with
   * with {@link #getError()}.
   * @param cacheID Identifier for the geocache.
   * @param parameter Parameter object that specifies the cache information.
   * @return A Geocache representing the whole geocache.
   */
  abstract public Geocache getCacheInfo(String cacheID, RequestCollection parameter);

  /**
   * Gets geocache information for a certain criteria.
   * In case of an error, null will be returned. The error can then be fetched with
   * with {@link #getError()}. A cache will have at least the cache id, if provided.
   * @param parameter Parameter object that specifies the cache information.
   * @return A list of geocaches with the specified parameter.
   */
  abstract public List<Geocache> fetchDatabase(RequestCollection parameter);

  /* Getter and Setter */

  public String getError() {
    return mError;
  }
}
