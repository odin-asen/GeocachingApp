package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
  private Drawable mTitleIcon;

  /**
   * Creates a search dialog object.
   * @param geocache Display information of this geocache.
   * @param titleIcon Icon to display in the title of the dialog.
   * @throws NullPointerException If {@code geocache} is null.
   */
  public SearchCacheDialog(Geocache geocache, Drawable titleIcon)
      throws NullPointerException {
    super();
    this.mGeocache = geocache;
    this.mTitleIcon = titleIcon;
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
    builder.setNeutralButton(R.string.button_fetch_details, new DescriptionListener());

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
    textView = (TextView) view.findViewById(R.id.cache_name);
    textView.setText(mGeocache.getName());

    /* set title */
    if(mTitleIcon != null) {
      ImageView imageView = (ImageView) view.findViewById(R.id.cache_type);
      imageView.setImageDrawable(mTitleIcon);
      textView = (TextView) view.findViewById(R.id.cache_code);
      textView.setText(mGeocache.getId());
    }
  }

  /***********************************/
  /* Listener for the dialog buttons */

  private class SearchListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      /* enable navigation to search for the cache */
      Intent navigationAction = new Intent(ACTION_ID_NAVIGATION);
      navigationAction.putExtra(NAVIGATION_ENABLED, true);
      navigationAction.putExtra(NAVIGATION_DESTINATION, Geocache.toDTO(mGeocache));
      getActivity().sendBroadcast(navigationAction);
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
      Intent detailsAction = new Intent(ACTION_ID_DETAILS);
      detailsAction.putExtra(DETAILS_ID, mGeocache.getId());
      getActivity().sendBroadcast(detailsAction);
    }
  }

  /*               End               */
  /***********************************/
}
