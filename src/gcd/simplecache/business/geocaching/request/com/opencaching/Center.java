package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Returns a list of geocaches closest to the specified point side sorted
 * by the distance from that point.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Center extends ComOpencachingParameter {
  private static final String NAME = "center";
  private static final String SEPARATOR = ",";
  private static final String EQUALS = "=";

  /* Constructors */
  /**
   * Creates the center parameter. The parameters {@code lat} and {@code long}
   * define the point.
   * Coordinates should be in decimal degrees (e.g. 21.4578°).
   * Use positive numbers for north latitude and east longitude and negative
   * numbers of south latitude and west longitude.
   * The box cannot cross the 180° longitude line or the 90° or -90° points.
   * @param latitude Latitude value for the point.
   * @param longitude Longitude value for the point.
   */
  public Center(float latitude, float longitude) {
    super(NAME, Float.toString(latitude)+SEPARATOR+Float.toString(longitude));
  }

  /* Methods */

  @Override
  public String formatString() {
    return name + EQUALS + value;
  }
}
