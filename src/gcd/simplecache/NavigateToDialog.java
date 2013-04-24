package gcd.simplecache;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NavigateToDialog extends DialogFragment implements IntentActions {
	
	private EditText lonDecDeg;
	private EditText latDecDeg;
	private EditText lonDecMinDeg;
	private EditText lonDecMinMin;
	private EditText latDecMinDeg;
	private EditText latDecMinMin;
	
	private boolean isDecimalDegree;
	
	
	public NavigateToDialog()  {
		
	}
	
		
	 @Override
	  public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 View navView = inflater.inflate(R.layout.dialog_navigate_to, null);
		 
		 builder.setView(navView);
		 
		 
		 builder.setPositiveButton(R.string.button_ok, new OkListener());
		 builder.setNegativeButton(R.string.button_cancel, new CancelListener());

		 return builder.create();		 
	 }

	

	
	
	private class OkListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent("hallo");
			
		}
	}
	
	private class CancelListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();			
		}
	}

}


