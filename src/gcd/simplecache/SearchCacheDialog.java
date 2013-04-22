package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import gcd.simplecache.business.geocaching.Geocache;

/**
 * A dialog of this class shows information of a geocache and provides to search
 * this cache. If a button is clicked an appropriate intent will be fired.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 22.04.13
 */
public class SearchCacheDialog extends DialogFragment implements IntentActions {

  private Geocache mGeocache;

  /**
   * Creates a search dialog object.
   * @param geocache Display information of this geocache.
   * @throws NullPointerException If {@code geocache} is null.
   */
  public SearchCacheDialog(Geocache geocache) throws NullPointerException {
    super();
    this.mGeocache = geocache;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    /* Inflate cache information layout and pass null */
    /* for a parent because its going in the dialog layout */
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View cacheView = inflater.inflate(R.layout.dialog_search_cache, null);

    fillViewContent(cacheView);
    builder.setView(cacheView);


    /* Add buttons */
    builder.setPositiveButton(R.string.button_search_cache, new SearchListener());
    builder.setNegativeButton(R.string.button_cancel, new CancelListener());
    builder.setNeutralButton(R.string.button_display_description, new DescriptionListener());

    return builder.create();
  }

  private void fillViewContent(View view) {
    /* set text views */
    TextView textView = (TextView) view.findViewById(R.id.cache_size);
    textView.setText(Float.toString(mGeocache.getSize()));
    textView = (TextView) view.findViewById(R.id.cache_difficulty);
    textView.setText(Float.toString(mGeocache.getDifficulty()));
    textView = (TextView) view.findViewById(R.id.cache_terrain);
    textView.setText(Float.toString(mGeocache.getTerrain()));
    textView = (TextView) view.findViewById(R.id.cache_code);
    textView.setText(mGeocache.getId());
  }

  /***********************************/
  /* Listener for the dialog buttons */

  private class SearchListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      /* enable navigation to search for the cache */
      Intent navigationAction = new Intent(ACTION_ID_NAVIGATION);
      navigationAction.putExtra(NAVIGATION_ENABLED, true);
      navigationAction.putExtra(NAVIGATION_DESTINATION, mGeocache.getPoint());
      getActivity().getApplicationContext().sendBroadcast(navigationAction);
    }
  }

  private class CancelListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      dialog.dismiss();
    }
  }

  private class DescriptionListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      /* show description of the cache */
      Intent navigationAction = new Intent(ACTION_ID_DESCRIPTION);
      navigationAction.putExtra(DESCRIPTION_ID, mGeocache.getId());
      navigationAction.putExtra(DESCRIPTION_NAME, mGeocache.getName());
      navigationAction.putExtra(DESCRIPTION_HINT, mGeocache.getHint());
      navigationAction.putExtra(DESCRIPTION_OWNER, mGeocache.getOwner());
      navigationAction.putExtra(DESCRIPTION_TEXT, mGeocache.getDescription());
      getActivity().getApplicationContext().sendBroadcast(navigationAction);
    }
  }

  /*               End               */
  /***********************************/
}
