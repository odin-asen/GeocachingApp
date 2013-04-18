package gcd.simplecache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

  private Paint paint;
  private float position = 0;
  private double longitude = 43.5;
  private double latitude = 18.3;
  
  public CompassView(Context context) {
    super(context);
    init();
  }
  
  public CompassView(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  init();
  }
  
  public CompassView(Context context, AttributeSet attrs, int defStyle) {
	  super( context, attrs, defStyle );
	  init();
  }

  private void init() {
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2);
    paint.setTextSize(25);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.BLACK);  
//    GPSTracker gps = new GPSTracker(getContext());
//    longitude = gps.getLongitude();
//    latitude = gps.getLatitude();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int xPoint = getMeasuredWidth() / 2;
    int yPoint = getMeasuredHeight() / 2;

    float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
    canvas.drawCircle(xPoint, yPoint, radius, paint);
    canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

    canvas.drawLine(xPoint,
        yPoint,
        (float) (xPoint + radius
            * Math.sin((double) (-position) / 180 * 3.143)),
        (float) (yPoint - radius
            * Math.cos((double) (-position) / 180 * 3.143)), paint);

    canvas.drawText(String.valueOf(position), xPoint, yPoint, paint);  
    canvas.drawText(String.valueOf(longitude), 50, 50, paint);
    canvas.drawText(String.valueOf(latitude), 100, 100, paint);
  }

  public void updateData(float position) {
    this.position = position;
    invalidate();
  }
  
  public void setLongitude(Double longitude) {
	this.longitude = longitude;
	invalidate();
  }
  
  public void setLatitude(Double latitude) {
	this.latitude = latitude;
	invalidate();
  }

} 
