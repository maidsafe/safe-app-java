package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Created by expertonetechnologies on 07/06/17.
 */
public class MDataPermissionSet {
    private final Pointer appHandle;
    private final long handle;
    private final MutableDataBinding lib = BindingFactory.getInstance()
            .getMutableData();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public MDataPermissionSet(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<Void> allowPermissionSet(Permission permission) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_permissions_set_allow(appHandle, handle, permission.ordinal(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> denyPermissionSet(Permission permission) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_permissions_set_deny(appHandle, handle, permission.ordinal(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> clearPermissions(Permission permission) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_permissions_set_clear(appHandle, handle, permission.ordinal(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.mdata_permissions_set_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }
}
