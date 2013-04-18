package gcd.simplecache.dto.geocache;

/**
 * This data transfer object can be used to store information about a geo cache.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class DTOGeocache {
  /* Identification information */
  public String id;
  public DTOCacheOwner owner;
  public String name;

  /* Cache position */
  public DTOLocation location;

  /* Cache attributes */
  public String type;
  public Float difficulty;
  public Float terrain;
  public Float size;

  public String hint;
  public String description;

  /* Constructors */
  public DTOGeocache() {
    this("",new DTOCacheOwner("",""),"",new DTOLocation(0.0,0.0),"none",0.0f,0.0f,0.0f);
  }

  public DTOGeocache(String id, DTOCacheOwner owner, String name, DTOLocation location,
                     String type, Float difficulty, Float terrain, Float size) {
    this.id = id;
    this.owner = owner;
    this.name = name;
    this.location = location;
    this.type = type;
    this.difficulty = difficulty;
    this.terrain = terrain;
    this.size = size;
    this.hint = "";
    this.description = "";
  }

  /* Methods */

  @Override
  public String toString() {
    return "DTOGeocache{" +
        "id='" + id + '\'' +
        ", owner=" + owner +
        ", name='" + name + '\'' +
        ", location=" + location +
        ", type='" + type + '\'' +
        ", difficulty=" + difficulty +
        ", terrain=" + terrain +
        ", size=" + size +
        ", hint='" + hint + '\'' +
        ", description='" + description + '\'' +
        '}';
  }

  /* Getter and Setter */
}
