package org.screenz.views.ruler;

import java.util.UUID;

import org.screenz.R;
import org.screenz.ScreenzApp;
import org.screenz.data.PixelsPerInch;
import org.screenz.data.Size;
import org.screenz.views.grid.PositionUpdater.OnPositionUpdatedListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

public abstract class RulerView extends View implements OnPositionUpdatedListener {
    @SuppressWarnings("unused")
    private static final String TAG = RulerView.class.getSimpleName();
    
    // These two should add up to 1.00f
    protected static final float OFFSET_PERCENT_MAJORITY = 0.80f;
    protected static final float OFFSET_PERCENT_MINORITY = 0.20f;
    
    protected static final String ORIENTATION_ATTR = "orientation";
    
    protected float inch_marker_pixels;
    protected float half_inch_marker_pixels;
    
    protected PixelsPerInch ppi = PixelsPerInch.Factory.Empty;
    protected ShapeDrawable rulerSizeDrawable;
    protected Context context;
    protected PointF currentTouchPoint;
    protected Paint currentPositionPaint = new Paint();
    protected Size size;
    protected int currentPositionColor = 0xc7ff7777;

    private Displayer displayer;
    private UUID positionListenerKey;
    
    abstract protected Displayer onProvideDisplayer();
    abstract protected void recalculateOffsets();
    abstract protected void onDrawCurrentPosition(Canvas canvas);
    
    public RulerView(Context context) {
        super(context);
        initialize(context);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    
    private void initialize(Context context) {
        this.context = context;
        currentPositionColor = context.getResources().getColor(R.color.currentPositionColor);
        currentPositionPaint.setColor(currentPositionColor);
        currentPositionPaint.setStrokeWidth(ImperialDisplay.INCH_STROKE_WIDTH);
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        displayer = onProvideDisplayer();
        ppi = ScreenzApp.getInstance().getPixelsPerInch();
        
        positionListenerKey = ScreenzApp.PositionUpdater.addListener(this);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
        ScreenzApp.PositionUpdater.removeListener(positionListenerKey);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int measureWidth(int measureSpec) {
        return MeasureSpec.getSize(measureSpec);
    }
    
    private int measureHeight(int measureSpec) {
        return MeasureSpec.getSize(measureSpec);
    }
    
    protected float toDpUnits(float value) {
        return ScreenzApp.getInstance().toDpUnits(value);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (shouldCalculateRulerSize())
            calculateRulerSize();
        
        if (displayer != null)
            displayer.display(canvas);
        
        if (currentTouchPoint != null)
            onDrawCurrentPosition(canvas);
    }
    
    private boolean shouldCalculateRulerSize() {
        return size == null;
    }
    
    private void calculateRulerSize() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ruler_size_metric);
        GradientDrawable fivemm = drawable instanceof GradientDrawable ? (GradientDrawable) drawable : null;
        if (fivemm == null)
            return;
        
        int w = fivemm.getIntrinsicWidth();
        int h = fivemm.getIntrinsicHeight();
        Size bmSize = new Size (w, h);
        if (bmSize.isNotEmpty()) {
            size = bmSize;
            inch_marker_pixels = size.getWidth();
            half_inch_marker_pixels = inch_marker_pixels * OFFSET_PERCENT_MAJORITY;
            recalculateOffsets();
        }
    }
    
    @Override
    public void onPositionUpdated(PointF currentPosition) {
        currentTouchPoint = currentPosition;
        invalidate();
    }
}
