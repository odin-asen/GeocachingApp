package gcd.simplecache.dto.geocache;

import java.io.Serializable;

/**
 * Represents a geocache owner.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOCacheOwner implements Serializable {
  private static final String ATTRIBUTE_SEPARATOR = " - ";

  public String id;
  public String name;

  public DTOCacheOwner(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    if(name == null && id == null)
      return "";
    else if(name == null)
      return id;
    else if(id == null)
      return name;
    else return name + ATTRIBUTE_SEPARATOR + id;
  }
}
