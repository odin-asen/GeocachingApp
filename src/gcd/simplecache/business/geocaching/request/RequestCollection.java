package gcd.simplecache.business.geocaching.request;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this class contains the parameters for a request to a geocaching
 * service. It formats the request parameters to an appropriate format.
 * Parameters can be sub classes of
 * {@link gcd.simplecache.business.geocaching.request.Parameter}.
 * <p/>
 * An example for a string might look like this:<br/>
 * {@code ?name="Hide and seek"&description=none&bbox=45.23,-21.56,44.65,25.2"}
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 * @param <T> Class type of what the sub class of {@code RequestCollection} depends on.
 */
abstract public class RequestCollection<T extends Parameter> {
  protected List<T> mParameterList;

  public RequestCollection() {
    mParameterList = new ArrayList<T>();
  }

  abstract public String getRequestParameter();
  abstract public void addParameter(T parameter);
}
