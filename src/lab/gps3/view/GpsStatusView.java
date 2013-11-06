package lab.gps3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.GpsSatellite;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GpsStatusView extends View {

    public final float maxRadius = 200;
    private Paint paint = new Paint();
    private Paint bigGreenFont = new Paint();
    public static List<GpsSatellite> satellites = new ArrayList<GpsSatellite>();

    public GpsStatusView(Context context) {
        super(context);
        bigGreenFont.setColor(Color.GREEN);
        bigGreenFont.setTextSize(16);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // draw vertical
        paint.setColor(Color.RED);
        canvas.drawLine(cx, 0, cx, cy * 2, paint);
        // draw horizontal
        canvas.drawLine(0, cy, cx * 2, cy, paint);
        paint.setColor(Color.YELLOW);

        drawRadar(canvas);

        paint.setColor(Color.CYAN);

        paint.setTextSize(paint.getTextSize() + 5);
        for (GpsSatellite satellite : satellites) {
            canvas.drawCircle(cx + (float) xPosition(satellite), cy + (float) yPosition(satellite), 2, paint);
            canvas.drawText("/" + String.valueOf((int) satellite.getPrn()), cx + (float) xPosition(satellite), cy + (float) yPosition(satellite) - 3, paint);
//            canvas.drawText("/E:" + String.valueOf((int) satellite.getElevation()) + "/A:" + String.valueOf((int) satellite.getAzimuth()), cx + (float) xPosition(satellite), cy + (float) yPosition(satellite) - 3, paint);
        }
        drawStatus(canvas);
        paint.setTextSize(paint.getTextSize() - 5);
        paint.setColor(Color.YELLOW);
        this.invalidate();
    }

    public void drawStatus(Canvas canvas) {
//        Paint p = new Paint();
//        p.setColor(Color.GREEN);
        int OFFSET = 25;
        int numberOfSatellites = satellites.size();

        for (int i = 0; i < numberOfSatellites; i++) {
            canvas.drawRect(new Rect(10 + i * OFFSET, 50 - 2 * (int) satellites.get(i).getSnr(), 20 + i * OFFSET, 50), bigGreenFont);
            canvas.drawText(String.valueOf(satellites.get(i).getPrn()), (float) (10 + i * OFFSET), 80, bigGreenFont);
        }
        canvas.drawText("Sat NO: "+String.valueOf(numberOfSatellites), (float) 10, 100, bigGreenFont);
    }

    public void drawRadar(Canvas canvas) {
        double tab[] = {90, 75, 60, 45, 30, 0};
        int textSize = 7;
        paint.setTextSize(paint.getTextSize() + textSize);
        double radius;
        double cos;
        for (int i = 0; i < tab.length; i++) {
            cos = (double) Math.cos(Math.toRadians(tab[i]));
            radius = (maxRadius * cos);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) radius, paint);
            canvas.drawText(String.valueOf(tab[i]), getWidth() / 2, (getHeight() / 2) - (float) radius - 1, paint);
        }
        paint.setTextSize(paint.getTextSize() - textSize);
    }

    public double xPosition(GpsSatellite satellite) {
        double radius = maxRadius * Math.cos(Math.toRadians(satellite.getElevation()));
        double x = radius * Math.sin(Math.toRadians(satellite.getAzimuth()));
        return x;
    }

    public double yPosition(GpsSatellite satellite) {
        double radius = maxRadius * Math.cos(Math.toRadians(satellite.getElevation()));
        double y = radius * Math.cos(Math.toRadians(satellite.getAzimuth()));
        return y;
    }
}

