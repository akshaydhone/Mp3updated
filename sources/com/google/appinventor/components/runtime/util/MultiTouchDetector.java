package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.google.appinventor.components.runtime.Canvas;
import com.google.appinventor.components.runtime.Canvas.ExtensionGestureDetector;

public class MultiTouchDetector {
    private Canvas myCanvas;

    public class MyOnScaleGestureListener extends SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            MultiTouchDetector.this.myCanvas.Scale((double) detector.getScaleFactor());
            return true;
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    public class MyPinchZoomDetector extends ScaleGestureDetector implements ExtensionGestureDetector {
        MyPinchZoomDetector(Context c, OnScaleGestureListener l) {
            super(c, l);
        }
    }

    public MultiTouchDetector(Canvas canvas) {
        registerCanvas(canvas);
    }

    private void registerCanvas(Canvas myCanvas) {
        if (myCanvas != null) {
            this.myCanvas = myCanvas;
            myCanvas.registerCustomGestureDetector(new MyPinchZoomDetector(myCanvas.getContext(), new MyOnScaleGestureListener()));
        }
    }
}
