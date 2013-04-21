package gcd.simplecache.business.geocaching;

import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.dto.GeocachingPoint;
import gcd.simplecache.dto.geocache.DTOGeocache;
import org.osmdroid.util.GeoPoint;

/**
 * This class represents a geocache.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 14.04.13
 */
public class Geocache {
  private static final String ATTRIBUTE_SEPARATOR = " - ";
  /**
   * Unique identifier of this cache for a certain geocaching service.
   */
  private String mId;
  /** Position of the geocache. */
  private GeocachingPoint mPoint;
  private String mName;
  private String mOwner;
  private String mDescription;
  private float mDifficulty;
  private float mTerrain;
  private float mAwesomeness;
  private float mSize;

  /* Constructors */

  /* Methods */

  /* Getter and Setter */

  public GeocachingPoint getPoint() {
    return mPoint;
  }

  public void setPoint(GeocachingPoint point) {
    this.mPoint = point;
  }

  public float getDifficulty() {
    return mDifficulty;
  }

  public void setDifficulty(float difficulty) {
    this.mDifficulty = difficulty;
  }

  public float getTerrain() {
    return mTerrain;
  }

  public void setTerrain(float terrain) {
    this.mTerrain = terrain;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    this.mId = id;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    this.mName = name;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    this.mDescription = description;
  }

  public float getAwesomeness() {
    return mAwesomeness;
  }

  public void setAwesomeness(float awesomeness) {
    this.mAwesomeness = awesomeness;
  }

  public String getOwner() {
    return mOwner;
  }

  public void setOwner(String owner) {
    this.mOwner = owner;
  }

  public float getSize() {
    return mSize;
  }

  public void setSize(float size) {
    this.mSize = size;
  }

  public static Geocache toGeocache(DTOGeocache dto) {
    final Geocache geocache = new Geocache();
    final GeoCoordinateConverter converter = new GeoCoordinateConverter();

    geocache.setDescription(dto.description);
    geocache.setDifficulty(dto.difficulty);
    geocache.setId(dto.id);
    geocache.setName(dto.name);
    geocache.setOwner(dto.owner.name + ATTRIBUTE_SEPARATOR +dto.owner.id);

    /* Write own method to convert from lat and long to GeocachingPoint */
    final GeocachingPoint point =
        converter.geoPointToGeocaching(new GeoPoint(dto.location.latitude, dto.location.longitude));
    geocache.setPoint(point);

    geocache.setSize(dto.size);
    geocache.setTerrain(dto.terrain);

    return geocache;
  }
}
