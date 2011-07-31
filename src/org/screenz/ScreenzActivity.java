package org.screenz;

import org.screenz.data.Size;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.codeswimmer.android.os.DeviceIsRunning;

public class ScreenzActivity extends Activity {
    @SuppressWarnings("unused")
    private static final String TAG = ScreenzActivity.class.getSimpleName();
    
    // TODO Extract these out into a DeviceInfoView class; update main.xml to <include> it
    private TextView effectiveSizeWidthView;
    private TextView effectiveSizeHeightView;
    private TextView resolutionView;
    private TextView densityView;
    private TextView deviceBuildInfoView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ScreenzApp.getInstance().initialize(this);
        storeWidgetHandles();
        updateWidgetValues();
        
        if (DeviceIsRunning.Honeycomb)
            customizeActionBar();
    }
    
    private void customizeActionBar() {
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.custom_action_bar);
        getActionBar().show();
    }
    
    private void storeWidgetHandles() {
        effectiveSizeWidthView = (TextView) findViewById(R.id.effectiveSizeWidthView);
        effectiveSizeHeightView = (TextView) findViewById(R.id.effectiveSizeHeightView);
        resolutionView = (TextView) findViewById(R.id.resolutionView);
        densityView = (TextView) findViewById(R.id.densityView);
        deviceBuildInfoView = (TextView) findViewById(R.id.deviceBuildInfoView);
    }
    
    private void updateWidgetValues() {
        String buildInfoText = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        deviceBuildInfoView.setText(buildInfoText);
        
        DisplayMetrics dm = ScreenzApp.getInstance().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        
        Size dpSize = ScreenzApp.getInstance().getScreenSizeInDpUnits(width, height);
        
        effectiveSizeWidthView.setText(Integer.toString(dpSize.getWidth()));
        effectiveSizeHeightView.setText(Integer.toString(dpSize.getHeight()));
        densityView.setText(String.format("%d dpi", dm.densityDpi));
        
        String resolutionText = determineScreenSize(width, height);
        resolutionView.setText(resolutionText);
    }
    
    private String determineScreenSize(int width, int height) {
        String physicalPixels = getString(R.string.physical_pixels);
        return String.format("(%d x %d %s)", width, height, physicalPixels);
    }

}