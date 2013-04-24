package gcd.simplecache.business.geocaching;

/**
 * This enumeration represents a geocache type.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public enum CacheType {
  DEFAULT(""),
  MULTI("multi"),
  RIDDLE("unknown"),
  TRADITIONAL("traditional"),
  VIRTUAL("virtual");

  private String description;

  CacheType(String description) {
    this.description = description;
  }

  /**
   * Parse a string and return the enumeration object
   * Which is contained in that string. The parsing
   * is case insensitive. If no enumeration matches,
   * the default object will be returned.
   */
  static CacheType parseType(String description) {
    if(description == null)
      return DEFAULT;

    final CacheType[] values = CacheType.values();

    description = description.toLowerCase();
    for (int index = 1; index < values.length; index++) {
      final CacheType type = values[index];
      if(description.contains(type.description))
        return type;
    }

    return DEFAULT;
  }

  public String toString() {
    return description;
  }
}
