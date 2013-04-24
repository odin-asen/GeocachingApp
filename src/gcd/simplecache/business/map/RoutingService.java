package gcd.simplecache.business.map;

import android.util.Log;
import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 23.04.13
 */
public class RoutingService {
  private static final String LOG_TAG = RoutingService.class.getName();
  private static final String GEOMETRY_COLLECTION = "GeometryCollection";

  private String mError;

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
   * Returns an empty string if the path couldn't be fetched for some reason.
   * Refer to getError to receive a showable user message.
   * @param src Starting point.
   * @param dst Destination point.
   * @return A string with comma separated pairs for the path.
   */
  public String getPath(GeoPoint src, GeoPoint dst) {
    /* connect to map web service */
    String urlString = buildRequest(src, dst);
    Log.d(LOG_TAG, "Routing fetch URL=" + urlString);

    /* get the kml (XML) doc and parse it */
    /* to get the coordinates(direction route) */

    final InputStream input = open(urlString);
    if(input == null)
      return "";

    try {
      final Document doc;
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.parse(input);

      if (doc.getElementsByTagName(GEOMETRY_COLLECTION).getLength() > 0) {

        // String path =
        // doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
        String path = doc.getElementsByTagName(GEOMETRY_COLLECTION).item(0).getFirstChild().getFirstChild()
            .getFirstChild().getNodeValue();

        Log.d(LOG_TAG, "path=" + path);

        return path;
      }
    } catch (Exception e) {
      /* ParserConfigurationException not cached */
      /* because there's nothing special configured */
      Log.e(LOG_TAG, e.getMessage());
      mError = "Could not read routing information. Please contact the support!";
    }

    return "";
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
    urlString.append("&sensor=true&mode=walking");

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
