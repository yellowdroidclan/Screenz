package org.screenz.views.ruler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class ImperialDisplay implements Displayer {
    public static final float INCH_STROKE_WIDTH = 5;
    public static final float HALF_INCH_STROKE_WIDTH = INCH_STROKE_WIDTH / 2f;
    
    protected final Paint inchPaint = new Paint();
    protected final Paint halfInchPaint = new Paint();
    
    abstract protected void drawInchMarkers(Canvas canvas);
    abstract protected void drawHalfInchMarkers(Canvas canvas);

    public ImperialDisplay() {
        initializePaint();
    }
    
    private void initializePaint() {
        inchPaint.setColor(0xc7000055);
        inchPaint.setStrokeWidth(INCH_STROKE_WIDTH);
        halfInchPaint.setColor(Color.DKGRAY);
        halfInchPaint.setStrokeWidth(HALF_INCH_STROKE_WIDTH);
    }
    
    @Override
    public void display(Canvas canvas) {
        drawHalfInchMarkers(canvas);
        drawInchMarkers(canvas);
    }
}
