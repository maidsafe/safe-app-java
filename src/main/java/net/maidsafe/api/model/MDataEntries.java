package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;

public class MDataEntries {
    private final Pointer appHandle;
    private final long handle;
    private final MutableDataBinding lib = BindingFactory.getInstance()
            .getMutableData();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public MDataEntries(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public interface ForEachCallback {
        void onData(MDataEntry mDataEntry);

        void completed();
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<Long> getLength() {
        final CompletableFuture<Long> future;
        future = new CompletableFuture<>();

        lib.mdata_entries_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(future));

        return future;
    }

    public CompletableFuture<byte[]> getEntries(byte[] key) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        lib.mdata_entries_get(appHandle, handle, key, key.length, Pointer.NULL, callbackHelper.getDataWithVersionCallback(future));

        return future;
    }

    public CompletableFuture<Void> forEachEntry(ForEachCallback eachEntryCallback) {
        final CompletableFuture<Void> future;
        final CompletableFuture<Long> lenFuture;

        future = new CompletableFuture<>();
        lenFuture = new CompletableFuture<>();

        lib.mdata_entries_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(lenFuture));

        lenFuture.thenAccept(size -> lib.mdata_entries_for_each(appHandle, handle, callbackHelper.getForEachEntryCallback(eachEntryCallback, size),
                Pointer.NULL, callbackHelper.getResultCallBack(future))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> insertEntry(byte[] key, byte[] value) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        lib.mdata_entries_insert(appHandle, handle, key, key.length, value, value.length, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<EntryMutationTransaction> mutate() {
        final CompletableFuture<EntryMutationTransaction> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        lib.mdata_entry_actions_new(appHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new EntryMutationTransaction(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.mdata_entries_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }
}
