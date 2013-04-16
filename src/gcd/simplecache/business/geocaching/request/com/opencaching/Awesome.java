package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.Parameter;

/**
 * Limits returned geocache to those with a awesomeness rating between
 * a minimum and a maximum value inclusive.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Awesome extends Parameter {
  private static final String NAME = "awesome";
  private static final String SEPARATOR = "-";
  private static final String EQUALS = "=";
  private Float mMin;
  private Float mMax;

  /* Constructors */
  /**
   * Creates the awesome parameter. {@code min} should be smaller than or equal to {@code max}.
   * The domain of these values are decimal numbers from 1 to 5.
   * @param min Minimum value of the range.
   * @param max Maximum value of the range
   */
  public Awesome(float min, float max) {
    super(NAME, "");
    setValues(min, max);
  }

  /* Methods */
  private void setValues(float min, float max) {
    if(min < 0.0f)
      mMin = 0.0f;
    else mMin = min;
    if(max > 5.0f)
      mMax = 5.0f;
    else mMax = max;
  }

  @Override
  public String formatString() {
    return name+ EQUALS +mMin.toString()+ SEPARATOR +mMax.toString();
  }
}
