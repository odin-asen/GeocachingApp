package gcd.simplecache.dto.geocache;

/**
 * Represents a geocache owner.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOCacheOwner {
  public String id;
  public String name;

  public DTOCacheOwner(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "DTOCacheOwner{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
