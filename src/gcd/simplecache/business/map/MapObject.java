package gcd.simplecache.business.map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * User: Timm Herrmann
 * Date: 13.04.13
 * Time: 21:12
 */
public class MapObject extends OverlayItem {
  private ObjectType type;

  /* Constructors */
  public MapObject(String aTitle, String aDescription, GeoPoint aGeoPoint) {
    super(aTitle, aDescription, aGeoPoint);
  }

  /* Methods */

  /* Getter and Setter */
  public void setObjectType(ObjectType type) {
    this.type = type;
  }

  public ObjectType getObjectType() {
    return type;
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
  }
}
