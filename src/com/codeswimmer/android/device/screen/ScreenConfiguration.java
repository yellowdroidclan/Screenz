package com.codeswimmer.android.device.screen;

import org.screenz.R;
import org.screenz.data.Size;

public enum ScreenConfiguration {
    Unknown(Size.Factory.Empty, R.string.unknown),
    Qvga(Size.create(320, 240), R.string.qvga),
    Wqvga(Size.create(432, 240), R.string.wqvga),
    Hvga(Size.create(480, 320), R.string.hvga),
    Vga(Size.create(640, 480), R.string.vga),
    Wvga(Size.create(800, 480), R.string.wvga),
    Fwvga(Size.create(854, 480), R.string.fwvga),
    Svga(Size.create(800, 600), R.string.svga),
    Wsvga(Size.create(1024, 576), R.string.wsvga),
    
    Xga(Size.create(1024, 768), R.string.xga),
    Wxga(Size.create(1280, 768), R.string.wxga),
    XgaPlus(Size.create(1152, 864), R.string.wxga_plus),
    WxgaPlus(Size.create(1440, 900), R.string.wxga_plus),
    Sxga(Size.create(1280, 1024), R.string.sxga),
    SxgaPlus(Size.create(1440, 1050), R.string.wxga_plus),
    WsxgaPlus(Size.create(1680, 1050), R.string.wsxga_plus),
    Uxga(Size.create(1600, 1200), R.string.uxga),
    Wuxga(Size.create(1920, 1200), R.string.wuxga),
    
    nHD(Size.create(640, 360), R.string.nhd),
    qHD(Size.create(960, 540), R.string.qhd),
    Wqhd(Size.create(2560, 1440), R.string.wqhd),
    Qfhd(Size.create(3840, 2160), R.string.qfhd),
                                                    
    Qwxga(Size.create(2048, 1152), R.string.qwxga),
    Qxga(Size.create(2048, 1536), R.string.qxga),
    Wqxga(Size.create(2560, 1600), R.string.wqxga),
    Qsxga(Size.create(2560, 2048), R.string.qsxga),
    Wqsxga(Size.create(3200, 2048), R.string.wqsxga),
    Quxga(Size.create(3200, 2400), R.string.quxga),
    Wquxga(Size.create(3840, 2400), R.string.wquxga),
                                                    
    Hxga(Size.create(4096, 3072), R.string.hxga),
    Whxga(Size.create(5120, 3200), R.string.whxga),
    Hsxga(Size.create(5120, 4096), R.string.hsxga),
    Whsxga(Size.create(6400, 4096), R.string.whsxga),
    Huxga(Size.create(6400, 4800), R.string.huxga),
    Whuxga(Size.create(7680, 4800), R.string.whuxga);
    
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
