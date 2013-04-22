package gcd.simplecache.business.geocaching;

import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.dto.geocache.DTOCacheOwner;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;

/**
 * This class represents a geocache.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 14.04.13
 */
public class Geocache {
  /**
   * Unique identifier of this cache for a certain geocaching service.
   */
  private String mId;
  /** Position of the geocache. */
  private GeocachingPoint mPoint;
  private String mName;
  private String mOwner;
  private String mDescription;
  private String mHint;
  private String mType;
  private float mDifficulty;
  private float mTerrain;
  private float mAwesomeness;
  private float mSize;

  /****************/
  /* Constructors */

  public Geocache() {
    setId("");
    setPoint(new GeocachingPoint());
    setName("");
    setOwner("");
    setDescription("");
    setHint("");
    setDifficulty(0.0f);
    setTerrain(0.0f);
    setAwesomeness(0.0f);
    setSize(0.0f);
    setType("");
  }

  /*     End      */
  /****************/

  /***********/
  /* Methods */

  public static Geocache toGeocache(DTOGeocache dto) {
    final Geocache geocache = new Geocache();

    if(dto != null) {
      final GeoCoordinateConverter converter = new GeoCoordinateConverter();
      geocache.setPoint(converter.decimalDegreesToGeocaching(dto.location.latitude, dto.location.longitude));

      geocache.setId(dto.id);
      geocache.setName(dto.name);

      if(dto.owner != null)
        geocache.setOwner(dto.owner.toString());

      geocache.setDescription(dto.description);
      geocache.setHint(dto.hint);
      geocache.setDifficulty(dto.difficulty);
      geocache.setTerrain(dto.terrain);
      geocache.setSize(dto.size);
      geocache.setType(dto.type);
    }

    return geocache;
  }

  public static DTOGeocache toDTO(Geocache geocache) {
    final DTOGeocache dto = new DTOGeocache();

    if(geocache != null) {
      final GeoCoordinateConverter converter = new GeoCoordinateConverter();
      double[] coordinates = converter.getGeoDecimal(geocache.mPoint);
      dto.location = new DTOLocation(coordinates[0], coordinates[1]);

      dto.id = geocache.mId;
      dto.name = geocache.mName;
      dto.owner = DTOCacheOwner.parseString(geocache.mOwner);
      dto.description = geocache.mDescription;
      dto.hint = geocache.mHint;
      dto.difficulty = geocache.mDifficulty;
      dto.terrain = geocache.mTerrain;
      dto.size = geocache.mSize;
      dto.type = geocache.mType;
    }

    return dto;
  }

  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */
  /*       End       */
  /*******************/

  /* If string is null, an empty string will be returned */
  /* Else the string will be returned. */
  private String getNoNull(String string) {
    if(string == null)
      return "";
    else return string;
  }

  /*********************/
  /* Getter and Setter */

  public GeocachingPoint getPoint() {
    return mPoint;
  }

  /**
   * Set the point value by creating a new instance for the surpassed value.
   * If null will be surpassed the default constructor will be called.
   * @param point New geocaching point.
   */
  public void setPoint(GeocachingPoint point) {
    if(point == null)
      this.mPoint = new GeocachingPoint();
    else this.mPoint = new GeocachingPoint(point);
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

  /**
   * Set the cache id. If null will be surpassed, the value will
   * be set to an empty string.
   * @param id Cache id.
   */
  public void setId(String id) {
    this.mId = getNoNull(id);
  }

  public String getName() {
    return mName;
  }

  /**
   * Set the cache name. If null will be surpassed, the value will
   * be set to an empty string.
   * @param name Cache name.
   */
  public void setName(String name) {
    this.mName = getNoNull(name);
  }

  public String getDescription() {
    return mDescription;
  }

  /**
   * Set the cache description. If null will be surpassed, the value will
   * be set to an empty string.
   * @param description Cache description.
   */
  public void setDescription(String description) {
    this.mDescription = getNoNull(description);
  }

  public String getHint() {
    return mHint;
  }

  /**
   * Set the cache hint. If null will be surpassed, the value will
   * be set to an empty string.
   * @param hint Cache hint.
   */
  public void setHint(String hint) {
    this.mHint = getNoNull(hint);
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

  /**
   * Set the cache owner. If null will be surpassed, the value will
   * be set to an empty string.
   * @param owner Cache owner.
   */
  public void setOwner(String owner) {
    this.mOwner = getNoNull(owner);
  }

  public float getSize() {
    return mSize;
  }

  public void setSize(float size) {
    this.mSize = size;
  }

  public String getType() {
    return mType;
  }

  /**
   * Set the cache type. If null will be surpassed, the value will
   * be set to an empty string.
   * @param type Cache type.
   */
  public void setType(String type) {
    mType = getNoNull(type);
  }

  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */
  /*      End      */
  /*****************/
}
