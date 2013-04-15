package gcd.simplecache.business.geocaching.request;

/**
 * This class can be inherited by other classes to specify that this parameter
 * defines the syntax for requests to
 * <a href="http://www.opencaching.com">opencaching.com</a>.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
abstract public class ComOpencachingParameter extends Parameter {
  protected ComOpencachingParameter(String name, String value) {
    super(name, value);
  }
}
