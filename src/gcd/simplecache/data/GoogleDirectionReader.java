package gcd.simplecache.data;

import android.text.TextUtils;
import android.util.Log;
import gcd.simplecache.dto.geocache.DTOLocation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 24.04.13
 */
public class GoogleDirectionReader {
  private static final String LOG_TAG = GoogleDirectionReader.class.getName();

  /****************/
  /* Constructors */
  /*     End      */
  /****************/

  /***********/
  /* Methods */

  /**
   * <em>Should be in a helper class.</em>
   * Read an input stream and return the content as string.
   * @param stream InputStream object with content.
   * @return A String with the input stream content.
   * @throws java.io.IOException
   */
  public String readInputStream(InputStream stream) throws IOException {
    String result = "";
    BufferedReader reader;

    reader = new BufferedReader(new InputStreamReader(stream));
    String line;

    while ((line = reader.readLine()) != null) {
      if(!TextUtils.isEmpty(result))
        result = result + "\n";
      result = result + line;
    }

    try {
      reader.close();
    } catch (IOException e) {
      Log.e(LOG_TAG, "Error closing stream reader", e);
    }

    return result;
  }

  /** Returns null if the json file could not be read. */
  public List<DTOLocation> readJSON(String jsonString) throws IllegalFormatException {
    try {
      JSONObject jsonRoutes = new JSONObject(jsonString);
      return getLocationList(jsonRoutes);
    } catch (Exception e) {
      Log.e(LOG_TAG, "Could not read json object");
    }

    return null;
  }

  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

  /* return a DTOLocation list object with data from a json file */
  private List<DTOLocation> getLocationList(JSONObject jsonRoute)
      throws JSONException {
    final JSONArray routes = jsonRoute.getJSONArray("routes");
    final List<DTOLocation> locationList = new ArrayList<DTOLocation>(routes.length());

    for (int legIndex = 0; legIndex < routes.length(); legIndex++) {
      final JSONArray legs = routes.getJSONArray(legIndex);
      fillStepList(locationList, legs);
    }
    return locationList;
  }

  /** fills a DTOLocation list with information from a legs array */
  private void fillStepList(List<DTOLocation> stepList, JSONArray legs)
      throws JSONException {
    for (int stepIndex = 0; stepIndex < legs.length(); stepIndex++) {
      final JSONObject step = legs.getJSONObject(stepIndex);
      final JSONObject startLocation = step.getJSONObject("start_location");

      /* add location */
      final double latitude = startLocation.getDouble("lat");
      final double longitude = startLocation.getDouble("lng");
      stepList.add(new DTOLocation(latitude, longitude));
    }
  }
  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */
  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */
  /*****************/
}
