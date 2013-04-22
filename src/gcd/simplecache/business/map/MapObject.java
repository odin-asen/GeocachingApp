package gcd.simplecache.business.map;

import gcd.simplecache.business.geocaching.Geocache;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * User: Timm Herrmann
 * Date: 13.04.13
 * Time: 21:12
 */
public class MapObject extends OverlayItem {
  private ObjectType mType;
  private Geocache mGeocache;

  /* Constructors */
  public MapObject(String title, String description, GeoPoint geoPoint) {
    super(title, description, geoPoint);
    mType = ObjectType.TRADITIONAL;
    mGeocache = null;
  }

  /* Methods */

  /* Getter and Setter */
  public void setType(ObjectType type) {
    this.mType = type;
  }

  public ObjectType getType() {
    return mType;
  }

  public Geocache getGeocache() {
    return mGeocache;
  }

  public void setGeocache(Geocache geocache) {
    mGeocache = geocache;
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
