package gcd.simplecache.business.geocaching.request;

import android.text.TextUtils;

/**
 * Sub class of RequestCollection to create a request parameter string for
 * <a href="http://www.opencaching.com">opencaching.com</a>.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
public class ComOpencachingRequestCollection
    extends RequestCollection<ComOpencachingParameter> {
  private static final String COM_AND = "&";

  /* Constructors */
  /* Methods */

  @Override
  public String getRequestParameter() {
    String requestParameters = "";
    for (ComOpencachingParameter parameter : mParameterList) {
      if(!TextUtils.isEmpty(requestParameters))
        requestParameters = requestParameters + COM_AND;
      requestParameters = requestParameters + parameter.formatString();
    }
    return requestParameters;
  }

  @Override
  public void addParameter(ComOpencachingParameter parameter) {
    mParameterList.add(parameter);
  }
}
