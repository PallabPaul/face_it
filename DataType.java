package org.tensorflow;

public enum DataType {
    FLOAT(1),
    DOUBLE(2),
    INT32(3),
    UINT8(4),
    STRING(7),
    INT64(9),
    BOOL(10);
    
    private final int value;

    private DataType(int i) {
        this.value = i;
    }

    int m19c() {
        return this.value;
    }

    static DataType fromC(int i) {
        for (DataType dataType : values()) {
            if (dataType.m19c() == i) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("DataType " + i + " is not recognized in Java (version " + TensorFlow.version() + ")");
    }
}
