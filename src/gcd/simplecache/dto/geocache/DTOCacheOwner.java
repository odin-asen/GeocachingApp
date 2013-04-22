package gcd.simplecache.dto.geocache;

import java.io.Serializable;

/**
 * Represents a geocache owner.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOCacheOwner implements Serializable {
  public String id;
  public String name;

  public DTOCacheOwner(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "{ id=\'" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
