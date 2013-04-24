package gcd.simplecache.business.geocaching;

import java.io.Serializable;

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
public class GeocachingPoint implements Serializable {
  private static final int LOWER_BOUND = 0;
  private static final int LAT_UPPER_BOUND = 90;
  private static final int LON_UPPER_BOUND = 180;
  private static final double MINUTES_UPPER_BOUND = 60.0;

  private Orientation mLatOrientation;
  private int mLatDegrees;
  private double mLatMinutes;
  private Orientation mLonOrientation;
  private int mLonDegrees;
  private double mLonMinutes;

  /****************/
  /* Constructors */

  /**
   * Creates a GeocachingPoint object with default values.
   */
  public GeocachingPoint() {
    this(Orientation.NORTH, LOWER_BOUND, LOWER_BOUND,
        Orientation.EAST, LOWER_BOUND, 0.0);
  }

  /**
   * Creates a GeocachingPoint object with the specified values.
   * See the setter methods for more information.
   * @param latOrientation {@link #setLatOrientation(gcd.simplecache.business.geocaching.GeocachingPoint.Orientation)}
   * @param latDegrees {@link #setLatDegrees(int)}
   * @param latMinutes {@link #setLatMinutes(double)}
   * @param longOrientation {@link #setLonOrientation(gcd.simplecache.business.geocaching.GeocachingPoint.Orientation)}
   * @param longDegrees {@link #setLonDegrees(int)}
   * @param longMinutes {@link #setLonMinutes(double)}
   */
  public GeocachingPoint(Orientation latOrientation, int latDegrees,
                         double latMinutes, Orientation longOrientation,
                         int longDegrees, double longMinutes) {
    setLatOrientation(latOrientation);
    setLatDegrees(latDegrees);
    setLatMinutes(latMinutes);
    setLonOrientation(longOrientation);
    setLonDegrees(longDegrees);
    setLonMinutes(longMinutes);
  }

  /**
   * Creates a new GeocachingPoint object from the surpassed object.
   * The parameter must not be null. Else a NullPointerException will be
   * thrown.
   */
  public GeocachingPoint(GeocachingPoint point) {
    this(point.getLatOrientation(), point.getLatDegrees(), point.getLatMinutes(),
        point.getLonOrientation(), point.getLonDegrees(), point.getLonMinutes());
  }

  /*     End      */
  /****************/

  /***********/
  /* Methods */
  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

  /** Truncates the checked value to the lower or upper bound if
   * it is outside of the domain.
   */
  private int truncateInt(int toCheck, int lower, int upper) {
    if(toCheck < lower)
      toCheck = lower;
    else if(toCheck > upper)
      toCheck = upper;
    return toCheck;
  }

  private double truncateDouble(double toCheck, double lower, double upper) {
    final double delta = 0.001;
    if((toCheck-lower) <= delta)
      toCheck = lower;
    else if((upper-toCheck) <= delta)
      toCheck = upper;
    return toCheck;
  }

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */

  public Orientation getLatOrientation() {
    return mLatOrientation;
  }

  /**
   * Must be either {@link Orientation#NORTH} or {@link Orientation#SOUTH}.
   * If it is not one of these values the orientation will be set to
   * {@link Orientation#NORTH}.
   * @param latOrientation Orientation for the latitude value.
   */
  public void setLatOrientation(Orientation latOrientation) {
    if(latOrientation.isLatitude())
      this.mLatOrientation = latOrientation;
    else this.mLatOrientation = Orientation.NORTH;
  }

  public int getLatDegrees() {
    return mLatDegrees;
  }

  /**
   * Truncates the surpassed value if it is not in the value domain.
   * {@code latDegrees} must be positive and in the range of
   * [{@value #LOWER_BOUND}..{@value #LAT_UPPER_BOUND}].
   * @param latDegrees Whole degrees for the latitude value.
   */
  public void setLatDegrees(int latDegrees) {
    this.mLatDegrees = truncateInt(latDegrees, LOWER_BOUND, LAT_UPPER_BOUND);
    if(this.mLatDegrees == LAT_UPPER_BOUND)
      this.mLatMinutes = 0.0;
  }

  public double getLatMinutes() {
    return mLatMinutes;
  }

  /**
   * Truncates the surpassed value if it is not in the value domain.
   * {@code latDegrees} must be positive and in the range of
   * [{@value #LOWER_BOUND}..{@value #MINUTES_UPPER_BOUND}].
   * If the degrees value for longitude is {@value #LAT_UPPER_BOUND}
   * this value will always be {@value #LOWER_BOUND}.
   * @param latMinutes Minute part of the latitude value.
   */
  public void setLatMinutes(double latMinutes) {
    if(this.mLatDegrees == LAT_UPPER_BOUND)
      this.mLatMinutes = LOWER_BOUND;
    else
      this.mLatMinutes = truncateDouble(latMinutes, LOWER_BOUND, MINUTES_UPPER_BOUND);
  }

  public Orientation getLonOrientation() {
    return mLonOrientation;
  }

  /**
   * Must be either {@link Orientation#EAST} or {@link Orientation#WEST}.
   * If it is not one of these values the orientation will be set to
   * {@link Orientation#EAST}.
   * @param lonOrientation Orientation for the longitude value.
   */
  public void setLonOrientation(Orientation lonOrientation) {
    if(lonOrientation.isLongitude())
      this.mLonOrientation = lonOrientation;
    else this.mLonOrientation = Orientation.EAST;
  }

  public int getLonDegrees() {
    return mLonDegrees;
  }

  /**
   * Truncates the surpassed value if it is not in the value domain.
   * {@code lonDegrees} must be positive and in the range of
   * [{@value #LOWER_BOUND}..{@value #LON_UPPER_BOUND}].
   * @param lonDegrees Whole degrees for the longitude value.
   */
  public void setLonDegrees(int lonDegrees) {
    this.mLonDegrees = truncateInt(lonDegrees, LOWER_BOUND, LAT_UPPER_BOUND);
    if(this.mLonDegrees == LON_UPPER_BOUND)
      this.mLonMinutes = 0.0;
  }

  public double getLonMinutes() {
    return mLonMinutes;
  }

  /**
   * Truncates the surpassed value if it is not in the value domain.
   * {@code latDegrees} must be positive and in the range of
   * [{@value #LOWER_BOUND}..{@value #MINUTES_UPPER_BOUND}].
   * If the degrees value for longitude is {@value #LON_UPPER_BOUND}
   * this value will always be {@value #LOWER_BOUND}.
   * @param lonMinutes Minute part of the longitude value.
   */
  public void setLonMinutes(double lonMinutes) {
    this.mLonMinutes = lonMinutes;
  }

  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */

  /**
   * This enumeration represents an orientation like used on a compass.
   * <p/>
   * Author: Timm Herrmann<br/>
   * Date: 13.04.13
   */
  public enum Orientation {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W");

    private String abbr;
    Orientation(String abbreviation) {
      this.abbr = abbreviation;
    }

    public String toString() {
      return abbr;
    }

    public boolean isLongitude() {
      return this.equals(EAST) || this.equals(WEST);
    }

    public boolean isLatitude() {
      return this.equals(NORTH) || this.equals(SOUTH);
    }
  }

  /*      End      */
  /*****************/
}
