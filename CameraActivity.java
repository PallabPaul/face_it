package org.faceit.demo;

import android.app.Activity;
import android.content.Intent;
import android.media.Image.Plane;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import java.nio.ByteBuffer;
import org.faceit.demo.CameraConnectionFragment.ConnectionCallback;
import org.faceit.demo.OverlayView.DrawCallback;
import org.faceit.demo.env.Logger;

public abstract class CameraActivity extends Activity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    private static final String PERMISSION_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private boolean debug = false;
    private Handler handler;
    private HandlerThread handlerThread;

    class C02611 implements ConnectionCallback {
        C02611() {
        }

        public void onPreviewSizeChosen(Size size, int rotation) {
            CameraActivity.this.onPreviewSizeChosen(size, rotation);
        }
    }

    protected abstract Size getDesiredPreviewFrameSize();

    protected abstract int getLayoutId();

    protected abstract void onPreviewSizeChosen(Size size, int i);

    protected void onCreate(Bundle savedInstanceState) {
        LOGGER.m9d("onCreate " + this, new Object[0]);
        super.onCreate(null);
        getWindow().addFlags(128);
        setContentView(C0196R.layout.activity_camera);
        if (hasPermission()) {
            setFragment();
        } else {
            requestPermission();
        }
    }

    public void goPreferences(View view) {
        startActivity(new Intent(this, Preferences.class));
    }

    public synchronized void onStart() {
        LOGGER.m9d("onStart " + this, new Object[0]);
        super.onStart();
    }

    public synchronized void onResume() {
        LOGGER.m9d("onResume " + this, new Object[0]);
        super.onResume();
        this.handlerThread = new HandlerThread("inference");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
    }

    public synchronized void onPause() {
        LOGGER.m9d("onPause " + this, new Object[0]);
        if (!isFinishing()) {
            LOGGER.m9d("Requesting finish", new Object[0]);
            finish();
        }
        this.handlerThread.quitSafely();
        try {
            this.handlerThread.join();
            this.handlerThread = null;
            this.handler = null;
        } catch (InterruptedException e) {
            LOGGER.m12e(e, "Exception!", new Object[0]);
        }
        super.onPause();
    }

    public synchronized void onStop() {
        LOGGER.m9d("onStop " + this, new Object[0]);
        super.onStop();
    }

    public synchronized void onDestroy() {
        LOGGER.m9d("onDestroy " + this, new Object[0]);
        super.onDestroy();
    }

    protected synchronized void runInBackground(Runnable r) {
        if (this.handler != null) {
            this.handler.post(r);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == 0 && grantResults[1] == 0) {
                    setFragment();
                    return;
                } else {
                    requestPermission();
                    return;
                }
            default:
                return;
        }
    }

    private boolean hasPermission() {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (checkSelfPermission(PERMISSION_CAMERA) == 0 && checkSelfPermission(PERMISSION_STORAGE) == 0) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (VERSION.SDK_INT >= 23) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA) || shouldShowRequestPermissionRationale(PERMISSION_STORAGE)) {
                Toast.makeText(this, "Camera AND storage permission are required for this demo", 1).show();
            }
            requestPermissions(new String[]{PERMISSION_CAMERA, PERMISSION_STORAGE}, 1);
        }
    }

    protected void setFragment() {
        getFragmentManager().beginTransaction().replace(C0196R.id.container, CameraConnectionFragment.newInstance(new C02611(), this, getLayoutId(), getDesiredPreviewFrameSize())).commit();
    }

    protected void fillBytes(Plane[] planes, byte[][] yuvBytes) {
        for (int i = 0; i < planes.length; i++) {
            ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                LOGGER.m9d("Initializing buffer %d at size %d", Integer.valueOf(i), Integer.valueOf(buffer.capacity()));
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void requestRender() {
        OverlayView overlay = (OverlayView) findViewById(C0196R.id.debug_overlay);
        if (overlay != null) {
            overlay.postInvalidate();
        }
    }

    public void addCallback(DrawCallback callback) {
        OverlayView overlay = (OverlayView) findViewById(C0196R.id.debug_overlay);
        if (overlay != null) {
            overlay.addCallback(callback);
        }
    }

    public void onSetDebug(boolean debug) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 25 && keyCode != 24) {
            return super.onKeyDown(keyCode, event);
        }
        this.debug = !this.debug;
        requestRender();
        onSetDebug(this.debug);
        return true;
    }
}
