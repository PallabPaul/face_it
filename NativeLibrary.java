package org.tensorflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class NativeLibrary {
    private static final boolean DEBUG = (System.getProperty("org.tensorflow.NativeLibrary.DEBUG") != null);
    private static final String LIBNAME = "tensorflow_jni";

    NativeLibrary() {
    }

    public static void load() {
        if (!isLoaded() && !tryLoadLibrary()) {
            String makeResourceName = makeResourceName();
            log("resourceName: " + makeResourceName);
            InputStream resourceAsStream = NativeLibrary.class.getClassLoader().getResourceAsStream(makeResourceName);
            if (resourceAsStream == null) {
                throw new UnsatisfiedLinkError(String.format("Cannot find TensorFlow native library for OS: %s, architecture: %s. See https://github.com/tensorflow/tensorflow/tree/master/java/README.md for possible solutions (such as building the library from source).", new Object[]{os(), architecture()}));
            }
            try {
                System.load(extractResource(resourceAsStream));
            } catch (IOException e) {
                throw new UnsatisfiedLinkError(String.format("Unable to extract native library into a temporary file (%s)", new Object[]{e.toString()}));
            }
        }
    }

    private static boolean tryLoadLibrary() {
        try {
            System.loadLibrary(LIBNAME);
            return true;
        } catch (UnsatisfiedLinkError e) {
            log("tryLoadLibraryFailed: " + e.getMessage());
            return false;
        }
    }

    private static boolean isLoaded() {
        try {
            TensorFlow.version();
            log("isLoaded: true");
            return true;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    private static String extractResource(InputStream inputStream) throws IOException {
        String mapLibraryName = System.mapLibraryName(LIBNAME);
        int indexOf = mapLibraryName.indexOf(".");
        File createTempFile = File.createTempFile(indexOf < 0 ? mapLibraryName : mapLibraryName.substring(0, indexOf), indexOf < 0 ? null : mapLibraryName.substring(indexOf));
        String absolutePath = createTempFile.getAbsolutePath();
        createTempFile.deleteOnExit();
        log("extracting native library to: " + absolutePath);
        long copy = copy(inputStream, createTempFile);
        log(String.format("copied %d bytes to %s", new Object[]{Long.valueOf(copy), absolutePath}));
        return absolutePath;
    }

    private static String os() {
        String toLowerCase = System.getProperty("os.name").toLowerCase();
        if (toLowerCase.contains("linux")) {
            return "linux";
        }
        if (toLowerCase.contains("os x") || toLowerCase.contains("darwin")) {
            return "darwin";
        }
        if (toLowerCase.contains("windows")) {
            return "windows";
        }
        return toLowerCase.replaceAll("\\s", "");
    }

    private static String architecture() {
        String toLowerCase = System.getProperty("os.arch").toLowerCase();
        return toLowerCase.equals("amd64") ? "x86_64" : toLowerCase;
    }

    private static void log(String str) {
        if (DEBUG) {
            System.err.println("org.tensorflow.NativeLibrary: " + str);
        }
    }

    private static String makeResourceName() {
        return "org/tensorflow/native/" + String.format("%s-%s/", new Object[]{os(), architecture()}) + System.mapLibraryName(LIBNAME);
    }

    private static long copy(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            byte[] bArr = new byte[1048576];
            long j = 0;
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
                j += (long) read;
            }
            return j;
        } finally {
            fileOutputStream.close();
            inputStream.close();
        }
    }
}
