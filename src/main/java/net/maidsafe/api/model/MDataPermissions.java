package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class MDataPermissions {
    private final Pointer appHandle;
    private final long handle;
    private final MutableDataBinding lib = BindingFactory.getInstance()
            .getMutableData();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public interface ForEachCallback {
        void onData(MDataPermission permission);

        void completed();
    }

    public MDataPermissions(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<Long> getLength() {
        final CompletableFuture<Long> future;
        future = new CompletableFuture<>();

        lib.mdata_permissions_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(future));

        return future;
    }

    public CompletableFuture<MDataPermissionSet> getPermissionSet(PublicSignKey signKey) {
        final CompletableFuture<MDataPermissionSet> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        lib.mdata_permissions_get(appHandle, handle, signKey.getHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataPermissionSet(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> insertPermissionSet(PublicSignKey signKey, MDataPermissionSet MDataPermissionSet) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_permissions_insert(appHandle, handle, signKey.getHandle(), MDataPermissionSet.getHandle(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> forEachPermissionSet(ForEachCallback eachPermissionCallback) {
        final CompletableFuture<Void> future;
        final CompletableFuture<Long> lenFuture;

        future = new CompletableFuture<>();
        lenFuture = new CompletableFuture<>();

        lib.mdata_permissions_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(lenFuture));

        lenFuture.thenAccept(size -> lib.mdata_permissions_for_each(appHandle, handle, callbackHelper.getForEachPermissionsCallback(eachPermissionCallback, size),
                Pointer.NULL, callbackHelper.getResultCallBack(future))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.mdata_permissions_set_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }

}
