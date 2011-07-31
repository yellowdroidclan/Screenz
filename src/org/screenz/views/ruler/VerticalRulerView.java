package org.screenz.views.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.codeswimmer.common.data.Values;
import com.codeswimmer.common.util.StringUtils;

public class VerticalRulerView extends RulerView {
    @SuppressWarnings("unused")
    private static final String TAG = VerticalRulerView.class.getSimpleName();
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    
    private Orientation orientation = Orientation.Unknown;
    private float right_inch_offset;
    private float right_half_inch_offset;
    
    public VerticalRulerView(Context context) {
        super(context);
        orientation = Orientation.Left;
    }
    
    public VerticalRulerView(Context context, AttributeSet attrs) {
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
        return new HorzImperialDisplay();
    }
    
    @Override
    protected void recalculateOffsets() {
        calculatOffsetImperial();
    }
    
    protected void calculatOffsetImperial() {
        right_inch_offset = inch_marker_pixels * OFFSET_PERCENT_MINORITY;
        right_half_inch_offset = inch_marker_pixels * OFFSET_PERCENT_MINORITY;
    }

    @Override
    protected void onDrawCurrentPosition(Canvas canvas) {
        float left = orientation.isLeft() ? 0 : right_half_inch_offset;
        float right = orientation.isLeft() ? getRight() - right_half_inch_offset : getRight();
        canvas.drawLine(left, currentTouchPoint.y, right, currentTouchPoint.y, currentPositionPaint);
    }
    
    public static enum Orientation {
        Unknown(Values.EMPTY_STRING),
        Left(LEFT),
        Right(RIGHT);
        
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

        public boolean isRight() {
            return this == Right;
        }

        public boolean isNotRight() {
            return this != Right;
        }
        
        public boolean isLeft() {
            return this == Left;
        }

        public boolean isNotLeft() {
            return this != Left;
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
    
    private class HorzImperialDisplay extends ImperialDisplay {
        @Override
        protected void drawInchMarkers(Canvas canvas) {
            float bottom = getBottom();
            float offset = ppi.Y();
            float left = orientation.isLeft() ? 0 : right_inch_offset;
            float right = orientation.isLeft() ? getRight() - right_inch_offset : getRight();
            
            for (float y = offset; y < bottom; y += offset)
                canvas.drawLine(left, y, right, y, inchPaint);
        }
        
        @Override
        protected void drawHalfInchMarkers(Canvas canvas) {
            float bottom = getBottom();
            float offset = ppi.Y() / 2;
            float left = orientation.isLeft() ? 0 : right_half_inch_offset;
            float right = orientation.isLeft() ? getRight() - right_half_inch_offset : getRight();
            
            for (float y = offset; y < bottom; y += offset)
                canvas.drawLine(left, y, right, y, halfInchPaint);
        }
    }
}
