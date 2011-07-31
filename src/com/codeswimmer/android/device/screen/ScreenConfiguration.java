package com.codeswimmer.android.device.screen;

import org.screenz.R;
import org.screenz.data.Size;

public enum ScreenConfiguration {
    Unknown(Size.Factory.Empty, R.string.unknown),
    Qvga(Size.create(320, 240), R.string.qvga),
    Wqvga(Size.create(432, 240), R.string.wqvga),
    Hvga(Size.create(480, 320), R.string.hvga),
    Wvga(Size.create(800, 480), R.string.wvga),
    Fwvga(Size.create(854, 480), R.string.fwvga),
    Svga(Size.create(800, 600), R.string.svga),
    Wsvga(Size.create(1024, 576), R.string.wsvga),
    Xga(Size.create(1024, 768), R.string.xga),
    Wxga(Size.create(1280, 768), R.string.wxga);
    
    private Size thresholdSize;
    private int displayNameResId;
    
    private ScreenConfiguration(Size thresholdSize, int displayNameResId) {
        this.thresholdSize = thresholdSize;
        this.displayNameResId = displayNameResId;
    }
    
    public static final ScreenConfiguration fromSize(int w, int h) {
        if (Wxga.contains(w, h))
            return Wxga;
        
        if (Xga.contains(w, h))
            return Xga;
        
        if (Wsvga.contains(w, h))
            return Wsvga;
        
        if (Svga.contains(w, h))
            return Svga;
        
        if (Fwvga.contains(w, h))
            return Fwvga;
        
        if (Wvga.contains(w, h))
            return Wvga;
        
        if (Hvga.contains(w, h))
            return Hvga;
        
        if (Wqvga.contains(w, h))
            return Wqvga;
        
        if (Qvga.contains(w, h))
            return Qvga;
        
        return Unknown;
    }
    
    public boolean contains(int w, int h) {
        int thresholdWidth = thresholdSize.getWidth();
        int thresholdHeight = thresholdSize.getHeight();
        if (w >= thresholdWidth)
            if (h >= thresholdHeight)
                return true;
        return false;
    }
    
    public int getDisplayNameResId() {
        return displayNameResId;
    }
}
