package org.faceit.demo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Size;
import android.util.TypedValue;
import java.util.List;
import java.util.Vector;
import org.faceit.demo.Classifier.Recognition;
import org.faceit.demo.OverlayView.DrawCallback;
import org.faceit.demo.env.BorderedText;
import org.faceit.demo.env.ImageUtils;
import org.faceit.demo.env.Logger;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "Mul:0";
    private static final int INPUT_SIZE = 299;
    private static final String LABEL_FILE = "file:///android_asset/retrained_labels.txt";
    private static final Logger LOGGER = new Logger();
    private static final boolean MAINTAIN_ASPECT = true;
    private static final String MODEL_FILE = "file:///android_asset/rounded_graph.pb";
    private static final String OUTPUT_NAME = "final_result";
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10.0f;
    private BorderedText borderedText;
    private Classifier classifier;
    private boolean computing = false;
    private Bitmap cropCopyBitmap;
    private Matrix cropToFrameTransform;
    private Bitmap croppedBitmap = null;
    private Matrix frameToCropTransform;
    private long lastProcessingTimeMs;
    private int previewHeight = 0;
    private int previewWidth = 0;
    private ResultsView resultsView;
    private int[] rgbBytes = null;
    private Bitmap rgbFrameBitmap = null;
    private Integer sensorOrientation;
    private byte[][] yuvBytes;

    class C01952 implements Runnable {
        C01952() {
        }

        public void run() {
            long startTime = SystemClock.uptimeMillis();
            List<Recognition> results = ClassifierActivity.this.classifier.recognizeImage(ClassifierActivity.this.croppedBitmap);
            ClassifierActivity.this.lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
            ClassifierActivity.this.cropCopyBitmap = Bitmap.createBitmap(ClassifierActivity.this.croppedBitmap);
            ClassifierActivity.this.resultsView.setResults(results);
            ClassifierActivity.this.requestRender();
            ClassifierActivity.this.computing = false;
        }
    }

    class C02621 implements DrawCallback {
        C02621() {
        }

        public void drawCallback(Canvas canvas) {
            ClassifierActivity.this.renderDebug(canvas);
        }
    }

    protected int getLayoutId() {
        return C0196R.layout.camera_connection_fragment;
    }

    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    public void onPreviewSizeChosen(Size size, int rotation) {
        this.borderedText = new BorderedText(TypedValue.applyDimension(1, TEXT_SIZE_DIP, getResources().getDisplayMetrics()));
        this.borderedText.setTypeface(Typeface.MONOSPACE);
        this.classifier = TensorFlowImageClassifier.create(getAssets(), MODEL_FILE, LABEL_FILE, INPUT_SIZE, 128, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);
        this.resultsView = (ResultsView) findViewById(C0196R.id.results);
        this.previewWidth = size.getWidth();
        this.previewHeight = size.getHeight();
        LOGGER.m13i("Sensor orientation: %d, Screen orientation: %d", Integer.valueOf(rotation), Integer.valueOf(getWindowManager().getDefaultDisplay().getRotation()));
        this.sensorOrientation = Integer.valueOf(rotation + screenOrientation);
        LOGGER.m13i("Initializing at size %dx%d", Integer.valueOf(this.previewWidth), Integer.valueOf(this.previewHeight));
        this.rgbBytes = new int[(this.previewWidth * this.previewHeight)];
        this.rgbFrameBitmap = Bitmap.createBitmap(this.previewWidth, this.previewHeight, Config.ARGB_8888);
        this.croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);
        this.frameToCropTransform = ImageUtils.getTransformationMatrix(this.previewWidth, this.previewHeight, INPUT_SIZE, INPUT_SIZE, this.sensorOrientation.intValue(), MAINTAIN_ASPECT);
        this.cropToFrameTransform = new Matrix();
        this.frameToCropTransform.invert(this.cropToFrameTransform);
        this.yuvBytes = new byte[3][];
        addCallback(new C02621());
    }

    public void onImageAvailable(ImageReader reader) {
        Image image = null;
        try {
            image = reader.acquireLatestImage();
            if (image != null) {
                if (this.computing) {
                    image.close();
                    return;
                }
                this.computing = MAINTAIN_ASPECT;
                Trace.beginSection("imageAvailable");
                Plane[] planes = image.getPlanes();
                fillBytes(planes, this.yuvBytes);
                ImageUtils.convertYUV420ToARGB8888(this.yuvBytes[0], this.yuvBytes[1], this.yuvBytes[2], this.previewWidth, this.previewHeight, planes[0].getRowStride(), planes[1].getRowStride(), planes[1].getPixelStride(), this.rgbBytes);
                image.close();
                this.rgbFrameBitmap.setPixels(this.rgbBytes, 0, this.previewWidth, 0, 0, this.previewWidth, this.previewHeight);
                new Canvas(this.croppedBitmap).drawBitmap(this.rgbFrameBitmap, this.frameToCropTransform, null);
                runInBackground(new C01952());
                Trace.endSection();
            }
        } catch (Throwable e) {
            if (image != null) {
                image.close();
            }
            LOGGER.m12e(e, "Exception!", new Object[0]);
            Trace.endSection();
        }
    }

    public void onSetDebug(boolean debug) {
        this.classifier.enableStatLogging(debug);
    }

    private void renderDebug(Canvas canvas) {
        if (isDebug()) {
            Bitmap copy = this.cropCopyBitmap;
            if (copy != null) {
                Matrix matrix = new Matrix();
                matrix.postScale(2.0f, 2.0f);
                matrix.postTranslate(((float) canvas.getWidth()) - (((float) copy.getWidth()) * 2.0f), ((float) canvas.getHeight()) - (((float) copy.getHeight()) * 2.0f));
                canvas.drawBitmap(copy, matrix, new Paint());
                Vector<String> lines = new Vector();
                if (this.classifier != null) {
                    for (String line : this.classifier.getStatString().split("\n")) {
                        lines.add(line);
                    }
                }
                lines.add("Frame: " + this.previewWidth + "x" + this.previewHeight);
                lines.add("Crop: " + copy.getWidth() + "x" + copy.getHeight());
                lines.add("View: " + canvas.getWidth() + "x" + canvas.getHeight());
                lines.add("Rotation: " + this.sensorOrientation);
                lines.add("Inference time: " + this.lastProcessingTimeMs + "ms");
                this.borderedText.drawLines(canvas, TEXT_SIZE_DIP, (float) (canvas.getHeight() - 10), lines);
            }
        }
    }
}
