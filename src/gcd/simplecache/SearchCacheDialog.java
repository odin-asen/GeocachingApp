package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 22.04.13
 */
public class SearchCacheDialog extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.dialog_search_cache);

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
      // FIRE ZE MISSILES!
    }
  }

  private class CancelListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      // FIRE ZE MISSILES!
    }
  }

  private class DescriptionListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int id) {
      // FIRE ZE MISSILES!
    }
  }
}
