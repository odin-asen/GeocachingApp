package gcd.simplecache.dto;

/**
 * This data transfer object can be used to store information about a geo cache.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class DTOGeocache {
  /* Identification information */
  public String mID;
  public String mOwner;

  /* Cache position */
  public Double mLatitude;
  public Double mLongitude;

  /* Cache attributes */
  public CacheType mType;
  public Float mDifficulty;
  public Float mTerrain;
  public Float mSize;

  public String mDescription;

  /* Constructors */
  public DTOGeocache() {
    this("","",0.0,0.0,CacheType.TRADITIONAL,0.0f,0.0f,0.0f);
  }

  public DTOGeocache(String ID, String owner,
                     Double latitude, Double longitude,
                     CacheType type, Float difficulty, Float terrain, Float size) {
    mID = ID;
    mOwner = owner;
    mLatitude = latitude;
    mLongitude = longitude;
    mType = type;
    mDifficulty = difficulty;
    mTerrain = terrain;
    mSize = size;
    mDescription = "";
  }

  /* Methods */
  /* Getter and Setter */
}
