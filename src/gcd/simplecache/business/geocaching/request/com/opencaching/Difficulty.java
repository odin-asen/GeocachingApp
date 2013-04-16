package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
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
