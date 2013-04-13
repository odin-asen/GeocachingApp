package gcd.simplecache.business.geocaching;

import gcd.simplecache.dto.GeocachingPoint;

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
  private String id;
  /** Position of the geocache. */
  private GeocachingPoint point;
  private String name;
  private String description;
  private float difficulty;
  private float terrain;

  /* Constructors */

  /* Methods */

  /* Getter and Setter */

  public GeocachingPoint getPoint() {
    return point;
  }

  public void setPoint(GeocachingPoint point) {
    this.point = point;
  }

  public float getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(float difficulty) {
    this.difficulty = difficulty;
  }

  public float getTerrain() {
    return terrain;
  }

  public void setTerrain(float terrain) {
    this.terrain = terrain;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
