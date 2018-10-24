package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;

class AppHandle extends NativeHandle {

    AppHandle(final long handle) {
        super(handle, h -> NativeBindings.appFree(h));
    }

    public void invalidate() {
        if (handle > 0) {
            NativeBindings.appFree(handle);
            this.handle = -1;
        }
    }

    @Override
    public long toLong() {
        if (handle < 0) {
            throw new java.lang.RuntimeException("Session Invalidated");
        }
        return handle;
    }

}
