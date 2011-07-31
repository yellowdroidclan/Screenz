package org.screenz.views.grid;

import java.util.UUID;

import org.screenz.R;
import org.screenz.ScreenzApp;
import org.screenz.data.PixelsPerInch;
import org.screenz.data.Size;
import org.screenz.views.grid.PositionUpdater.OnPositionUpdatedListener;
import org.screenz.views.metrics.MetricsView;
import org.screenz.views.ruler.Displayer;
import org.screenz.views.ruler.ImperialDisplay;

import com.codeswimmer.android.device.screen.DeviceScreen;
import com.codeswimmer.android.device.screen.ScreenConfiguration;
import com.codeswimmer.android.device.screen.ScreenDensity;
import com.codeswimmer.android.device.screen.ScreenSize;
import com.codeswimmer.common.data.Values;
import com.codeswimmer.common.util.StringUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CenterView extends RelativeLayout {
    @SuppressWarnings("unused")
    private static final String TAG = CenterView.class.getSimpleName();
    protected static final float OFFSET_PERCENT_MAJORITY = 0.80f;
    protected static final float OFFSET_PERCENT_MINORITY = 0.20f;
    
    private PixelsPerInch ppi;
    private GridDisplay gridDisplay;
    private Context context;
    private float inch_marker_pixels;
    private float bottom_inch_offset;
    private float right_inch_offset;
    private Size size;
    private int currentPositionColor = 0xc7ff7777;
    private int versionColor = 0xffbbbbbb;
    private boolean displayTouchPoint;
    
    private MetricsView metricsView;
    
    public CenterView(Context context) {
        super(context);
        initialize(context);
    }
    
    public CenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    
    public CenterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }
    
    private void initialize(Context context) {
        this.context = context;
        currentPositionColor = context.getResources().getColor(R.color.currentPositionColor);
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        ppi = ScreenzApp.getInstance().getPixelsPerInch();
        
        initializeDisplayers();
        updateDisplayValues();
        
        gridDisplay.onAttachedToWindow();
    }
    
    private void updateDisplayValues() {
        updateMetricsValues();
        updateItemDisplayValues(R.id.infoScreenSize, determineScreenSize());
        updateItemDisplayValues(R.id.screenConfig, determineScreenConfiguration());
        updateItemDisplayValues(R.id.infoScreenDensity, determineScreenDensity());
        updateItemDisplayValues(R.id.orientation, determineOrientation());
    }
    
    private void updateMetricsValues() {
        DisplayMetrics metrics = ScreenzApp.getInstance().getDisplayMetrics();
        metricsView = (MetricsView) findViewById(R.id.mainMetricsView);
        if (metricsView != null)
            metricsView.updateDisplay(metrics);
    }
    
    private void updateItemDisplayValues(int textViewResId, String value) {
        if (StringUtils.isBlank(value))
            return;
        
        TextView textView = (TextView) findViewById(textViewResId);
        if (textView != null)
            textView.setText(value);
    }
    
    private void initializeDisplayers() {
        MajorImperialDisplay majorDisplay = new MajorImperialDisplay();
        MinorImperialDisplay minorDisplay = new MinorImperialDisplay();
        gridDisplay = new GridDisplay(majorDisplay, minorDisplay);
    }
    
    private String determineScreenSize() {
        ScreenSize screenSize = DeviceScreen.getDeviceScreenSize(getContext());
        return context.getString(screenSize.getDisplayNameResId());
    }
    
    private String determineScreenDensity() {
        DisplayMetrics dm = ScreenzApp.getInstance().getDisplayMetrics();
        String dpiGroup = determineScreenDensity(dm.densityDpi);
        return dpiGroup;
    }
    
    private String determineScreenDensity(int dpi) {
        ScreenDensity screenDensity = ScreenDensity.fromSize(dpi);
        return screenDensity.name();
    }
    
    private String determineScreenConfiguration() {
        DisplayMetrics dm = ScreenzApp.getInstance().getDisplayMetrics();
        ScreenConfiguration result = ScreenConfiguration.fromSize(dm.widthPixels, dm.heightPixels);
        return context.getString(result.getDisplayNameResId());
    }
    
    private String determineOrientation() {
        DisplayMetrics dm = ScreenzApp.getInstance().getDisplayMetrics();
        double ratio = (double)dm.heightPixels / (double)dm.widthPixels;
        return ratio < 1.0 ? context.getString(R.string.landscape) : context.getString(R.string.portrait);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
        if (gridDisplay != null)
            gridDisplay.onDetachedFromWindow();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (shouldCalculateRulerSize())
            calculateRulerSize();
        
        if (gridDisplay != null)
            gridDisplay.display(canvas);
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
            recalculateOffsets();
        }
    }
    
    protected void recalculateOffsets() {
        calculatOffsetImperial();
    }
    
    protected void calculatOffsetImperial() {
        bottom_inch_offset = inch_marker_pixels * OFFSET_PERCENT_MINORITY;
    }
    
    protected float toDpUnits(float value) {
        return ScreenzApp.getInstance().toDpUnits(value);
    }

    private static final PointF lastTouchPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!displayTouchPoint)
            return true;
        
        PointF currentTouchPoint = new PointF(event.getX(), event.getY());
        
        if (currentTouchPoint.x != lastTouchPoint.x || currentTouchPoint.y != lastTouchPoint.y) {
            lastTouchPoint.x = currentTouchPoint.x;
            lastTouchPoint.y = currentTouchPoint.y;
            ScreenzApp.PositionUpdater.notifyPositionUpdated(currentTouchPoint);
        }
        
        return true;
    }
    
    private class GridDisplay implements Displayer {
        private Displayer majorLines;
        private Displayer minorLines;
        private CurPositionDisplayer curPosition;
        private AppVersionDisplayer appVersion;
        
        public GridDisplay(Displayer majorLines, Displayer minorLines) {
            this.majorLines = majorLines;
            this.minorLines = minorLines;
            curPosition = new CurPositionDisplayer();
            appVersion = new AppVersionDisplayer();
        }
        
        @Override
        public void display(Canvas canvas) {
            minorLines.display(canvas);
            majorLines.display(canvas);
            curPosition.display(canvas);
            appVersion.display(canvas);
        }
        
        public void onAttachedToWindow() {
            curPosition.onAttachedToWindow();
        }
        
        public void onDetachedFromWindow() {
            curPosition.onDetachedFromWindow();
        }
    }
    
    private class MajorImperialDisplay implements Displayer {
        private MajorHorzImperialDisplay majorHorzDisplay = new MajorHorzImperialDisplay();
        private MajorVertImperialDisplay majorVertDisplay = new MajorVertImperialDisplay();
        
        @Override
        public void display(Canvas canvas) {
            majorHorzDisplay.display(canvas);
            majorVertDisplay.display(canvas);
        }
    }
    
    private class MinorImperialDisplay implements Displayer {
        MinorHorzImperialDisplay minorHorzDisplay = new MinorHorzImperialDisplay();
        MinorVertImperialDisplay minorVertDisplay = new MinorVertImperialDisplay();
        
        @Override
        public void display(Canvas canvas) {
            minorHorzDisplay.display(canvas);
            minorVertDisplay.display(canvas);
        }
    }
    
    private interface GridDisplayer extends Displayer {
        void displayHorzLines(Canvas canvas);
        void displayVertLines(Canvas canvas);
    }
    
    private abstract class BaseGridDisplay implements GridDisplayer {
        protected Paint gridPaint;
        
        public BaseGridDisplay() {
            gridPaint = new Paint();
        }
        
        @Override
        public void display(Canvas canvas) {
            displayHorzLines(canvas);
            displayVertLines(canvas);
        }
    }
    
    private abstract class BaseHorzDisplay extends BaseGridDisplay {
        @Override public void displayVertLines(Canvas canvas) {}
    }
    
    private abstract class BaseVertDisplay extends BaseGridDisplay {
        @Override public void displayHorzLines(Canvas canvas) {}
    }

    private class MajorHorzImperialDisplay extends BaseHorzDisplay {
        public MajorHorzImperialDisplay() {
            gridPaint.setColor(0x55000055);
            gridPaint.setStrokeWidth(ImperialDisplay.INCH_STROKE_WIDTH);
        }

        @Override
        public void displayHorzLines(Canvas canvas) {
            float bottom = getBottom();
            float offset = ppi.Y();
            float left = 0;
            float right = getWidth();
            
            for (float y = offset; y < bottom; y += offset)
                canvas.drawLine(left, y, right, y, gridPaint);
        }
    }
    
    private class MinorHorzImperialDisplay extends BaseHorzDisplay {
        public MinorHorzImperialDisplay() {
            super();
            gridPaint.setColor(Color.DKGRAY);
            gridPaint.setStrokeWidth(ImperialDisplay.HALF_INCH_STROKE_WIDTH);
        }
        
        @Override
        public void displayHorzLines(Canvas canvas) {
            float bottom = getBottom();
            float offset = ppi.Y() / 2.0f;
            float left = 0;
            float right = getWidth();
            
            for (float y = offset; y < bottom; y += offset)
                canvas.drawLine(left, y, right, y, gridPaint);
        }
    }
    
    private class MajorVertImperialDisplay extends BaseVertDisplay {
        public MajorVertImperialDisplay() {
            gridPaint.setColor(0x55000055);
            gridPaint.setStrokeWidth(ImperialDisplay.INCH_STROKE_WIDTH);
        }

        @Override
        public void displayVertLines(Canvas canvas) {
            float right = getRight();
            float offset = ppi.X();
            float top = 0;
            float bottom = getBottom() - bottom_inch_offset;
            
            for (float x = offset; x < right; x += offset)
                canvas.drawLine(x, top, x, bottom, gridPaint);
        }
    }
    
    private class MinorVertImperialDisplay extends BaseVertDisplay {
        public MinorVertImperialDisplay() {
            gridPaint.setColor(Color.DKGRAY);
            gridPaint.setStrokeWidth(ImperialDisplay.HALF_INCH_STROKE_WIDTH);
        }

        @Override
        public void displayVertLines(Canvas canvas) {
            float right = getRight();
            float offset = ppi.X() / 2.0f;
            float top = 0;
            float bottom = getBottom() - bottom_inch_offset;
            
            for (float x = offset; x < right; x += offset)
                canvas.drawLine(x, top, x, bottom, gridPaint);
        }
    }
    
    private class CurPositionDisplayer extends BaseGridDisplay implements OnPositionUpdatedListener {
        private Paint currentPositionPaint = new Paint();
        private PointF currentTouchPoint;
        private UUID listenerKey;
        
        public CurPositionDisplayer() {
            currentPositionPaint.setColor(currentPositionColor);
            currentPositionPaint.setStrokeWidth(ImperialDisplay.INCH_STROKE_WIDTH);
        }
        
        public void onAttachedToWindow() {
            listenerKey = ScreenzApp.PositionUpdater.addListener(this);
        }
        
        public void onDetachedFromWindow() {
            ScreenzApp.PositionUpdater.removeListener(listenerKey);
        }
         
        @Override
        public void onPositionUpdated(PointF currentPosition) {
            currentTouchPoint = currentPosition;
        }
        
        @Override
        public void displayHorzLines(Canvas canvas) {
            if (currentTouchPoint == null)
                return;
            
            canvas.drawLine(currentTouchPoint.x, 0, currentTouchPoint.x, getBottom(), currentPositionPaint);
        }

        @Override
        public void displayVertLines(Canvas canvas) {
            if (currentTouchPoint == null)
                return;
            
            canvas.drawLine(0, currentTouchPoint.y, getRight() - right_inch_offset, currentTouchPoint.y, currentPositionPaint);
        }
    }
    
    private class AppVersionDisplayer implements Displayer {
        private Paint versionPaint = new Paint();
        private String version = Values.EMPTY_STRING;
        
        public AppVersionDisplayer() {
            initializePaint();
            initializeVersionString();
        }
        
        private void initializePaint() {
            versionPaint.setColor(versionColor);
            versionPaint.setTextSize(20.0f);
            versionPaint.setTypeface(Typeface.DEFAULT_BOLD);
            versionPaint.setAntiAlias(true);
        }
        
        private void initializeVersionString() {
            String versionNumber = getContext().getString(R.string.app_version);
            version = String.format("Version %s", versionNumber);
        }
        
        @Override
        public void display(Canvas canvas) {
            if (StringUtils.isEmpty(version))
                return;
            
            float x = 5.0f;
            float y = ppi.Y() - 5.0f;
            canvas.drawText(version, x, y, versionPaint);
        }
    }
}
