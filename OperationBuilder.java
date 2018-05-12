package org.tensorflow;

import java.nio.charset.Charset;

public final class OperationBuilder {
    private Graph graph;
    private long unsafeNativeHandle;

    private static native void addInput(long j, long j2, int i);

    private static native void addInputList(long j, long[] jArr, int[] iArr);

    private static native long allocate(long j, String str, String str2);

    private static native long finish(long j);

    private static native void setAttrBool(long j, String str, boolean z);

    private static native void setAttrBoolList(long j, String str, boolean[] zArr);

    private static native void setAttrFloat(long j, String str, float f);

    private static native void setAttrFloatList(long j, String str, float[] fArr);

    private static native void setAttrInt(long j, String str, long j2);

    private static native void setAttrIntList(long j, String str, long[] jArr);

    private static native void setAttrShape(long j, String str, long[] jArr, int i);

    private static native void setAttrString(long j, String str, byte[] bArr);

    private static native void setAttrTensor(long j, String str, long j2);

    private static native void setAttrTensorList(long j, String str, long[] jArr);

    private static native void setAttrType(long j, String str, int i);

    private static native void setAttrTypeList(long j, String str, int[] iArr);

    private static native void setDevice(long j, String str);

    OperationBuilder(Graph graph, String str, String str2) {
        this.graph = graph;
        Reference ref = graph.ref();
        try {
            this.unsafeNativeHandle = allocate(ref.nativeHandle(), str, str2);
        } finally {
            ref.close();
        }
    }

    public Operation build() {
        Reference ref = this.graph.ref();
        try {
            Operation operation = new Operation(this.graph, finish(this.unsafeNativeHandle));
            this.unsafeNativeHandle = 0;
            return operation;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder addInput(Output output) {
        Reference ref = this.graph.ref();
        try {
            addInput(this.unsafeNativeHandle, output.op().getUnsafeNativeHandle(), output.index());
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder addInputList(Output[] outputArr) {
        Reference ref = this.graph.ref();
        try {
            long[] jArr = new long[outputArr.length];
            int[] iArr = new int[outputArr.length];
            for (int i = 0; i < outputArr.length; i++) {
                jArr[i] = outputArr[i].op().getUnsafeNativeHandle();
                iArr[i] = outputArr[i].index();
            }
            addInputList(this.unsafeNativeHandle, jArr, iArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setDevice(String str) {
        Reference ref = this.graph.ref();
        try {
            setDevice(this.unsafeNativeHandle, str);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, String str2) {
        setAttr(str, str2.getBytes(Charset.forName("UTF-8")));
        return this;
    }

    public OperationBuilder setAttr(String str, byte[] bArr) {
        Reference ref = this.graph.ref();
        try {
            setAttrString(this.unsafeNativeHandle, str, bArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, long j) {
        Reference ref = this.graph.ref();
        try {
            setAttrInt(this.unsafeNativeHandle, str, j);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, long[] jArr) {
        Reference ref = this.graph.ref();
        try {
            setAttrIntList(this.unsafeNativeHandle, str, jArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, float f) {
        Reference ref = this.graph.ref();
        try {
            setAttrFloat(this.unsafeNativeHandle, str, f);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, float[] fArr) {
        Reference ref = this.graph.ref();
        try {
            setAttrFloatList(this.unsafeNativeHandle, str, fArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, boolean z) {
        Reference ref = this.graph.ref();
        try {
            setAttrBool(this.unsafeNativeHandle, str, z);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, boolean[] zArr) {
        Reference ref = this.graph.ref();
        try {
            setAttrBoolList(this.unsafeNativeHandle, str, zArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, DataType dataType) {
        Reference ref = this.graph.ref();
        try {
            setAttrType(this.unsafeNativeHandle, str, dataType.m19c());
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, DataType[] dataTypeArr) {
        int[] iArr = new int[dataTypeArr.length];
        for (int i = 0; i < dataTypeArr.length; i++) {
            iArr[i] = dataTypeArr[i].m19c();
        }
        Reference ref = this.graph.ref();
        try {
            setAttrTypeList(this.unsafeNativeHandle, str, iArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, Tensor tensor) {
        Reference ref = this.graph.ref();
        try {
            setAttrTensor(this.unsafeNativeHandle, str, tensor.getNativeHandle());
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, Tensor[] tensorArr) {
        int i = 0;
        long[] jArr = new long[tensorArr.length];
        int length = tensorArr.length;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            jArr[i2] = tensorArr[i].getNativeHandle();
            i++;
            i2 = i3;
        }
        Reference ref = this.graph.ref();
        try {
            setAttrTensorList(this.unsafeNativeHandle, str, jArr);
            return this;
        } finally {
            ref.close();
        }
    }

    public OperationBuilder setAttr(String str, Shape shape) {
        Reference ref = this.graph.ref();
        try {
            setAttrShape(this.unsafeNativeHandle, str, shape.asArray(), shape.numDimensions());
            return this;
        } finally {
            ref.close();
        }
    }
}
