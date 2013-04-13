package gcd.simplecache.business.map;

import android.location.Location;
import gcd.simplecache.dto.GeocachingPoint;
import org.osmdroid.util.GeoPoint;

/**
 * This class converts some representations of geo coordinates to others.
 * E.g. Micro degree coordinates to decimal degree coordinates.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 13.04.13
 */
public class GeoCoordinateConverter {
  /* Constructors */
  /* Methods */
  public double microToDecimalDegree(int microDegrees) {
    return 0.0;
  }

  public int decimalToMicroDegree(double decimalDegrees) {
    return 0;
  }

  public GeocachingPoint locationToGeocaching(Location location) {
    return null;
  }

  public GeoPoint geocachingToGeoPoint(GeocachingPoint point) {
    return null;
  }

  public GeocachingPoint geoPointToGeocaching(GeoPoint point) {
    return null;
  }

  /* Getter and Setter */
}
