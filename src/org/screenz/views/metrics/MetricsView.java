package org.screenz.views.metrics;

import org.screenz.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MetricsView extends LinearLayout {
    private ViewGroup localRootView;
    private TextView metricsDensity;
    private TextView metricsDensityDpi;
    private TextView metricsScaledDensity;
    private TextView metricsXdpi;
    private TextView metricsYdpi;
    
    public MetricsView(Context context) {
        super(context);
        initialize();
    }
    
    public MetricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    
    public MetricsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
    
    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        localRootView = (ViewGroup) inflater.inflate(R.layout.metrics_display, null);
        addView(localRootView);
        
        metricsDensity = (TextView) findViewById(R.id.metricsDensity);
        metricsDensityDpi = (TextView) findViewById(R.id.metricsDensityDpi);
        metricsScaledDensity = (TextView) findViewById(R.id.metricsScaledDensity);
        metricsXdpi = (TextView) findViewById(R.id.metricsXdpi);
        metricsYdpi = (TextView) findViewById(R.id.metricsYdpi);
    }
    
    public void updateDisplay(DisplayMetrics metrics) {
        if (metricsDensity != null)
            metricsDensity.setText(String.format("%3.2f", metrics.density));
        
        if (metricsDensityDpi != null)
            metricsDensityDpi.setText(String.format("%3d", metrics.densityDpi));
        
        if (metricsScaledDensity != null)
            metricsScaledDensity.setText(String.format("%3.2f", metrics.scaledDensity));
        
        if (metricsXdpi != null)
            metricsXdpi.setText(String.format("%3.2f", metrics.xdpi));
        
        if (metricsYdpi != null)
            metricsYdpi.setText(String.format("%3.2f", metrics.ydpi));
    }
}
