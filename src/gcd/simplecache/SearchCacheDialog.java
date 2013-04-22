package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import gcd.simplecache.business.geocaching.Geocache;

/**
 * A dialog of this class shows information of a geocache and provides to search
 * this cache. If a button is clicked an appropriate intent will be fired.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 22.04.13
 */
public class SearchCacheDialog extends DialogFragment {

  private Geocache geocache;

  /**
   * Creates a search dialog object.
   * @param geocache Display information of this geocache.
   * @throws NullPointerException If {@code geocache} is null.
   */
  public SearchCacheDialog(Geocache geocache) throws NullPointerException {
    super();
    this.geocache = geocache;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    /* Inflate cache information layout and pass null */
    /* for a parent because its going in the dialog layout */
    LayoutInflater inflater = getActivity().getLayoutInflater();
    builder.setView(inflater.inflate(R.layout.dialog_search_cache, null));

    /* Add buttons */
    builder.setPositiveButton(R.string.button_search_cache, new SearchListener());
    builder.setNegativeButton(R.string.button_cancel, new CancelListener());
    builder.setNeutralButton(R.string.button_display_description, new DescriptionListener());

    return builder.create();
  }

  /***********************************/
  /* Listener for the dialog buttons */

  private class SearchListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      id = id+1;
    }
  }

  private class CancelListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      dialog.dismiss();
    }
  }

  private class DescriptionListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      id = id+1;
    }
  }
}
