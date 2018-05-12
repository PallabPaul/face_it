package org.tensorflow;

public final class Output {
    private final int index;
    private final Operation operation;

    public Output(Operation operation, int i) {
        this.operation = operation;
        this.index = i;
    }

    public Operation op() {
        return this.operation;
    }

    public int index() {
        return this.index;
    }

    public Shape shape() {
        return new Shape(this.operation.shape(this.index));
    }

    public DataType dataType() {
        return this.operation.dtype(this.index);
    }
}
