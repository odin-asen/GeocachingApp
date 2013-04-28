package gcd.simplecache;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    TextView textView = (TextView) getView().findViewById(R.id.cache_name);
    textView.setText(mNavigationInfo.getCurrentCache().getName());
    textView = (TextView) getView().findViewById(R.id.cache_hint);
    textView.setText(mNavigationInfo.getCurrentCache().getHint());
    textView = (TextView) getView().findViewById(R.id.cache_description);
    textView.setText(mNavigationInfo.getCurrentCache().getDescription());
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
