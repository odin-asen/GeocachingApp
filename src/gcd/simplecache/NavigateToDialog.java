package gcd.simplecache;

import gcd.simplecache.business.geocaching.GeocachingPoint;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.dto.geocache.DTOGeocache;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NavigateToDialog extends DialogFragment implements IntentActions, OnCheckedChangeListener {
	
	private View navView; 
	private RadioGroup navGroup;
	private boolean isDegree;
	
	public NavigateToDialog()  {
		
	}
	
		
	 @Override
	  public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 navView = inflater.inflate(R.layout.dialog_navigate_to, null);
		 		 
		 builder.setView(navView);
		 
		 navGroup = (RadioGroup)navView.findViewById(R.id.navGroup);
		 navGroup.setOnCheckedChangeListener(this);
		 setDegreeEnabled();
		 
		 builder.setPositiveButton(R.string.button_ok, new OkListener());
		 builder.setNegativeButton(R.string.button_cancel, new CancelListener());
		 
		 
		 
		 return builder.create();		 
	 }

	

	
	
	private class OkListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
		      Intent navigationAction = new Intent(ACTION_ID_NAVIGATION);
		      navigationAction.putExtra(NAVIGATION_DESTINATION, createDTO());
		      getActivity().sendBroadcast(navigationAction);			
		}
	}
	
	private class CancelListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();			
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedInt) {
		// TODO Auto-generated method stub
		switch (checkedInt) {
			case R.id.radio_dec_deg:
				setDegreeEnabled();
				break;
			case R.id.radio_dec_min:
				setMinuteEnabled();
				break;
		}
	}
	
	private void setDegreeEnabled() {
		EditText editText = (EditText)navView.findViewById(R.id.text_lon_dec_deg);
		editText.setEnabled(true);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_deg);
		editText.setEnabled(true);
		editText = (EditText)navView.findViewById(R.id.text_lon_dec_min_deg);
		editText.setEnabled(false);
		editText = (EditText)navView.findViewById(R.id.text_lon_dec_min_min);
		editText.setEnabled(false);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_deg);
		editText.setEnabled(false);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_min);
		editText.setEnabled(false);
		isDegree = true;
	}
	
	private void setMinuteEnabled() {
		EditText editText = (EditText)navView.findViewById(R.id.text_lon_dec_deg);
		editText.setEnabled(false);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_deg);
		editText.setEnabled(false);
		editText = (EditText)navView.findViewById(R.id.text_lon_dec_min_deg);
		editText.setEnabled(true);
		editText = (EditText)navView.findViewById(R.id.text_lon_dec_min_min);
		editText.setEnabled(true);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_deg);
		editText.setEnabled(true);
		editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_min);
		editText.setEnabled(true);
		isDegree = false;
	}
	
	private DTOGeocache createDTO () {
		DTOGeocache dto = new DTOGeocache();
		if (isDegree) {
			EditText editText = (EditText)navView.findViewById(R.id.text_lon_dec_deg);
			dto.location.longitude = Double.parseDouble(editText.getText().toString());
			editText = (EditText)navView.findViewById(R.id.text_lat_dec_deg);
			dto.location.latitude = Double.parseDouble(editText.getText().toString());
		}
		else {
			GeoCoordinateConverter converter = new GeoCoordinateConverter();
			EditText editText = (EditText)navView.findViewById(R.id.text_lon_dec_deg);
			int degrees = Integer.parseInt(editText.getText().toString());
			editText = (EditText)navView.findViewById(R.id.text_lon_dec_min_min);
			double minutes = Double.parseDouble(editText.getText().toString());
			double longitude = converter.decimalMinutesToDecimalDegrees(degrees, minutes);
			editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_deg);
			degrees = Integer.parseInt(editText.getText().toString());
			editText = (EditText)navView.findViewById(R.id.text_lat_dec_min_min);
			minutes = Double.parseDouble(editText.getText().toString());
			double latitude = converter.decimalMinutesToDecimalDegrees(degrees, minutes);
			dto.location.longitude = longitude;
			dto.location.latitude = latitude;
		}
		return dto;
	}

}


