package com.google.appinventor.components.runtime;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
import java.util.List;

public class FrameLayout implements Layout {
    private final android.widget.FrameLayout layoutManager;

    FrameLayout(Context context) {
        this.layoutManager = new android.widget.FrameLayout(context);
    }

    public ViewGroup getLayoutManager() {
        Log.i("FrameLayout", "some one just get my framelayout");
        return this.layoutManager;
    }

    public void add(AndroidViewComponent component) {
        Log.i("FrameLayout", "adding component..");
        this.layoutManager.addView(component.getView(), new LayoutParams(-1, -2));
    }

    public List<Object> getChildren() {
        ArrayList<Object> children = new ArrayList();
        int childcount = this.layoutManager.getChildCount();
        for (int i = 0; i < childcount; i++) {
            children.add(this.layoutManager.getChildAt(i));
        }
        return children;
    }
}
