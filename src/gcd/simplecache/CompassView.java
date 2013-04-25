package gcd.simplecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

  private Paint paint;
  private Paint write;
  
  private float position;
  private double cur_lon;
  private double cur_lat;
  private double dest_lon;
  private double dest_lat;
  private double dist;
  private int angleToLocation;
  
  private String n = "N";
  private String e = "E";
  private String w = "W";
  private String s = "S";
  private String longitude = "Longitude:";
  private String latitude = "Latitude";
  private String current = "Current Location:";
  private String destination = "Destination Location:";
  private String distance = "Distance:";
  
  private Bitmap arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);

  
  
  public CompassView(Context context) {
    super(context);
    init();
  }
  
  public CompassView(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  init();
  }
  


  private void init() {
	setBackgroundColor(0xFFFFBB33);
	paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2);
    paint.setTextSize(25);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.BLACK);
    write = new Paint();
    write.setAntiAlias(true);
    write.setStrokeWidth(2);
    write.setTextSize(20);
    write.setStyle(Paint.Style.STROKE);
    write.setColor(Color.BLACK);
  }

  @Override
  protected void onDraw(Canvas canvas) {
	  
	//Values for the Compass
    int compassCenterX = getMeasuredWidth() / 2;
    int compassCenterY = getMeasuredHeight() / 4;

    float radius = (float) (Math.max(compassCenterX, compassCenterY)* 0.5);
    
    //Measurements for the Compass-characters
    Rect wBounds = new Rect();
    paint.getTextBounds(w, 0, 1, wBounds);
    Rect nBounds = new Rect();
    paint.getTextBounds(n, 0, 1, nBounds);
    
    int centerNS = nBounds.width() / 2;
    int topN = nBounds.width() / 2;
    int centerWE = wBounds.height() / 2;
    int leftW = wBounds.width();
    
    
    //Compass-drawing
    canvas.drawCircle(compassCenterX, compassCenterY, radius, paint);
    canvas.drawText(n, compassCenterX-centerNS, compassCenterY-radius-topN, paint);
    canvas.drawText(s, compassCenterX-centerNS, compassCenterY+radius+wBounds.height()+centerWE, paint);
    canvas.drawText(e, compassCenterX+radius+centerNS, compassCenterY+centerWE, paint);
    canvas.drawText(w, compassCenterX-radius-leftW-centerNS, compassCenterY+centerWE, paint);
    canvas.drawLine(compassCenterX, compassCenterY,
        (float) (compassCenterX + radius * Math.sin((double) (-position) / 180 * 3.143)),
        (float) (compassCenterY - radius * Math.cos((double) (-position) / 180 * 3.143)), paint);
    
    //Current Location Drawing
    Rect lonBounds = new Rect();
    paint.getTextBounds(longitude, 0, 10, lonBounds);
    
    int lonHeight = lonBounds.height();
    int lonWidth = lonBounds.width();
    
    float locationX = compassCenterX+radius+10;
    
    canvas.drawText(current, 10, locationX, write);
    canvas.drawText(longitude, 10, locationX+lonHeight, write);
    canvas.drawText(String.valueOf(cur_lon), lonWidth+14, locationX+lonBounds.height(), write);
    canvas.drawText(latitude, 10, locationX+(lonHeight)*2, write);
    canvas.drawText(String.valueOf(cur_lat), lonWidth+14, locationX+(lonHeight)*2, write);
    
    //Destination Location Drawing
    canvas.drawText(destination, compassCenterX, locationX, write);
    canvas.drawText(longitude, compassCenterX, locationX+lonHeight, write);
    canvas.drawText(String.valueOf(dest_lon), lonBounds.width()+compassCenterX, locationX+lonHeight, write);
    canvas.drawText(latitude, compassCenterX, locationX+(lonHeight)*2, write);
    canvas.drawText(String.valueOf(dest_lat), lonBounds.width()+compassCenterX, locationX+(lonHeight)*2, write);   
    
    //Navigation Drawing
    float imageCenterY = compassCenterY+radius*3-20;
    float imageCenterX = compassCenterX;
    Bitmap b = getResizedBitmap(arrow,100,100);

    float imageX = imageCenterX-(b.getWidth()/2);
    float imageY = imageCenterY-(b.getHeight()/2);
    
    
    canvas.save();
    canvas.rotate(angleToLocation, imageCenterX, imageCenterY);
    canvas.drawBitmap(b, imageX, imageY, write);
    canvas.restore();
    canvas.drawText(distance, imageCenterX-b.getWidth(), imageCenterY+b.getHeight(), write);
    canvas.drawText(String.valueOf(dist), imageCenterX-b.getWidth(), imageCenterY+b.getHeight()+nBounds.height(), write);
  }

  public void updateCompass(float position) {
    this.position = position;
    invalidate();
  }
  
  public void updateCurrentLoc(Location location) {
	cur_lat = location.getLatitude();
	cur_lon = location.getLongitude();
	invalidate();
  }
  
  public void updateDestLoc(Location location) {
	dest_lat = location.getLatitude();
	dest_lon = location.getLongitude();
	Location current = new Location("Cur");
	current.setLongitude(cur_lon);
	current.setLatitude(cur_lat);
	dist = location.distanceTo(current);
	invalidate();
  }
  
  public void updateNavigation(Location location) {
	Location dest = new Location("dest");
	dest.setLongitude(dest_lon);
	dest.setLatitude(dest_lat);
	angleToLocation = (int)location.bearingTo(dest);
	invalidate(); 
  }
  
  public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	  
	  int width = bm.getWidth();	   
	  int height = bm.getHeight();	   
	  
	  float scaleWidth = ((float) newWidth) / width;	   
	  float scaleHeight = ((float) newHeight) / height;
 
	  Matrix matrix = new Matrix();	   
	  matrix.postScale(scaleWidth, scaleHeight);

	  Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	  
	  return resizedBitmap;
	   
	  }
  
  
}
