package org.screenz.views.grid;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.PointF;

public class PositionUpdater {
    public static final UUID UNKNOWN_KEY = UUID.randomUUID();
    
    private static final Listeners listeners = new Listeners();
    
    public UUID addListener(OnPositionUpdatedListener listener) {
        if (listener == null)
            return UNKNOWN_KEY;
        
        UUID key = UUID.randomUUID();
        listeners.put(key, listener);
        return key;
    }
    
    public OnPositionUpdatedListener removeListener(UUID key) {
        if (key == null)
            return null;
        
        OnPositionUpdatedListener listener = listeners.get(key);
        if (listener == null)
            return null;
        
        return listeners.remove(key);
    }
    
    public void notifyPositionUpdated(PointF currentPosition) {
        for (OnPositionUpdatedListener listener : listeners.values())
            listener.onPositionUpdated(currentPosition);
    }
    
    public static interface OnPositionUpdatedListener {
        public void onPositionUpdated(PointF currentPosition);
    }
    
    @SuppressWarnings("serial")
    private static class Listeners extends ConcurrentHashMap<UUID, OnPositionUpdatedListener> {}
}
