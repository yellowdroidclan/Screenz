package com.codeswimmer.android.device.screen;

import org.screenz.R;
import org.screenz.data.Size;

public enum ScreenSize {
    Undefined(Size.Factory.Empty, R.string.unknown),
    Small(Size.create(426, 320), R.string.small),
    Normal(Size.create(470, 320), R.string.normal),
    Large(Size.create(640, 480), R.string.large),
    X_large(Size.create(960, 720), R.string.xlarge);
    
    public static final ScreenSize[] bySizeAscending = { Small, Normal, Large, X_large };
    public static final ScreenSize[] bySizeDescending = { X_large, Large, Normal, Small };
    
    private Size thresholdSize;
    private int displayNameResId;
    
    private ScreenSize(Size thresholdSize, int displayNameResId) {
        this.thresholdSize = thresholdSize;
        this.displayNameResId = displayNameResId;
    }
    
    public boolean isUndefined() {
        return this == Undefined;
    }

    public boolean isNotUndefined() {
        return this != Undefined;
    }
    
    public Size getThresholdSize() {
        return thresholdSize;
    }

    public int getDisplayNameResId() {
        return displayNameResId;
    }
}
