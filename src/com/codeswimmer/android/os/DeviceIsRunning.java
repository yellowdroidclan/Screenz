package com.codeswimmer.android.os;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Looper;

public final class DeviceIsRunning {

    public static final boolean Honeycomb = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    
    public static final boolean landscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    
    public static final boolean onMainUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    public static final boolean offMainUiThread() {
        return Looper.getMainLooper().getThread() != Thread.currentThread();
    }
}
