package org.tensorflow;

public final class Graph implements AutoCloseable {
    private long nativeHandle;
    private final Object nativeHandleLock;
    private int refcount;

    class Reference implements AutoCloseable {
        private boolean active;
        final /* synthetic */ Graph this$0;

        private Reference(Graph graph) {
            boolean z = true;
            this.this$0 = graph;
            synchronized (graph.nativeHandleLock) {
                if (graph.nativeHandle == 0) {
                    z = false;
                }
                this.active = z;
                if (this.active) {
                    this.active = true;
                    graph.refcount = graph.refcount + 1;
                } else {
                    throw new IllegalStateException("close() has been called on the Graph");
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close() {
            /*
            r2 = this;
            r0 = r2.this$0;
            r1 = r0.nativeHandleLock;
            monitor-enter(r1);
            r0 = r2.active;	 Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        L_0x000c:
            return;
        L_0x000d:
            r0 = 0;
            r2.active = r0;	 Catch:{ all -> 0x0023 }
            r0 = r2.this$0;	 Catch:{ all -> 0x0023 }
            r0 = org.tensorflow.Graph.access$206(r0);	 Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x0021;
        L_0x0018:
            r0 = r2.this$0;	 Catch:{ all -> 0x0023 }
            r0 = r0.nativeHandleLock;	 Catch:{ all -> 0x0023 }
            r0.notifyAll();	 Catch:{ all -> 0x0023 }
        L_0x0021:
            monitor-exit(r1);	 Catch:{ all -> 0x0023 }
            goto L_0x000c;
        L_0x0023:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0023 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tensorflow.Graph.Reference.close():void");
        }

        public long nativeHandle() {
            long access$100;
            synchronized (this.this$0.nativeHandleLock) {
                access$100 = this.active ? this.this$0.nativeHandle : 0;
            }
            return access$100;
        }
    }

    private static native long allocate();

    private static native void delete(long j);

    private static native void importGraphDef(long j, byte[] bArr, String str) throws IllegalArgumentException;

    private static native long operation(long j, String str);

    private static native byte[] toGraphDef(long j);

    static /* synthetic */ int access$206(Graph graph) {
        int i = graph.refcount - 1;
        graph.refcount = i;
        return i;
    }

    public Graph() {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = allocate();
    }

    Graph(long j) {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = j;
    }

    public void close() {
        synchronized (this.nativeHandleLock) {
            if (this.nativeHandle == 0) {
                return;
            }
            while (this.refcount > 0) {
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

    public Operation operation(String str) {
        Operation operation;
        synchronized (this.nativeHandleLock) {
            long operation2 = operation(this.nativeHandle, str);
            if (operation2 == 0) {
                operation = null;
            } else {
                operation = new Operation(this, operation2);
            }
        }
        return operation;
    }

    public OperationBuilder opBuilder(String str, String str2) {
        return new OperationBuilder(this, str, str2);
    }

    public void importGraphDef(byte[] bArr) throws IllegalArgumentException {
        importGraphDef(bArr, "");
    }

    public void importGraphDef(byte[] bArr, String str) throws IllegalArgumentException {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("graphDef and prefix cannot be null");
        }
        synchronized (this.nativeHandleLock) {
            importGraphDef(this.nativeHandle, bArr, str);
        }
    }

    public byte[] toGraphDef() {
        byte[] toGraphDef;
        synchronized (this.nativeHandleLock) {
            toGraphDef = toGraphDef(this.nativeHandle);
        }
        return toGraphDef;
    }

    Reference ref() {
        return new Reference();
    }

    static {
        TensorFlow.init();
    }
}
