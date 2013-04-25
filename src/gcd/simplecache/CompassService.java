package gcd.simplecache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class CompassService extends Service implements SensorEventListener, IntentActions {
	
	private final Context mContext;
	private static SensorManager sensorService;
	private Sensor sensor;
	
	public CompassService(Context context) {
		this.mContext = context;
		registerSensor();
	}
	
	public void registerSensor() {
		sensorService = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (sensor!= null) {
			sensorService.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			Log.d("Sensor", "registered");
		}
		
		
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	    float azimuth = event.values[0];
		Intent intent = new Intent(ACTION_ID_COMPASS);
		intent.putExtra("azimuth", azimuth);
		mContext.sendBroadcast(intent);	
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
