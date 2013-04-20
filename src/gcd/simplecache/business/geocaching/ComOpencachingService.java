package gcd.simplecache.business.geocaching;

import android.util.Log;
import gcd.simplecache.business.geocaching.request.RequestCollection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * This class communicates with <a href=http://www.opencaching.com>opencaching.com</a>.
 * It is possible to request information from the geocaching database.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
public class ComOpencachingService extends GeocachingService {
  private static final String LOG_TAG = ComOpencachingService.class.getName();
  private static final String AUTHENTICATION_KEY = "Authorization=tIklJ776J534DNNI";
  private static final String OPENCACHING_PAGE = "http://test.opencaching.com";
  private static final String TARGET_GEOCACHE = "/api/geocache";
  private static final String QUESTION_MARK = "?";
  private static final String COM_AND = "&";

  private String mUserName;
  private String mPassword;

  /* Constructors */
  public ComOpencachingService() {
    super();
  }

  /* Methods */

  @Override
  public boolean login() {
    return false;
  }

  @Override
  public void logout() {

  }


  /**
   * Gets information for a geocache of the opencaching.com homepage.
   * {@code parameter} should be of class ComOpencachingRequestCollection to get an
   * appropriate request string for this database.
   * @param cacheID Identifier for the geocache.
   * @param parameter Parameter object that specifies the cache information.
   * @return A Geocache object filled with the desired data.
   */
  @Override
  public Geocache getCacheInfo(String cacheID, RequestCollection parameter) {
    Geocache cache = null;
    final String requestURL = buildRequestParameters(cacheID, parameter);
    final InputStream in = open(requestURL);
    if(in != null) {
      /* Read data from input stream */
      closeHttpConnection();
    }

    return cache;
  }

  /* build a request for one cache in json format */
  private String buildRequestParameters(String cacheID, RequestCollection parameter) {
    return OPENCACHING_PAGE + TARGET_GEOCACHE
        + "/" + cacheID + QUESTION_MARK + parameter.getRequestParameter()
        + COM_AND + AUTHENTICATION_KEY;
  }

  /* Open the http connection */
  private InputStream open(String requestURL) {
    InputStream in = null;
    try {
      openHttpConnection(requestURL);
      if(mHttpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
        in = mHttpConn.getInputStream();
    } catch (IOException e) {
      Log.e(LOG_TAG, e.getMessage());
      mError = "Could not get cache information due to a connection error.";
    }
    return in;
  }

  @Override
  public List<Geocache> fetchDatabase(RequestCollection parameter) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  /* Getter and Setter */

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    this.mPassword = password;
  }

  public String getUserName() {
    return mUserName;
  }

  public void setUserName(String userName) {
    this.mUserName = userName;
  }
}
