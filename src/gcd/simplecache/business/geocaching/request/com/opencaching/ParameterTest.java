package gcd.simplecache.business.geocaching.request.com.opencaching;

import gcd.simplecache.business.geocaching.request.ComOpencachingParameter;

/**
 * Make test output for the opencaching.com parameters.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class ParameterTest {
  public static void main(String[] args) {
    format(new Awesome(0.25f, 3.0f));
    format(new BBox(true, 38.642f, -94.754f, 39.643f, -90f));
    format(new BBox(false, 38.642f, -94.754f, 39.643f, -90f));
    format(new CacheID("OXZZZYX"));
    format(new CacheID("OXZZZYZ","OXZZZYY","OXZZZYX"));
    format(new Center(38.642f, -94.754f));
    format(new Description(Description.HTML));
    format(new Description(Description.USER));
    format(new Description(Description.NONE));
    format(new Difficulty(0.25f, 3.0f));
    format(new Found(true));
    format(new Found(false));
    format(new Hint(true));
    format(new Hint(false));
    format(new Limit(4234));
    format(new Log(5, true));
    format(new Log(10, false));
    format(new Name("hide and seek"));
    format(new Size(0.25f, 3.0f));
    format(new Status(Status.REVIEW));
    format(new Status(Status.ACTIVE, Status.ARCHIVED));
    format(new Terrain(0.25f, 3.0f));
    format(new Type(Type.MULTI));
    format(new Type(Type.TRADITIONAL, Type.RIDDLE, Type.VIRTUAL));
  }

  private static void format(ComOpencachingParameter parameter) {
    System.out.println(parameter.formatString());
  }
}
