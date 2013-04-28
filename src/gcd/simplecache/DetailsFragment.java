package gcd.simplecache;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 25.04.13
 */
public class DetailsFragment extends Fragment {
  private CacheNavigationInfo mNavigationInfo;

  /****************/
  /* Constructors */
  /*     End      */
  /****************/

  /***********/
  /* Methods */

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_details, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();

    fillTextFields();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof CacheNavigationInfo) {
      mNavigationInfo = (CacheNavigationInfo) activity;
    } else {
      throw new ClassCastException(activity.toString()
          + " must implement "+CacheNavigationInfo.class.getName());
    }
  }

  /*   End   */
  /***********/

  /*******************/
  /* Private Methods */

  private void fillTextFields() {
    /* Name field */
    TextView textView = (TextView) getView().findViewById(R.id.cache_name);
    String content = mNavigationInfo.getCurrentCache().getName();
    setTextWithDefault(textView, content, "", false);

    /* Hint field */
    textView = (TextView) getView().findViewById(R.id.cache_hint);
    content = mNavigationInfo.getCurrentCache().getHint();
    setTextWithDefault(textView, content, getString(R.string.empty_filler), false);

    /* Description field */
    textView = (TextView) getView().findViewById(R.id.cache_description);
    content = mNavigationInfo.getCurrentCache().getDescription();
    setTextWithDefault(textView, content, getString(R.string.empty_filler), true);
  }

  /* Sets the content to the text view or def if content is empty */
  private void setTextWithDefault(TextView view, String content, String def, boolean html) {
    if(TextUtils.isEmpty(content)) {
      if(html)
        view.setText(Html.fromHtml(def));
      else view.setText(def);
    } else {
      if(html)
        view.setText(Html.fromHtml(content));
      else view.setText(content);
    }
  }

  /*       End       */
  /*******************/

  /*********************/
  /* Getter and Setter */
  /*        End        */
  /*********************/

  /*****************/
  /* Inner classes */
  /*****************/
}
