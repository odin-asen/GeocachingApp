package gcd.simplecache.business.map;

import gcd.simplecache.business.geocaching.GeocachingPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * User: Timm Herrmann
 * Date: 13.04.13
 * Time: 21:12
 */
public class MapObject extends OverlayItem {
  private ObjectType type;
  private GeocachingPoint geocachingPoint;

  /* Constructors */
  public MapObject(String title, String description, GeoPoint geoPoint) {
    super(title, description, geoPoint);
  }

  /* Methods */

  /* Getter and Setter */
  public void setType(ObjectType type) {
    this.type = type;
  }

  public ObjectType getType() {
    return type;
  }

  public GeocachingPoint getGeocachingPoint() {
    return geocachingPoint;
  }

  public void setGeocachingPoint(GeocachingPoint geocachingPoint) {
    this.geocachingPoint = geocachingPoint;
  }

  /* Inner classes */
  public enum ObjectType {
    AIM,
    MULTI,
    RIDDLE,
    TRADITIONAL,
    USER;

    public boolean isGeocache() {
      return !(this.equals(AIM) || this.equals(USER));
    }

    public boolean isUser() {
      return !this.isGeocache() && !this.equals(AIM);
    }
  }
}
