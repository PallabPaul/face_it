package org.faceit.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.faceit.demo.env.Logger;

public class CameraConnectionFragment extends Fragment {
    static final /* synthetic */ boolean $assertionsDisabled = (!CameraConnectionFragment.class.desiredAssertionStatus());
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final Logger LOGGER = new Logger();
    private static final int MINIMUM_PREVIEW_SIZE = 320;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private final ConnectionCallback cameraConnectionCallback;
    private CameraDevice cameraDevice;
    private String cameraId;
    private final Semaphore cameraOpenCloseLock = new Semaphore(1);
    private final CaptureCallback captureCallback = new C01924();
    private CameraCaptureSession captureSession;
    private final OnImageAvailableListener imageListener;
    private final Size inputSize;
    private final int layout;
    private ImageReader previewReader;
    private CaptureRequest previewRequest;
    private Builder previewRequestBuilder;
    private Size previewSize;
    private Integer sensorOrientation;
    private final StateCallback stateCallback = new C01902();
    private final SurfaceTextureListener surfaceTextureListener = new C01891();
    private AutoFitTextureView textureView;

    class C01891 implements SurfaceTextureListener {
        C01891() {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            CameraConnectionFragment.this.openCamera(width, height);
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            CameraConnectionFragment.this.configureTransform(width, height);
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    }

    class C01902 extends StateCallback {
        C01902() {
        }

        public void onOpened(CameraDevice cd) {
            CameraConnectionFragment.this.cameraOpenCloseLock.release();
            CameraConnectionFragment.this.cameraDevice = cd;
            CameraConnectionFragment.this.createCameraPreviewSession();
        }

        public void onDisconnected(CameraDevice cd) {
            CameraConnectionFragment.this.cameraOpenCloseLock.release();
            cd.close();
            CameraConnectionFragment.this.cameraDevice = null;
        }

        public void onError(CameraDevice cd, int error) {
            CameraConnectionFragment.this.cameraOpenCloseLock.release();
            cd.close();
            CameraConnectionFragment.this.cameraDevice = null;
            Activity activity = CameraConnectionFragment.this.getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    class C01924 extends CaptureCallback {
        C01924() {
        }

        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
        }

        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        }
    }

    class C01935 extends CameraCaptureSession.StateCallback {
        C01935() {
        }

        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            if (CameraConnectionFragment.this.cameraDevice != null) {
                CameraConnectionFragment.this.captureSession = cameraCaptureSession;
                try {
                    CameraConnectionFragment.this.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(4));
                    CameraConnectionFragment.this.previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(2));
                    CameraConnectionFragment.this.previewRequest = CameraConnectionFragment.this.previewRequestBuilder.build();
                    CameraConnectionFragment.this.captureSession.setRepeatingRequest(CameraConnectionFragment.this.previewRequest, CameraConnectionFragment.this.captureCallback, CameraConnectionFragment.this.backgroundHandler);
                } catch (CameraAccessException e) {
                    CameraConnectionFragment.LOGGER.m12e(e, "Exception!", new Object[0]);
                }
            }
        }

        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            CameraConnectionFragment.this.showToast("Failed");
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {
        CompareSizesByArea() {
        }

        public int compare(Size lhs, Size rhs) {
            return Long.signum((((long) lhs.getWidth()) * ((long) lhs.getHeight())) - (((long) rhs.getWidth()) * ((long) rhs.getHeight())));
        }
    }

    public interface ConnectionCallback {
        void onPreviewSizeChosen(Size size, int i);
    }

    public static class ErrorDialog extends DialogFragment {
        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity).setMessage(getArguments().getString(ARG_MESSAGE)).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.finish();
                }
            }).create();
        }
    }

    static {
        ORIENTATIONS.append(0, 90);
        ORIENTATIONS.append(1, 0);
        ORIENTATIONS.append(2, 270);
        ORIENTATIONS.append(3, 180);
    }

    private CameraConnectionFragment(ConnectionCallback connectionCallback, OnImageAvailableListener imageListener, int layout, Size inputSize) {
        this.cameraConnectionCallback = connectionCallback;
        this.imageListener = imageListener;
        this.layout = layout;
        this.inputSize = inputSize;
    }

    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, text, 0).show();
                }
            });
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height) {
        int minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE);
        Size desiredSize = new Size(width, height);
        boolean exactSizeFound = false;
        List<Size> bigEnough = new ArrayList();
        List<Size> tooSmall = new ArrayList();
        for (Size option : choices) {
            if (option.equals(desiredSize)) {
                exactSizeFound = true;
            }
            if (option.getHeight() < minSize || option.getWidth() < minSize) {
                tooSmall.add(option);
            } else {
                bigEnough.add(option);
            }
        }
        LOGGER.m13i("Desired size: " + desiredSize + ", min size: " + minSize + "x" + minSize, new Object[0]);
        LOGGER.m13i("Valid preview sizes: [" + TextUtils.join(", ", bigEnough) + "]", new Object[0]);
        LOGGER.m13i("Rejected preview sizes: [" + TextUtils.join(", ", tooSmall) + "]", new Object[0]);
        if (exactSizeFound) {
            LOGGER.m13i("Exact size match found.", new Object[0]);
            return desiredSize;
        } else if (bigEnough.size() > 0) {
            Size chosenSize = (Size) Collections.min(bigEnough, new CompareSizesByArea());
            LOGGER.m13i("Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight(), new Object[0]);
            return chosenSize;
        } else {
            LOGGER.m11e("Couldn't find any suitable preview size", new Object[0]);
            return choices[0];
        }
    }

    public static CameraConnectionFragment newInstance(ConnectionCallback callback, OnImageAvailableListener imageListener, int layout, Size inputSize) {
        return new CameraConnectionFragment(callback, imageListener, layout, inputSize);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.layout, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.textureView = (AutoFitTextureView) view.findViewById(C0196R.id.texture);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (this.textureView.isAvailable()) {
            openCamera(this.textureView.getWidth(), this.textureView.getHeight());
        } else {
            this.textureView.setSurfaceTextureListener(this.surfaceTextureListener);
        }
    }

    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getActivity().getSystemService("camera");
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer facing = (Integer) characteristics.get(CameraCharacteristics.LENS_FACING);
                StreamConfigurationMap map = (StreamConfigurationMap) characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map != null) {
                    Size largest = (Size) Collections.max(Arrays.asList(map.getOutputSizes(35)), new CompareSizesByArea());
                    this.sensorOrientation = (Integer) characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                    this.previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), this.inputSize.getWidth(), this.inputSize.getHeight());
                    if (getResources().getConfiguration().orientation == 2) {
                        this.textureView.setAspectRatio(this.previewSize.getWidth(), this.previewSize.getHeight());
                    } else {
                        this.textureView.setAspectRatio(this.previewSize.getHeight(), this.previewSize.getWidth());
                    }
                    this.cameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            LOGGER.m12e(e, "Exception!", new Object[0]);
        } catch (NullPointerException e2) {
            ErrorDialog.newInstance(getString(C0196R.string.camera_error)).show(getChildFragmentManager(), FRAGMENT_DIALOG);
            throw new RuntimeException(getString(C0196R.string.camera_error));
        }
        this.cameraConnectionCallback.onPreviewSizeChosen(this.previewSize, this.sensorOrientation.intValue());
    }

    private void openCamera(int width, int height) {
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) getActivity().getSystemService("camera");
        try {
            if (this.cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                manager.openCamera(this.cameraId, this.stateCallback, this.backgroundHandler);
                return;
            }
            throw new RuntimeException("Time out waiting to lock camera opening.");
        } catch (CameraAccessException e) {
            LOGGER.m12e(e, "Exception!", new Object[0]);
        } catch (InterruptedException e2) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e2);
        }
    }

    private void closeCamera() {
        try {
            this.cameraOpenCloseLock.acquire();
            if (this.captureSession != null) {
                this.captureSession.close();
                this.captureSession = null;
            }
            if (this.cameraDevice != null) {
                this.cameraDevice.close();
                this.cameraDevice = null;
            }
            if (this.previewReader != null) {
                this.previewReader.close();
                this.previewReader = null;
            }
            this.cameraOpenCloseLock.release();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } catch (Throwable th) {
            this.cameraOpenCloseLock.release();
        }
    }

    private void startBackgroundThread() {
        this.backgroundThread = new HandlerThread("ImageListener");
        this.backgroundThread.start();
        this.backgroundHandler = new Handler(this.backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        this.backgroundThread.quitSafely();
        try {
            this.backgroundThread.join();
            this.backgroundThread = null;
            this.backgroundHandler = null;
        } catch (InterruptedException e) {
            LOGGER.m12e(e, "Exception!", new Object[0]);
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = this.textureView.getSurfaceTexture();
            if ($assertionsDisabled || texture != null) {
                texture.setDefaultBufferSize(this.previewSize.getWidth(), this.previewSize.getHeight());
                Surface surface = new Surface(texture);
                this.previewRequestBuilder = this.cameraDevice.createCaptureRequest(1);
                this.previewRequestBuilder.addTarget(surface);
                LOGGER.m13i("Opening camera preview: " + this.previewSize.getWidth() + "x" + this.previewSize.getHeight(), new Object[0]);
                this.previewReader = ImageReader.newInstance(this.previewSize.getWidth(), this.previewSize.getHeight(), 35, 2);
                this.previewReader.setOnImageAvailableListener(this.imageListener, this.backgroundHandler);
                this.previewRequestBuilder.addTarget(this.previewReader.getSurface());
                this.cameraDevice.createCaptureSession(Arrays.asList(new Surface[]{surface, this.previewReader.getSurface()}), new C01935(), null);
                return;
            }
            throw new AssertionError();
        } catch (CameraAccessException e) {
            LOGGER.m12e(e, "Exception!", new Object[0]);
        }
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (this.textureView != null && this.previewSize != null && activity != null) {
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            Matrix matrix = new Matrix();
            RectF viewRect = new RectF(0.0f, 0.0f, (float) viewWidth, (float) viewHeight);
            RectF bufferRect = new RectF(0.0f, 0.0f, (float) this.previewSize.getHeight(), (float) this.previewSize.getWidth());
            float centerX = viewRect.centerX();
            float centerY = viewRect.centerY();
            if (1 == rotation || 3 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
                matrix.setRectToRect(viewRect, bufferRect, ScaleToFit.FILL);
                float scale = Math.max(((float) viewHeight) / ((float) this.previewSize.getHeight()), ((float) viewWidth) / ((float) this.previewSize.getWidth()));
                matrix.postScale(scale, scale, centerX, centerY);
                matrix.postRotate((float) ((rotation - 2) * 90), centerX, centerY);
            } else if (2 == rotation) {
                matrix.postRotate(180.0f, centerX, centerY);
            }
            this.textureView.setTransform(matrix);
        }
    }
}
