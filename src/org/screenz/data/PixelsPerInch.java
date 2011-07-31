package org.screenz.data;

public class PixelsPerInch {
    private float x = 0;
    private float y = 0;
    
    public PixelsPerInch(float x, float y) {
        setXY(x, y);
    }
    
    protected void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public float X() {
        return x;
    }
    
    public float Y() {
        return y;
    }
    
    public static final class Factory {
        public static final PixelsPerInch Empty = new PixelsPerInch(0, 0);
    }
}
