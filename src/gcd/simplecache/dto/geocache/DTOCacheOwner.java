package gcd.simplecache.dto.geocache;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOCacheOwner {
  public String id;
  public String name;
  /* Constructors */

  public DTOCacheOwner(String id, String name) {
    this.id = id;
    this.name = name;
  }
  /* Methods */
  /* Getter and Setter */

  @Override
  public String toString() {
    return "DTOCacheOwner{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
