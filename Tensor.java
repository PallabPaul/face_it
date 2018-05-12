package org.tensorflow;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;

public final class Tensor implements AutoCloseable {
    private DataType dtype;
    private long nativeHandle;
    private long[] shapeCopy = null;

    private static native long allocate(int i, long[] jArr, long j);

    private static native long allocateScalarBytes(byte[] bArr);

    private static native ByteBuffer buffer(long j);

    private static native void delete(long j);

    private static native int dtype(long j);

    private static native void readNDArray(long j, Object obj);

    private static native boolean scalarBoolean(long j);

    private static native byte[] scalarBytes(long j);

    private static native double scalarDouble(long j);

    private static native float scalarFloat(long j);

    private static native int scalarInt(long j);

    private static native long scalarLong(long j);

    private static native void setValue(long j, Object obj);

    private static native long[] shape(long j);

    public static Tensor create(Object obj) {
        Tensor tensor = new Tensor();
        tensor.dtype = dataTypeOf(obj);
        tensor.shapeCopy = new long[numDimensions(obj)];
        fillShape(obj, 0, tensor.shapeCopy);
        if (tensor.dtype != DataType.STRING) {
            tensor.nativeHandle = allocate(tensor.dtype.m19c(), tensor.shapeCopy, (long) (elemByteSize(tensor.dtype) * numElements(tensor.shapeCopy)));
            setValue(tensor.nativeHandle, obj);
        } else if (tensor.shapeCopy.length != 0) {
            throw new UnsupportedOperationException(String.format("non-scalar DataType.STRING tensors are not supported yet (version %s). Please file a feature request at https://github.com/tensorflow/tensorflow/issues/new", new Object[]{TensorFlow.version()}));
        } else {
            tensor.nativeHandle = allocateScalarBytes((byte[]) obj);
        }
        return tensor;
    }

    public static Tensor create(long[] jArr, IntBuffer intBuffer) {
        Tensor allocateForBuffer = allocateForBuffer(DataType.INT32, jArr, intBuffer.remaining());
        allocateForBuffer.buffer().asIntBuffer().put(intBuffer);
        return allocateForBuffer;
    }

    public static Tensor create(long[] jArr, FloatBuffer floatBuffer) {
        Tensor allocateForBuffer = allocateForBuffer(DataType.FLOAT, jArr, floatBuffer.remaining());
        allocateForBuffer.buffer().asFloatBuffer().put(floatBuffer);
        return allocateForBuffer;
    }

    public static Tensor create(long[] jArr, DoubleBuffer doubleBuffer) {
        Tensor allocateForBuffer = allocateForBuffer(DataType.DOUBLE, jArr, doubleBuffer.remaining());
        allocateForBuffer.buffer().asDoubleBuffer().put(doubleBuffer);
        return allocateForBuffer;
    }

    public static Tensor create(long[] jArr, LongBuffer longBuffer) {
        Tensor allocateForBuffer = allocateForBuffer(DataType.INT64, jArr, longBuffer.remaining());
        allocateForBuffer.buffer().asLongBuffer().put(longBuffer);
        return allocateForBuffer;
    }

    public static Tensor create(DataType dataType, long[] jArr, ByteBuffer byteBuffer) {
        int elemByteSize;
        if (dataType != DataType.STRING) {
            elemByteSize = elemByteSize(dataType);
            if (byteBuffer.remaining() % elemByteSize != 0) {
                throw new IllegalArgumentException(String.format("ByteBuffer with %d bytes is not compatible with a %s Tensor (%d bytes/element)", new Object[]{Integer.valueOf(byteBuffer.remaining()), dataType.toString(), Integer.valueOf(elemByteSize)}));
            }
            elemByteSize = byteBuffer.remaining() / elemByteSize;
        } else {
            elemByteSize = byteBuffer.remaining();
        }
        Tensor allocateForBuffer = allocateForBuffer(dataType, jArr, elemByteSize);
        allocateForBuffer.buffer().put(byteBuffer);
        return allocateForBuffer;
    }

    private static Tensor allocateForBuffer(DataType dataType, long[] jArr, int i) {
        int numElements = numElements(jArr);
        if (dataType != DataType.STRING) {
            if (i != numElements) {
                throw incompatibleBuffer(i, jArr);
            }
            i = numElements * elemByteSize(dataType);
        }
        Tensor tensor = new Tensor();
        tensor.dtype = dataType;
        tensor.shapeCopy = Arrays.copyOf(jArr, jArr.length);
        tensor.nativeHandle = allocate(tensor.dtype.m19c(), tensor.shapeCopy, (long) i);
        return tensor;
    }

    public void close() {
        if (this.nativeHandle != 0) {
            delete(this.nativeHandle);
            this.nativeHandle = 0;
        }
    }

    public DataType dataType() {
        return this.dtype;
    }

    public int numDimensions() {
        return this.shapeCopy.length;
    }

    public int numBytes() {
        return buffer().remaining();
    }

    public int numElements() {
        return numElements(this.shapeCopy);
    }

    public long[] shape() {
        return this.shapeCopy;
    }

    public float floatValue() {
        return scalarFloat(this.nativeHandle);
    }

    public double doubleValue() {
        return scalarDouble(this.nativeHandle);
    }

    public int intValue() {
        return scalarInt(this.nativeHandle);
    }

    public long longValue() {
        return scalarLong(this.nativeHandle);
    }

    public boolean booleanValue() {
        return scalarBoolean(this.nativeHandle);
    }

    public byte[] bytesValue() {
        return scalarBytes(this.nativeHandle);
    }

    public <T> T copyTo(T t) {
        throwExceptionIfTypeIsIncompatible(t);
        readNDArray(this.nativeHandle, t);
        return t;
    }

    public void writeTo(IntBuffer intBuffer) {
        if (this.dtype != DataType.INT32) {
            throw incompatibleBuffer((Buffer) intBuffer, this.dtype);
        }
        intBuffer.put(buffer().asIntBuffer());
    }

    public void writeTo(FloatBuffer floatBuffer) {
        if (this.dtype != DataType.FLOAT) {
            throw incompatibleBuffer((Buffer) floatBuffer, this.dtype);
        }
        floatBuffer.put(buffer().asFloatBuffer());
    }

    public void writeTo(DoubleBuffer doubleBuffer) {
        if (this.dtype != DataType.DOUBLE) {
            throw incompatibleBuffer((Buffer) doubleBuffer, this.dtype);
        }
        doubleBuffer.put(buffer().asDoubleBuffer());
    }

    public void writeTo(LongBuffer longBuffer) {
        if (this.dtype != DataType.INT64) {
            throw incompatibleBuffer((Buffer) longBuffer, this.dtype);
        }
        longBuffer.put(buffer().asLongBuffer());
    }

    public void writeTo(ByteBuffer byteBuffer) {
        byteBuffer.put(buffer());
    }

    public String toString() {
        return String.format("%s tensor with shape %s", new Object[]{this.dtype.toString(), Arrays.toString(shape())});
    }

    static Tensor fromHandle(long j) {
        Tensor tensor = new Tensor();
        tensor.dtype = DataType.fromC(dtype(j));
        tensor.shapeCopy = shape(j);
        tensor.nativeHandle = j;
        return tensor;
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    private Tensor() {
    }

    private ByteBuffer buffer() {
        return buffer(this.nativeHandle).order(ByteOrder.nativeOrder());
    }

    private static IllegalArgumentException incompatibleBuffer(Buffer buffer, DataType dataType) {
        return new IllegalArgumentException(String.format("cannot use %s with Tensor of type %s", new Object[]{buffer.getClass().getName(), dataType}));
    }

    private static IllegalArgumentException incompatibleBuffer(int i, long[] jArr) {
        return new IllegalArgumentException(String.format("buffer with %d elements is not compatible with a Tensor with shape %s", new Object[]{Integer.valueOf(i), Arrays.toString(jArr)}));
    }

    private static int numElements(long[] jArr) {
        int i = 1;
        for (long j : jArr) {
            i = (int) (((long) i) * j);
        }
        return i;
    }

    private static int elemByteSize(DataType dataType) {
        switch (dataType) {
            case UINT8:
            case BOOL:
                return 1;
            case FLOAT:
            case INT32:
                return 4;
            case DOUBLE:
            case INT64:
                return 8;
            case STRING:
                throw new IllegalArgumentException("STRING tensors do not have a fixed element size");
            default:
                throw new IllegalArgumentException("DataType " + dataType + " is not supported yet");
        }
    }

    private static DataType dataTypeOf(Object obj) {
        if (obj.getClass().isArray()) {
            if (Array.getLength(obj) == 0) {
                throw new IllegalArgumentException("cannot create Tensors with a 0 dimension");
            }
            Object obj2 = Array.get(obj, 0);
            if (Byte.class.isInstance(obj2) || Byte.TYPE.isInstance(obj2)) {
                return DataType.STRING;
            }
            return dataTypeOf(obj2);
        } else if (Float.class.isInstance(obj) || Float.TYPE.isInstance(obj)) {
            return DataType.FLOAT;
        } else {
            if (Double.class.isInstance(obj) || Double.TYPE.isInstance(obj)) {
                return DataType.DOUBLE;
            }
            if (Integer.class.isInstance(obj) || Integer.TYPE.isInstance(obj)) {
                return DataType.INT32;
            }
            if (Long.class.isInstance(obj) || Long.TYPE.isInstance(obj)) {
                return DataType.INT64;
            }
            if (Boolean.class.isInstance(obj) || Boolean.TYPE.isInstance(obj)) {
                return DataType.BOOL;
            }
            throw new IllegalArgumentException("cannot create Tensors of " + obj.getClass().getName());
        }
    }

    private static int numDimensions(Object obj) {
        if (!obj.getClass().isArray()) {
            return 0;
        }
        Object obj2 = Array.get(obj, 0);
        if (Byte.class.isInstance(obj2) || Byte.TYPE.isInstance(obj2)) {
            return 0;
        }
        return numDimensions(obj2) + 1;
    }

    private static void fillShape(Object obj, int i, long[] jArr) {
        int i2 = 0;
        if (jArr != null && i != jArr.length) {
            int length = Array.getLength(obj);
            if (jArr[i] == 0) {
                jArr[i] = (long) length;
            } else if (jArr[i] != ((long) length)) {
                throw new IllegalArgumentException(String.format("mismatched lengths (%d and %d) in dimension %d", new Object[]{Long.valueOf(jArr[i]), Integer.valueOf(length), Integer.valueOf(i)}));
            }
            while (i2 < length) {
                fillShape(Array.get(obj, i2), i + 1, jArr);
                i2++;
            }
        }
    }

    private void throwExceptionIfTypeIsIncompatible(Object obj) {
        if (numDimensions(obj) != numDimensions()) {
            throw new IllegalArgumentException(String.format("cannot copy Tensor with %d dimensions into an object with %d", new Object[]{Integer.valueOf(numDimensions()), Integer.valueOf(numDimensions(obj))}));
        } else if (dataTypeOf(obj) != this.dtype) {
            throw new IllegalArgumentException(String.format("cannot copy Tensor with DataType %s into an object of type %s", new Object[]{this.dtype.toString(), obj.getClass().getName()}));
        } else {
            long[] jArr = new long[numDimensions()];
            fillShape(obj, 0, jArr);
            for (int i = 0; i < jArr.length; i++) {
                if (jArr[i] != shape()[i]) {
                    throw new IllegalArgumentException(String.format("cannot copy Tensor with shape %s into object with shape %s", new Object[]{Arrays.toString(shape()), Arrays.toString(jArr)}));
                }
            }
        }
    }

    static {
        TensorFlow.init();
    }
}
