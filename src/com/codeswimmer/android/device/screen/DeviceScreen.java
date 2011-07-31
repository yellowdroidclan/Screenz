package com.codeswimmer.android.device.screen;

import android.content.Context;
import android.content.res.Configuration;

public class DeviceScreen {
    
    public static final ScreenSize getDeviceScreenSize(Context context) {
        Configuration config = context.getResources().getConfiguration();
        
        if (isXlarge(config))
            return ScreenSize.X_large;
        else if (isLarge(config))
            return ScreenSize.Large;
        else if (isNormal(config))
            return ScreenSize.Normal;
        else if (isSmall(config))
            return ScreenSize.Small;
        
        return ScreenSize.Undefined;
    }
    
    private static boolean isXlarge(Configuration config) {
        return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
    
    private static boolean isLarge(Configuration config) {
        return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    private static boolean isNormal(Configuration config) {
        return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL;
    }
    
    private static boolean isSmall(Configuration config) {
        return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL;
    }
}
