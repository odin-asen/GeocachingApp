package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import gcd.simplecache.R;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 22.04.13
 */
public class SearchCacheDialog extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.dialog_search_cache).setPositiveButton(
        R.string.search_cache, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // FIRE ZE MISSILES!
      }
    })
        .setNegativeButton("He-eh!", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });
    // Create the AlertDialog object and return it
    return builder.create();
  }
}
