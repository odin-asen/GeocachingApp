package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.Parameter;

/**
 * Returns a list of geocaches inside or outside the specified bounding box
 * sorted by the distance from the center of the box.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class BBox extends Parameter {
  private static final String EXCLUDE = "exclude_";
  private static final String NAME = "bbox";
  private static final String SEPARATOR = ",";
  private static final String EQUALS = "=";
  private boolean mExclude;

  /* Constructors */
  /**
   * Creates the bbox parameter. The parameters {@code south}, {@code west},
   * {@code north}, {@code east} define the edges of a bounding box.
   * Coordinates should be in decimal degrees (e.g. 21.4578°).
   * Use positive numbers for north latitude and east longitude and negative
   * numbers of south latitude and west longitude.
   * The box cannot cross the 180° longitude line or the 90° or -90° points.
   * @param exclude Request included or excluded geocaches of this box.
   * @param south Latitude value for the south border of the box.
   * @param west Longitude value for the west border of the box.
   * @param north Latitude value for the north border of the box.
   * @param east Longitude value for the east border of the box.
   */
  public BBox(boolean exclude, float south, float west, float north, float east) {
    super(NAME,
        Float.toString(south)+SEPARATOR+Float.toString(west)
        +SEPARATOR+Float.toString(north)+SEPARATOR+Float.toString(east));
    mExclude = exclude;
  }

  /* Methods */

  @Override
  public String formatString() {
    String request = name+ EQUALS + value;
    if(mExclude)
      return EXCLUDE+request;
    else return request;
  }
}
