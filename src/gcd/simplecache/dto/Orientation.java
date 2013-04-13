package gcd.simplecache.dto;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 13.04.13
 */
public enum Orientation {
  NORTH("N"),
  SOUTH("S"),
  EAST("E"),
  WEST("W");

  private String abbr;
  Orientation(String abbreviation) {
    this.abbr = abbreviation;
  }
}
