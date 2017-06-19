package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;

public class EntryMutationTransaction {
    private final Pointer appHandle;
    private final long handle;
    private final MutableDataBinding lib = BindingFactory.getInstance()
            .getMutableData();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public EntryMutationTransaction(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<Void> insert(byte[] key, byte[] value) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_entry_actions_insert(appHandle, handle, key, key.length, value, value.length, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> remove(byte[] key, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_entry_actions_delete(appHandle, handle, key, key.length, version,Pointer.NULL,callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> update(byte[] key, byte[] value, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_entry_actions_update(appHandle, handle, key, key.length, value, value.length, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.mdata_entry_actions_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }
}
