package org.faceit.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.LinkedList;
import java.util.List;

public class OverlayView extends View {
    private final List<DrawCallback> callbacks = new LinkedList();

    public interface DrawCallback {
        void drawCallback(Canvas canvas);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addCallback(DrawCallback callback) {
        this.callbacks.add(callback);
    }

    public synchronized void draw(Canvas canvas) {
        for (DrawCallback callback : this.callbacks) {
            callback.drawCallback(canvas);
        }
    }
}
