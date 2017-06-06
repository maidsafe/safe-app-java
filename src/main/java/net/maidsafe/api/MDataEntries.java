package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class MDataEntries {
    private final App app;
    private final MutableDataBinding mDataBinding;
    private final CallbackHelper callbackHelper;
    private final long mDataEntriesHandle;

    public MDataEntries(App app, MutableDataBinding mDataBinding, CallbackHelper callbackHelper, long mDataEntriesHandle) {
        this.app = app;
        this.mDataBinding = mDataBinding;
        this.callbackHelper = callbackHelper;
        this.mDataEntriesHandle = mDataEntriesHandle;
    }

    public CompletableFuture<MutableData> newEntries(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_entries_new(app.getAppHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> insertEntries(byte[] key, byte[] value){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_entries_insert(app.getAppHandle(), mDataEntriesHandle, key, key.length, value, value.length, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<MutableData> getEntriesLength(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_entries_len(app.getAppHandle(), mDataEntriesHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return  future;
    }

    public CompletableFuture<byte[]> getEntries(byte[] key){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_entries_get(app.getAppHandle(), mDataEntriesHandle, key, key.length, Pointer.NULL, callbackHelper.getDataWithVersionCallback(future));

        return future;
    }

    public CompletableFuture<MutableData> getKeysLength(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_keys_len(app.getAppHandle(), mDataEntriesHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    //TODO: Implement mdata_keys_for_each

    public CompletableFuture<MutableData> getValuesLength(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_values_len(app.getAppHandle(), mDataEntriesHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    //TODO: Implement mdata_values_for_each

}
