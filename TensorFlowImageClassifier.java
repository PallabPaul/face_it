package org.faceit.demo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Trace;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import org.faceit.demo.Classifier.Recognition;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TensorFlowImageClassifier implements Classifier {
    private static final int MAX_RESULTS = 1;
    private static final String TAG = "TensorFlowImageClassifier";
    private static final float THRESHOLD = 0.1f;
    private float[] floatValues;
    private int imageMean;
    private float imageStd;
    private TensorFlowInferenceInterface inferenceInterface;
    private String inputName;
    private int inputSize;
    private int[] intValues;
    private Vector<String> labels = new Vector();
    private boolean logStats = false;
    private String outputName;
    private String[] outputNames;
    private float[] outputs;

    class C01971 implements Comparator<Recognition> {
        C01971() {
        }

        public int compare(Recognition lhs, Recognition rhs) {
            return Float.compare(rhs.getConfidence().floatValue(), lhs.getConfidence().floatValue());
        }
    }

    private TensorFlowImageClassifier() {
    }

    public static Classifier create(AssetManager assetManager, String modelFilename, String labelFilename, int inputSize, int imageMean, float imageStd, String inputName, String outputName) {
        IOException e;
        TensorFlowImageClassifier c = new TensorFlowImageClassifier();
        c.inputName = inputName;
        c.outputName = outputName;
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        Log.i(TAG, "Reading labels from: " + actualFilename);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(actualFilename)));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line != null) {
                        c.labels.add(line);
                    } else {
                        br.close();
                        c.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);
                        int numClasses = (int) c.inferenceInterface.graphOperation(outputName).output(0).shape().size(1);
                        Log.i(TAG, "Read " + c.labels.size() + " labels, output layer size is " + numClasses);
                        c.inputSize = inputSize;
                        c.imageMean = imageMean;
                        c.imageStd = imageStd;
                        c.outputNames = new String[]{outputName};
                        c.intValues = new int[(inputSize * inputSize)];
                        c.floatValues = new float[((inputSize * inputSize) * 3)];
                        c.outputs = new float[numClasses];
                        return c;
                    }
                } catch (IOException e2) {
                    e = e2;
                    BufferedReader bufferedReader = br;
                }
            }
        } catch (IOException e3) {
            e = e3;
            throw new RuntimeException("Problem reading label file!", e);
        }
    }

    public List<Recognition> recognizeImage(Bitmap bitmap) {
        int i;
        Trace.beginSection("recognizeImage");
        Trace.beginSection("preprocessBitmap");
        bitmap.getPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (i = 0; i < this.intValues.length; i++) {
            int val = this.intValues[i];
            this.floatValues[(i * 3) + 0] = ((float) (((val >> 16) & 255) - this.imageMean)) / this.imageStd;
            this.floatValues[(i * 3) + 1] = ((float) (((val >> 8) & 255) - this.imageMean)) / this.imageStd;
            this.floatValues[(i * 3) + 2] = ((float) ((val & 255) - this.imageMean)) / this.imageStd;
        }
        Trace.endSection();
        Trace.beginSection("feed");
        this.inferenceInterface.feed(this.inputName, this.floatValues, 1, (long) this.inputSize, (long) this.inputSize, 3);
        Trace.endSection();
        Trace.beginSection("run");
        this.inferenceInterface.run(this.outputNames, this.logStats);
        Trace.endSection();
        Trace.beginSection("fetch");
        this.inferenceInterface.fetch(this.outputName, this.outputs);
        Trace.endSection();
        PriorityQueue<Recognition> pq = new PriorityQueue(3, new C01971());
        i = 0;
        while (i < this.outputs.length) {
            if (this.outputs[i] > THRESHOLD) {
                pq.add(new Recognition("" + i, this.labels.size() > i ? (String) this.labels.get(i) : EnvironmentCompat.MEDIA_UNKNOWN, Float.valueOf(this.outputs[i]), null));
            }
            i++;
        }
        ArrayList<Recognition> recognitions = new ArrayList();
        int recognitionsSize = Math.min(pq.size(), 1);
        for (i = 0; i < recognitionsSize; i++) {
            recognitions.add(pq.poll());
        }
        Trace.endSection();
        return recognitions;
    }

    public void enableStatLogging(boolean logStats) {
        this.logStats = logStats;
    }

    public String getStatString() {
        return this.inferenceInterface.getStatString();
    }

    public void close() {
        this.inferenceInterface.close();
    }
}
