package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class MDataEntryActions {
    private final App app;
    private final MutableDataBinding mDataBinding;
    private final CallbackHelper callbackHelper;
    private final long handle;
    private final long mDataEntriesHandle;

    public MDataEntryActions(App app, MutableDataBinding mDataBinding, CallbackHelper callbackHelper, long handle, long mDataEntriesHandle) {
        this.app = app;
        this.mDataBinding = mDataBinding;
        this.callbackHelper = callbackHelper;
        this.handle = handle;
        this.mDataEntriesHandle = mDataEntriesHandle;
    }

    public CompletableFuture<MutableData> newEntry() {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_entry_actions_new(app.getAppHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> insertEntry(byte[] key, byte[] value) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_entry_actions_insert(app.getAppHandle(), handle, key, key.length, value, value.length, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> updateEntry(byte[] key, byte[] value, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_entry_actions_update(app.getAppHandle(), handle, key, key.length, value, value.length, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> deleteEntry(byte[] key) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();


        mDataBinding.mdata_entry_actions_delete(app.getAppHandle(), handle, key, key.length, Pointer.NULL, callbackHelper.getResultCallBack(future));
        return future;
    }

    public CompletableFuture<Void> freeActions(){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_entry_actions_free(app.getAppHandle(), handle, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }
}
