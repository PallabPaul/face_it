package org.tensorflow;

public final class Operation {
    private final Graph graph;
    private final long unsafeNativeHandle;

    private static native int dtype(long j, long j2, int i);

    private static native String name(long j);

    private static native int numOutputs(long j);

    private static native long[] shape(long j, long j2, int i);

    private static native String type(long j);

    Operation(Graph graph, long j) {
        this.graph = graph;
        this.unsafeNativeHandle = j;
    }

    public String name() {
        Reference ref = this.graph.ref();
        try {
            String name = name(this.unsafeNativeHandle);
            return name;
        } finally {
            ref.close();
        }
    }

    public String type() {
        Reference ref = this.graph.ref();
        try {
            String type = type(this.unsafeNativeHandle);
            return type;
        } finally {
            ref.close();
        }
    }

    public int numOutputs() {
        Reference ref = this.graph.ref();
        try {
            int numOutputs = numOutputs(this.unsafeNativeHandle);
            return numOutputs;
        } finally {
            ref.close();
        }
    }

    public Output output(int i) {
        return new Output(this, i);
    }

    long getUnsafeNativeHandle() {
        return this.unsafeNativeHandle;
    }

    long[] shape(int i) {
        Reference ref = this.graph.ref();
        try {
            long[] shape = shape(ref.nativeHandle(), this.unsafeNativeHandle, i);
            return shape;
        } finally {
            ref.close();
        }
    }

    DataType dtype(int i) {
        Reference ref = this.graph.ref();
        try {
            DataType fromC = DataType.fromC(dtype(ref.nativeHandle(), this.unsafeNativeHandle, i));
            return fromC;
        } finally {
            ref.close();
        }
    }
}
