package gcd.simplecache.business.map;

import android.util.Log;
import gcd.simplecache.data.GoogleDirectionReader;
import gcd.simplecache.dto.geocache.DTOLocation;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 23.04.13
 */
public class RoutingService {
  private static final String LOG_TAG = RoutingService.class.getName();
  private static final String GEOMETRY_COLLECTION = "GeometryCollection";

  private String mError;
  private String READ_ERROR_USER_MESSAGE;

  /****************/
  /* Constructors */
  public RoutingService() {
    mError = "";
  }

  /*     End      */
  /****************/

  /***********/
  /* Methods */

  /**
   * Returns null if the path couldn't be fetched for some reason.
   * Refer to getError to receive a printable user message.
   * @param src Starting point.
   * @param dst Destination point.
   * @return A list with DTOLocation objects.
   */
  public List<DTOLocation> getPath(GeoPoint src, GeoPoint dst) {
    List<DTOLocation> locations = null;

    /* connect to map web service */
    String urlString = buildRequest(src, dst);
    Log.d(LOG_TAG, "Routing fetch URL=" + urlString);

    /* get the kml (XML) doc and parse it */
    /* to get the coordinates(direction route) */

    final InputStream input = open(urlString);
    if(input != null) {
      READ_ERROR_USER_MESSAGE = "Could not read routing information. Please contact the support!";
      try {
        final GoogleDirectionReader reader = new GoogleDirectionReader();
        final String jsonString = reader.readInputStream(input);
        locations = reader.readJSON(jsonString);
        if(locations == null)
          mError = READ_ERROR_USER_MESSAGE;
      } catch (Exception e) {
        Log.e(LOG_TAG, e.getMessage());
        mError = READ_ERROR_USER_MESSAGE;
      }
    }

    return locations;
  }

  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

  private String buildRequest(GeoPoint src, GeoPoint dest) {
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();
    final StringBuilder urlString = new StringBuilder();

    urlString.append("http://maps.googleapis.com/maps/api/directions/json?");
    urlString.append("origin=");// from
    urlString.append(converter.microToDecimalDegree(src.getLatitudeE6()));
    urlString.append(",");
    urlString.append(converter.microToDecimalDegree(src.getLongitudeE6()));
    urlString.append("&destination=");// to
    urlString.append(converter.microToDecimalDegree(dest.getLatitudeE6()));
    urlString.append(",");
    urlString.append(converter.microToDecimalDegree(dest.getLongitudeE6()));
    urlString.append("&sensor=true");

    return urlString.toString();
  }

  /**
   * <em>Should probably be put in a helper class</em><br/>
   * Opens a http connection. Refer to
   * {@link gcd.simplecache.business.geocaching.GeocachingService#openHttpConnection(String)}
   */
  private HttpURLConnection getHttpConnection(String urlString)
      throws IOException {
    final URL url = new URL(urlString);
    final URLConnection conn = url.openConnection();
    final HttpURLConnection conection;

    if (!(conn instanceof HttpURLConnection))
      throw new IOException("Not an HTTP connection");

    /* setup http connection */
    try {
      conection = (HttpURLConnection) conn;
      conection.setInstanceFollowRedirects(true);
      conection.setRequestMethod("GET");
      conection.connect();
    } catch (IOException e) {
      throw new IOException("Error during connect.");
    }

    return conection;
  }

  private InputStream open(String requestURL) {
    InputStream in = null;
    try {
      final HttpURLConnection connection = getHttpConnection(requestURL);
      if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
        in = connection.getInputStream();
      else mError = "Wrong http request";
    } catch (IOException e) {
      Log.e(LOG_TAG, e.getMessage());
      mError = "Could not get routing information due to a connection error.";
    }
    return in;
  }

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */

  public String getError() {
    return mError;
  }

  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */
  /*****************/
}
