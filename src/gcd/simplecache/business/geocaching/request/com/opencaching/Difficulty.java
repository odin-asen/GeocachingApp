package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * difficulty=min_difficulty-max_difficulty

 Limits returned geocache to those with a difficulty rating between min_difficulty and max_difficulty inclusive.

 Min and max difficulty are decimal numbers that can range from 1 to 5

 * Represents the min and max difficulty values for a cache in a request.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
public class Difficulty extends ComOpencachingParameter {

  /* Constructors */
  public Difficulty(float minValue, float maxValue) {
    super("difficulty", "");
  }

  /* Methods */
  /* Getter and Setter */

  @Override
  public String formatString() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
