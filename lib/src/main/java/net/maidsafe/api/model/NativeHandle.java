package net.maidsafe.api.model;

import net.maidsafe.utils.IFreeFunc;

public class NativeHandle {
    private long handle;
    private IFreeFunc freeFunc;

    public NativeHandle(long handle, IFreeFunc freeFunc) {
        this.handle = handle;
        this.freeFunc = freeFunc;
    }

    public long toLong() {
        return handle;
    }

    @Override
    protected void finalize() throws Throwable {
        freeFunc.free(handle);
        super.finalize();
    }
}
