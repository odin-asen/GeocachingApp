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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import gcd.simplecache.business.map.GeoCoordinateConverter;
import gcd.simplecache.dto.geocache.DTOGeocache;

public class NavigateToDialog extends DialogFragment implements IntentActions, OnCheckedChangeListener {
	
	private View navView; 
	private RadioGroup navGroup;
	private boolean isDegree;

  /* Editable Views */
  private EditText mLatDecDeg;
  private EditText mLonDecDeg;
  private EditText mLatDecMinDeg;
  private EditText mLatDecMinMin;
  private EditText mLonDecMinDeg;
  private EditText mLonDecMinMin;

	public NavigateToDialog()  {

	}
	
		
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    navView = inflater.inflate(R.layout.dialog_navigate_to, null);
    initialiseEditVariables();

    builder.setView(navView);

    navGroup = (RadioGroup)navView.findViewById(R.id.navGroup);
    navGroup.setOnCheckedChangeListener(this);
    setDegreeEnabled();

    builder.setPositiveButton(R.string.button_ok, new OkListener());
    builder.setNegativeButton(R.string.button_cancel, new CancelListener());

    return builder.create();
  }

  private void initialiseEditVariables() {
    mLatDecDeg = (EditText) navView.findViewById(R.id.text_lat_dec_deg);
    mLonDecDeg = (EditText) navView.findViewById(R.id.text_lon_dec_deg);
    mLatDecMinDeg = (EditText) navView.findViewById(R.id.text_lat_dec_min_deg);
    mLatDecMinMin = (EditText) navView.findViewById(R.id.text_lat_dec_min_min);
    mLonDecMinDeg = (EditText) navView.findViewById(R.id.text_lon_dec_min_deg);
    mLonDecMinMin = (EditText) navView.findViewById(R.id.text_lon_dec_min_min);
  }

	private class OkListener implements DialogInterface.OnClickListener {

    @Override
		public void onClick(DialogInterface dialog, int which) {
      AlertDialog alert = (AlertDialog) dialog;

      if(inputIsValid()) {
        Intent navigationAction = new Intent(ACTION_ID_NAVIGATION);
        navigationAction.putExtra(NAVIGATION_ENABLED, true);
        navigationAction.putExtra(NAVIGATION_DESTINATION, createDTO());
        getActivity().sendBroadcast(navigationAction);
      } else {
        Toast.makeText(alert.getContext(),
            "Navigation did not change! Please fill out all fields with valid values!",
            Toast.LENGTH_LONG).show();
      }
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
		mLonDecDeg.setEnabled(true);
		mLatDecDeg.setEnabled(true);
		mLonDecMinDeg.setEnabled(false);
		mLonDecMinMin.setEnabled(false);
		mLatDecMinDeg.setEnabled(false);
		mLatDecMinMin.setEnabled(false);
		isDegree = true;
	}
	
	private void setMinuteEnabled() {
    mLonDecDeg.setEnabled(false);
    mLatDecDeg.setEnabled(false);
    mLonDecMinDeg.setEnabled(true);
    mLonDecMinMin.setEnabled(true);
    mLatDecMinDeg.setEnabled(true);
    mLatDecMinMin.setEnabled(true);
		isDegree = false;
	}
	
	private DTOGeocache createDTO () {
		DTOGeocache dto = new DTOGeocache();
		if (isDegree) {
			dto.location.longitude = Double.parseDouble(mLonDecDeg.getText().toString());
			dto.location.latitude = Double.parseDouble(mLatDecDeg.getText().toString());
		}
		else {
			GeoCoordinateConverter converter = new GeoCoordinateConverter();
			int degrees = Integer.parseInt(mLonDecMinDeg.getText().toString());
			double minutes = Double.parseDouble(mLonDecMinMin.getText().toString());
			double longitude = converter.decimalMinutesToDecimalDegrees(degrees, minutes);
			degrees = Integer.parseInt(mLatDecMinDeg.getText().toString());
			minutes = Double.parseDouble(mLatDecMinMin.getText().toString());
			double latitude = converter.decimalMinutesToDecimalDegrees(degrees, minutes);
			dto.location.longitude = longitude;
			dto.location.latitude = latitude;
		}
		return dto;
	}

  /** Check if the text fields have correct values. */
  private boolean inputIsValid() {
    boolean result;

    if(isDegree) {
      result = checkEditText(mLonDecDeg, -180.0, 180.0);
      result = result && checkEditText(mLatDecDeg, -90.0, 90.0);
    } else {
      result = checkEditText(mLonDecMinDeg, -180.0, 180.0);
      result = result && checkEditText(mLonDecMinMin, 0, 60);
      result = result && checkEditText(mLatDecMinDeg, -90, 90);
      result = result && checkEditText(mLatDecMinMin, 0, 60);
    }

    return result;
  }

  private boolean checkEditText(EditText editText, double min, double max) {
    try {
      double value = Double.parseDouble(editText.getText().toString());
      return (value >= min) && (value <= max);
    } catch (NumberFormatException e) {
      return false;
    }
  }
}


