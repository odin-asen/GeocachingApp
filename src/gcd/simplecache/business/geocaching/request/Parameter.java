package gcd.simplecache.business.geocaching.request;

/**
 * An object of this class specifies a parameter for a request to a geocaching
 * service. Sub classed instances of this class specify the format of the string
 * that can be added to a request.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
abstract public class Parameter {
  protected String name;
  protected String value;

  /* Constructors */
  protected Parameter(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /* Methods */
  abstract public String formatString();
}
