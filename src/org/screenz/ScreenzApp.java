package org.screenz;

import org.screenz.data.PixelsPerInch;
import org.screenz.data.Size;
import org.screenz.views.grid.PositionUpdater;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenzApp {
    public static final PositionUpdater PositionUpdater = new PositionUpdater();
    
    private DisplayMetrics displayMetrics;
    private Activity activity;
    private PixelsPerInch ppi;
    
    ScreenzApp() {}
    
    void initialize(Activity activity) {
        this.activity = activity;
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        initializePixelsPerInch();
    }
    
    private void initializePixelsPerInch() {
        if (displayMetrics == null)
            ppi = PixelsPerInch.Factory.Empty;
        
        ppi = new PixelsPerInch(displayMetrics.xdpi, displayMetrics.ydpi);
    }
    
    public float toDpUnits(float value) {
        float scalingFactor = displayMetrics.density;
        return (int)(value / scalingFactor);
    }
    
    public PixelsPerInch getPixelsPerInch() {
        return ppi;
    }
    
    public float getExactPixelsPerInchX() {
        if (displayMetrics != null)
            return displayMetrics.xdpi;
        return 160;
    }
    
    public float getExactPixelsPerInchY() {
        if (displayMetrics != null)
            return displayMetrics.ydpi;
        return 160;
    }
    
    public Size getScreenSizeInDpUnits(int width, int height) {
        int dpWidth = getWidthInDpUnits(width);
        int dpHeight = getHeightInDpUnits(height);
        Size sizeDpUnits = new Size(dpWidth, dpHeight);
        return sizeDpUnits;
    }
    
    public int getWidthInDpUnits(int width) {
        float scalingFactor = displayMetrics.density;
        return (int)(width / scalingFactor); // TODO: Is it necessary to round up?
    }
    
    public int getHeightInDpUnits(int width) {
        float scalingFactor = displayMetrics.density;
        return (int)(width / scalingFactor); // TODO: Is it necessary to round up?
    }
    
    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }
    
    public static ScreenzApp getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    private static class SingletonHolder { 
        private static final ScreenzApp INSTANCE = new ScreenzApp();
    }

    public Activity getActivity() {
        return activity;
    }
}
