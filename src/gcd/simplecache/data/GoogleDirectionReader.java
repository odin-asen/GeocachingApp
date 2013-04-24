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
      Log.e(LOG_TAG, "Could not read json object", e);
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
    final JSONObject firstRoute = routes.getJSONObject(0);
    final JSONArray legs = firstRoute.getJSONArray("legs");

    /* Initialise for the case that each leg has one step */
    final List<DTOLocation> locationList = new ArrayList<DTOLocation>();
    for (int legIndex = 0; legIndex < legs.length(); legIndex++) {
      final JSONObject leg = legs.getJSONObject(legIndex);
      fillStepList(locationList, leg);
    }
    return locationList;
  }
//  "routes": [ {
//    "summary": "I-40 W",
//        "legs": [ {
//      "steps": [ {
//        "travel_mode": "DRIVING",
//            "start_location": {
//          "lat": 41.8507300,
//              "lng": -87.6512600
//        },
//        "end_location": {
//          "lat": 41.8525800,
//              "lng": -87.6514100
//        },
  /** fills a DTOLocation list with information from a legs array */
  private void fillStepList(List<DTOLocation> stepList, JSONObject leg)
      throws JSONException {
    final JSONArray steps = leg.getJSONArray("steps");
    for (int index = 0; index < steps.length(); index++) {
      final JSONObject step = steps.getJSONObject(index);
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
