package org.screenz.data;

public class Size {
    private int width;
    private int height;
    
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public static final Size create(int width, int height) {
        return new Size(width, height);
    }
    
    @Override
    public String toString() {
        return String.format("%dx%d", width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean isEmpty() {
        return width == 0 && height == 0;
    }
    
    public boolean isNotEmpty() {
        return isEmpty() == false;
    }
    
    public static final class Factory {
        public static final Size Empty = new Size(0, 0);
    }
}
