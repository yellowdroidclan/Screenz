package com.codeswimmer.android.device.screen;

import org.screenz.R;


public enum ScreenDensity {
    Unknown(0, R.string.unknown),
    ldpi(120, R.string.hdpi),
    mdpi(160, R.string.mdpi),
    hdpi(240, R.string.hdpi),
    xhdpi(320, R.string.xhdpi);
    
    private int thresholdDensity;
    private int displayNameResId;
    
    private ScreenDensity(int thresholdDensity, int displayNameResId) {
        this.thresholdDensity = thresholdDensity;
        this.displayNameResId = displayNameResId;
    }
    
    public static final ScreenDensity fromSize(int d) {
        if (isXhdpi(d))
            return xhdpi;
        
        if (isHdpi(d))
            return hdpi;
        
        if (isMdpi(d))
            return mdpi;
        
        if (isLdpi(d))
            return ldpi;
        
        return Unknown;
    }
    
    private static boolean isLdpi(int d) {
        if (d >= ldpi.thresholdDensity)
            return true;
        return false;
    }
    
    private static boolean isMdpi(int d) {
        if (d >= mdpi.thresholdDensity)
            return true;
        return false;
    }
    
    private static boolean isHdpi(int d) {
        if (d >= hdpi.thresholdDensity)
            return true;
        return false;
    }
    
    private static boolean isXhdpi(int d) {
        if (d >= xhdpi.thresholdDensity)
            return true;
        return false;
    }

    public int getDisplayNameResId() {
        return displayNameResId;
    }
    
    public boolean isUnknown() {
        return this == Unknown;
    }

    public boolean isNotUnknown() {
        return this != Unknown;
    }
}
