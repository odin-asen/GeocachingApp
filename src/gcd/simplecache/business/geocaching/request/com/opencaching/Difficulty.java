package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Limits returned geocache to those with a difficulty rating between
 * a minimum and a maximum value inclusive.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
public class Difficulty extends ComOpencachingParameter {
  private static final String NAME = "difficulty";
  private static final String SEPARATOR = "-";
  private static final String EQUALS = "=";

  /* Constructors */
  /**
   * Creates the difficulty parameter. {@code min} should be smaller than or
   * equal to {@code max}.
   * The domain of these values are decimal numbers from 1 to 5.
   * @param min Minimum value of the range.
   * @param max Maximum value of the range.
   */
  public Difficulty(float min, float max) {
    super(NAME, Float.toString(min)+SEPARATOR+Float.toString(max));
  }

  /* Methods */
  @Override
  public String formatString() {
    return name+ EQUALS +value;
  }
}