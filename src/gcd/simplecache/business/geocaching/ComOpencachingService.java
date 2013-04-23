package gcd.simplecache.business.geocaching;

import android.util.Log;
import gcd.simplecache.business.geocaching.request.RequestCollection;
import gcd.simplecache.data.ComOpencachingReader;
import gcd.simplecache.dto.geocache.DTOGeocache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
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
      final ComOpencachingReader reader = new ComOpencachingReader();
      String result;

      try {
        /* Read string from input stream and convert the content */
        /* to an array */
        result = reader.readInputStream(in);
        cache = Geocache.toGeocache(reader.readJSON(result));
      } catch (IOException e) {
        mError = "Error reading the database.";
      }
    }

    closeHttpConnection();

    return cache;
  }

  /* build a request for a database fetch in json format */
  private String buildRequestParameters(RequestCollection parameter) {
    return OPENCACHING_PAGE + TARGET_GEOCACHE
        + QUESTION_MARK + parameter.getRequestParameter()
        + COM_AND + AUTHENTICATION_KEY;
  }

  /* build a request for one cache in json format */
  private String buildRequestParameters(String cacheID, RequestCollection parameter) {
    return OPENCACHING_PAGE + TARGET_GEOCACHE
        + "/" + cacheID + QUESTION_MARK + parameter.getRequestParameter()
        + COM_AND + AUTHENTICATION_KEY;
  }

  /** Open the http connection and fill mError in case of an error. */
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
    List<Geocache> geocaches = null;

    /* get request input stream */
    final String requestURL = buildRequestParameters(parameter);
    final InputStream in = open(requestURL);

    if(in != null) {
      final ComOpencachingReader reader = new ComOpencachingReader();

      try {
        /* Read string from input stream and convert the content */
        /* to an array */
        String result = reader.readInputStream(in);
        geocaches = readJSONResultString(reader, result);
      } catch (IOException e) {
        mError = "Error reading the database.";
      }
    }

    closeHttpConnection();

    return geocaches;
  }

  /** Read a json string and store it to a list */
  private List<Geocache> readJSONResultString(ComOpencachingReader reader, String result)
    throws IOException {
    final List<DTOGeocache> dtoGeocaches = reader.readManyJSON(result);

    /* could not read json file */
    if(dtoGeocaches == null)
      throw new IOException("Could not read json file");

    List<Geocache> geocaches = new ArrayList<Geocache>(dtoGeocaches.size());

    for (DTOGeocache dto : dtoGeocaches) {
      geocaches.add(Geocache.toGeocache(dto));
    }

    return geocaches;
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
