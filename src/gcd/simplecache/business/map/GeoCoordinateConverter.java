package gcd.simplecache.business.map;

import android.location.Location;
import gcd.simplecache.business.geocaching.GeocachingPoint;
import org.osmdroid.util.GeoPoint;

import static gcd.simplecache.business.geocaching.GeocachingPoint.Orientation;

/**
 * This class converts some representations of geo coordinates to others.
 * E.g. Micro degree coordinates to decimal degree coordinates.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 13.04.13
 */
public class GeoCoordinateConverter {
  private static final double MEGA = 1000000.0;
  private static final double MICRO = 1/MEGA;
  private static final int DECIMAL_PLACE = 3;

  /* Constructors */
  /* Methods */
  public double microToDecimalDegree(int microDegrees) {
    return MICRO*microDegrees;
  }

  public int decimalToMicroDegree(double decimalDegrees) {
    return (int) (MEGA*decimalDegrees);
  }

  /**
   * Converts the coordinates from {@code location} to a GeocachingPoint object.
   * The precision of the GeocachingObject is three decimal places for the
   * latitude and longitude minute values. All values are positive.
   * @param location Location object to convert.
   * @return A GeocachingPoint object.
   */
  public GeocachingPoint locationToGeocaching(Location location) {
    final GeocachingPoint point = new GeocachingPoint();
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();

    getOrientation(point, latitude, longitude);
    getPositiveCoordinates(point, DECIMAL_PLACE, latitude, longitude);

    return point;
  }

  public GeoPoint geocachingToGeoPoint(GeocachingPoint point) {
    double[] geoDecimal = getGeoDecimal(point);

    return new GeoPoint(decimalToMicroDegree(geoDecimal[0]),
        decimalToMicroDegree(geoDecimal[1]));
  }

  /**
   * Converts the coordinates from {@code point} to a GeocachingPoint object.
   * The precision of the GeocachingObject is three decimal places for the
   * latitude and longitude minute values. All values are positive.
   * @param geoPoint GeoPoint object to convert.
   * @return A GeocachingPoint object.
   */
  public GeocachingPoint geoPointToGeocaching(GeoPoint geoPoint) {
    final GeocachingPoint point = new GeocachingPoint();
    double latitude = microToDecimalDegree(geoPoint.getLatitudeE6());
    double longitude = microToDecimalDegree(geoPoint.getLongitudeE6());

    getOrientation(point, latitude, longitude);
    getPositiveCoordinates(point, DECIMAL_PLACE, latitude, longitude);

    return point;
  }

  /**
   * Converts the coordinates from {@code latitude} and {@code longitude} to
   * a GeocachingPoint object. The parameters must be in decimal degree format.
   * Otherwise a weird result might be return.
   * The precision of the GeocachingObject is three decimal places for the
   * latitude and longitude minute values. All values are positive.
   * @param latitude Latitude value of the coordinate.
   * @param longitude Longitude value of the coordinate.
   * @return A GeocachingPoint object.
   */
  public GeocachingPoint decimalDegreesToGeocaching(double latitude, double longitude) {
    final GeocachingPoint point = new GeocachingPoint();
    getPositiveCoordinates(point, DECIMAL_PLACE, latitude, longitude);
    getOrientation(point, latitude, longitude);
    return point;
  }

  private void getOrientation(GeocachingPoint point, double latitude, double longitude) {
    /* positive latitude is north, negative south orientation */
    if(latitude < 0)
      point.setLatOrientation(Orientation.SOUTH);
    else point.setLatOrientation(Orientation.NORTH);
    /* positive longitude is east, negative west orientation */
    if(longitude < 0)
      point.setLonOrientation(Orientation.WEST);
    else point.setLonOrientation(Orientation.EAST);
  }

  private void getPositiveCoordinates(GeocachingPoint point, int decimalPlace,
                                      double latitude, double longitude) {
    point.setLatDegrees((int) Math.abs(latitude));
    point.setLatMinutes(Math.abs(roundDecimalPlaces(
        (latitude - (int) latitude) * 60.0, decimalPlace)));
    point.setLonDegrees((int) Math.abs(longitude));
    point.setLonMinutes(Math.abs(roundDecimalPlaces(
        (longitude - (int) longitude) * 60.0, decimalPlace)));
  }

  /**
   * Returns the geo decimal representation of the latitude and
   * longitude values as an array.<br/>
   * North latitudes are positive and south latitudes are negative values.
   * East longitudes are positive and west longitudes are negative values.
   * The first index is the latitude, the second the longitude value.
   * @param point GeocachingPoint to convert.
   * @return An array with the coordinate values.
   */
  private double[] getGeoDecimal(GeocachingPoint point) {
    double latitude = point.getLatDegrees()+(point.getLatMinutes()/60.0);
    double longitude = point.getLonDegrees()+(point.getLonMinutes()/60.0);
    if(!point.getLatOrientation().equals(Orientation.NORTH))
      latitude = -latitude;
    if(!point.getLonOrientation().equals(Orientation.EAST))
      longitude = -longitude;

    return new double[]{latitude, longitude};
  }

  /* Rounds a number to a specified decimal place. The maximum precision is */
  /* limited to 10 decimal places. */
  private double roundDecimalPlaces(double number, int decimalPlace) {
    if(decimalPlace < 0)
      decimalPlace = 0;
    else if(decimalPlace > 10)
      decimalPlace = 10;
    double factor = Math.pow(10,decimalPlace);
    return Math.round(number*factor)/factor;
  }

  /* Getter and Setter */
}
