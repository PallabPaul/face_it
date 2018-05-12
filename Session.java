package org.tensorflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Session implements AutoCloseable {
    private final Graph graph;
    private final Reference graphRef;
    private long nativeHandle;
    private final Object nativeHandleLock;
    private int numActiveRuns;

    public static final class Run {
        public byte[] metadata;
        public List<Tensor> outputs;
    }

    public final class Runner {
        private ArrayList<Tensor> inputTensors = new ArrayList();
        private ArrayList<Output> inputs = new ArrayList();
        private ArrayList<Output> outputs = new ArrayList();
        private byte[] runOptions = null;
        private ArrayList<Operation> targets = new ArrayList();

        private class Reference implements AutoCloseable {
            public Reference() {
                synchronized (Session.this.nativeHandleLock) {
                    if (Session.this.nativeHandle == 0) {
                        throw new IllegalStateException("run() cannot be called on the Session after close()");
                    }
                    Session.access$304(Session.this);
                }
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void close() {
                /*
                r6 = this;
                r0 = org.tensorflow.Session.Runner.this;
                r0 = org.tensorflow.Session.this;
                r1 = r0.nativeHandleLock;
                monitor-enter(r1);
                r0 = org.tensorflow.Session.Runner.this;	 Catch:{ all -> 0x0030 }
                r0 = org.tensorflow.Session.this;	 Catch:{ all -> 0x0030 }
                r2 = r0.nativeHandle;	 Catch:{ all -> 0x0030 }
                r4 = 0;
                r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
                if (r0 != 0) goto L_0x0019;
            L_0x0017:
                monitor-exit(r1);	 Catch:{ all -> 0x0030 }
            L_0x0018:
                return;
            L_0x0019:
                r0 = org.tensorflow.Session.Runner.this;	 Catch:{ all -> 0x0030 }
                r0 = org.tensorflow.Session.this;	 Catch:{ all -> 0x0030 }
                r0 = org.tensorflow.Session.access$306(r0);	 Catch:{ all -> 0x0030 }
                if (r0 != 0) goto L_0x002e;
            L_0x0023:
                r0 = org.tensorflow.Session.Runner.this;	 Catch:{ all -> 0x0030 }
                r0 = org.tensorflow.Session.this;	 Catch:{ all -> 0x0030 }
                r0 = r0.nativeHandleLock;	 Catch:{ all -> 0x0030 }
                r0.notifyAll();	 Catch:{ all -> 0x0030 }
            L_0x002e:
                monitor-exit(r1);	 Catch:{ all -> 0x0030 }
                goto L_0x0018;
            L_0x0030:
                r0 = move-exception;
                monitor-exit(r1);	 Catch:{ all -> 0x0030 }
                throw r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tensorflow.Session.Runner.Reference.close():void");
            }
        }

        public Runner feed(String str, Tensor tensor) {
            return feed(str, 0, tensor);
        }

        public Runner feed(String str, int i, Tensor tensor) {
            Operation operationByName = operationByName(str);
            if (operationByName != null) {
                this.inputs.add(operationByName.output(i));
                this.inputTensors.add(tensor);
            }
            return this;
        }

        public Runner feed(Output output, Tensor tensor) {
            this.inputs.add(output);
            this.inputTensors.add(tensor);
            return this;
        }

        public Runner fetch(String str) {
            return fetch(str, 0);
        }

        public Runner fetch(String str, int i) {
            Operation operationByName = operationByName(str);
            if (operationByName != null) {
                this.outputs.add(operationByName.output(i));
            }
            return this;
        }

        public Runner fetch(Output output) {
            this.outputs.add(output);
            return this;
        }

        public Runner addTarget(String str) {
            Operation operationByName = operationByName(str);
            if (operationByName != null) {
                this.targets.add(operationByName);
            }
            return this;
        }

        public Runner addTarget(Operation operation) {
            this.targets.add(operation);
            return this;
        }

        public Runner setOptions(byte[] bArr) {
            this.runOptions = bArr;
            return this;
        }

        public List<Tensor> run() {
            return runHelper(false).outputs;
        }

        public Run runAndFetchMetadata() {
            return runHelper(true);
        }

        private Run runHelper(boolean z) {
            int i = 0;
            long[] jArr = new long[this.inputTensors.size()];
            long[] jArr2 = new long[this.inputs.size()];
            int[] iArr = new int[this.inputs.size()];
            long[] jArr3 = new long[this.outputs.size()];
            int[] iArr2 = new int[this.outputs.size()];
            long[] jArr4 = new long[this.targets.size()];
            long[] jArr5 = new long[this.outputs.size()];
            Iterator it = this.inputTensors.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                int i3 = i2 + 1;
                jArr[i2] = ((Tensor) it.next()).getNativeHandle();
                i2 = i3;
            }
            Iterator it2 = this.inputs.iterator();
            i2 = 0;
            while (it2.hasNext()) {
                Output output = (Output) it2.next();
                jArr2[i2] = output.op().getUnsafeNativeHandle();
                iArr[i2] = output.index();
                i2++;
            }
            it2 = this.outputs.iterator();
            i2 = 0;
            while (it2.hasNext()) {
                output = (Output) it2.next();
                jArr3[i2] = output.op().getUnsafeNativeHandle();
                iArr2[i2] = output.index();
                i2++;
            }
            it = this.targets.iterator();
            i2 = 0;
            while (it.hasNext()) {
                i3 = i2 + 1;
                jArr4[i2] = ((Operation) it.next()).getUnsafeNativeHandle();
                i2 = i3;
            }
            Reference reference = new Reference();
            try {
                byte[] access$100 = Session.run(Session.this.nativeHandle, this.runOptions, jArr, jArr2, iArr, jArr3, iArr2, jArr4, z, jArr5);
                List<Tensor> arrayList = new ArrayList();
                i2 = jArr5.length;
                while (i < i2) {
                    try {
                        arrayList.add(Tensor.fromHandle(jArr5[i]));
                        i++;
                    } catch (Exception e) {
                        Exception exception = e;
                        for (Tensor close : arrayList) {
                            close.close();
                        }
                        arrayList.clear();
                        throw exception;
                    }
                }
                Run run = new Run();
                run.outputs = arrayList;
                run.metadata = access$100;
                return run;
            } finally {
                reference.close();
            }
        }

        private Operation operationByName(String str) {
            Operation operation = Session.this.graph.operation(str);
            if (operation != null) {
                return operation;
            }
            throw new IllegalArgumentException("No Operation named [" + str + "] in the Graph");
        }
    }

    private static native long allocate(long j);

    private static native long allocate2(long j, String str, byte[] bArr);

    private static native void delete(long j);

    private static native byte[] run(long j, byte[] bArr, long[] jArr, long[] jArr2, int[] iArr, long[] jArr3, int[] iArr2, long[] jArr4, boolean z, long[] jArr5);

    static /* synthetic */ int access$304(Session session) {
        int i = session.numActiveRuns + 1;
        session.numActiveRuns = i;
        return i;
    }

    static /* synthetic */ int access$306(Session session) {
        int i = session.numActiveRuns - 1;
        session.numActiveRuns = i;
        return i;
    }

    public Session(Graph graph) {
        this(graph, null);
    }

    public Session(Graph graph, byte[] bArr) {
        long allocate;
        this.nativeHandleLock = new Object();
        this.graph = graph;
        Reference ref = graph.ref();
        if (bArr == null) {
            try {
                allocate = allocate(ref.nativeHandle());
            } catch (Throwable th) {
                ref.close();
            }
        } else {
            allocate = allocate2(ref.nativeHandle(), null, bArr);
        }
        this.nativeHandle = allocate;
        this.graphRef = graph.ref();
        ref.close();
    }

    Session(Graph graph, long j) {
        this.nativeHandleLock = new Object();
        this.graph = graph;
        this.nativeHandle = j;
        this.graphRef = graph.ref();
    }

    public void close() {
        this.graphRef.close();
        synchronized (this.nativeHandleLock) {
            if (this.nativeHandle == 0) {
                return;
            }
            while (this.numActiveRuns > 0) {
                try {
                    this.nativeHandleLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            delete(this.nativeHandle);
            this.nativeHandle = 0;
        }
    }

    public Runner runner() {
        return new Runner();
    }
}
