package gcd.simplecache.dto.geocache;

import java.io.Serializable;

/**
 * Represents a geocache owner.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOCacheOwner implements Serializable {
  private static final String ATTRIBUTE_SEPARATOR = "-";

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
    else {
      final String space = " ";
      return name + space + ATTRIBUTE_SEPARATOR + space + id;
    }
  }

  /**
   * Parses a string and creates an owner object. If the string was created
   * with {@link DTOCacheOwner#toString()} and all attributes were not null, the
   * object can be reconstructed.<br/>
   * If this is not the case the owner will have the string as name.
   * @param ownerString String representation for this owner.
   * @return A DTOCacheOwner object.
   */
  public static DTOCacheOwner parseString(String ownerString) {
    final DTOCacheOwner owner = new DTOCacheOwner("", "");

    if(ownerString != null) {
      final String[] strings = ownerString.split(ATTRIBUTE_SEPARATOR);

      if(strings.length >= 2) {
        owner.name = strings[0];
        owner.id = strings[1];
      } else if(strings.length == 1) {
        owner.name = strings[0];
      }
    }

    return owner;
  }
}
