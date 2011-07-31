package org.screenz.views.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.codeswimmer.common.data.Values;
import com.codeswimmer.common.util.StringUtils;

public class HorizontalRulerView extends RulerView {
    @SuppressWarnings("unused")
    private static final String TAG = HorizontalRulerView.class.getSimpleName();
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    
    private Orientation orientation = Orientation.Unknown;
    private float bottom_inch_offset;
    private float bottom_half_inch_offset;
    
    public HorizontalRulerView(Context context) {
        super(context);
        orientation = Orientation.Top;
    }
    
    public HorizontalRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractOrientation(attrs);
    }
    
    private void extractOrientation(AttributeSet attrs) {
        if (attrs == null)
            return;
        
        int numAttrs = attrs.getAttributeCount();
        
        for (int i = 0; i < numAttrs; i++) {
            String name = attrs.getAttributeName(i);
            
            if (isOrientationAttribute(name)) {
                String value = attrs.getAttributeValue(i);
                Orientation tempOrientation = Orientation.fromAttr(value);
                if (tempOrientation.isNotUnknown())
                    orientation = tempOrientation;
            }
        }
    }
    
    private boolean isOrientationAttribute(String name) {
        if (StringUtils.isBlank(name))
            return false;
        
        return ORIENTATION_ATTR.equals(name);        
    }
    
    @Override
    protected Displayer onProvideDisplayer() {
        return new VertImperialDisplay();
    }
    
    @Override
    protected void recalculateOffsets() {
        calculatOffsetMetric();
    }

    protected void calculatOffsetMetric() {
        bottom_inch_offset = inch_marker_pixels * OFFSET_PERCENT_MINORITY;
        bottom_half_inch_offset = inch_marker_pixels * OFFSET_PERCENT_MINORITY;
    }
    
    @Override
    protected void onDrawCurrentPosition(Canvas canvas) {
        float top = orientation.isTop() ? 0 : bottom_inch_offset;
        float bottom = orientation.isTop() ? getBottom() - bottom_inch_offset : getBottom();
        canvas.drawLine(currentTouchPoint.x, top, currentTouchPoint.x, bottom, currentPositionPaint);
    }

    public static enum Orientation {
        Unknown(Values.EMPTY_STRING),
        Top(TOP),
        Bottom(BOTTOM);
        
        private String attrValueName;

        private Orientation(String attrValueName) {
            this.attrValueName = attrValueName;
        }
        
        public static final Orientation fromAttr(String value) {
            if (StringUtils.isBlank(value))
                return Orientation.Unknown;
            
            for (Orientation orientation : Orientation.values())
                if (orientation.isNotUnknown()) {
                    String attrValueName = orientation.getAttrValueName();
                    if (value.equals(attrValueName))
                        return orientation;
                }
            
            return Orientation.Unknown;
        }
        
        public boolean isBottom() {
            return this == Bottom;
        }

        public boolean isNotBottom() {
            return this != Bottom;
        }
        
        public boolean isTop() {
            return this == Top;
        }

        public boolean isNotTop() {
            return this != Top;
        }
        
        public boolean isUnknown() {
            return this == Unknown;
        }

        public boolean isNotUnknown() {
            return this != Unknown;
        }

        public String getAttrValueName() {
            return attrValueName;
        }
    }
    
    private class VertImperialDisplay extends ImperialDisplay {
        @Override
        protected void drawInchMarkers(Canvas canvas) {
            float right = getRight();
            float offset = (ppi != null) ? ppi.X() : 0;
            float top = orientation.isTop() ? 0 : bottom_inch_offset;
            float bottom = orientation.isTop() ? getBottom() - bottom_inch_offset : getBottom();
            
            for (float x = offset; x < right; x += offset)
                canvas.drawLine(x, top, x, bottom, inchPaint);
        }
        
        @Override
        protected void drawHalfInchMarkers(Canvas canvas) {
            float right = getRight();
            float offset = (ppi != null) ? ppi.X() / 2 : 0;
            float top = orientation.isTop() ? 0 : bottom_inch_offset;
            float bottom = orientation.isTop() ? getBottom() - bottom_half_inch_offset : getBottom();
            
            for (float x = offset; x < right; x += offset)
                canvas.drawLine(x, top, x, bottom, halfInchPaint);
        }
    }
}
