package gcd.simplecache.dto;

/**
 * This class represents a geo coordinate. An object has the variables that are
 * used in a common format for geocaching.
 * <p/>
 * Example: N 47° 46.004', E 8° 49.002'<br/>
 * (47 degree and 46.004 minutes north, 8 degree and 49.002 minutes east)
 * <p/>
 * Author: Timm Herrmann
 * Date: 13.04.2013
 */
public class GeocachingPoint {
  public Orientation latOrientation;
  public int latDegrees;
  public double latMinutes;
  public Orientation longOrientation;
  public int longDegrees;
  public double longMinutes;

  public GeocachingPoint() {
    latOrientation = Orientation.NORTH;
    latDegrees = 0;
    latMinutes = 0.0;
    longOrientation = Orientation.EAST;
    longDegrees = 0;
    longMinutes = 0.0;
  }

  public GeocachingPoint(Orientation latOrientation, int latDegrees,
                         double latMinutes, Orientation longOrientation,
                         int longDegrees, double longMinutes) {
    this.latDegrees = latDegrees;
    this.latMinutes = latMinutes;
    this.latOrientation = latOrientation;
    this.longDegrees = longDegrees;
    this.longMinutes = longMinutes;
    this.longOrientation = longOrientation;
  }
}
