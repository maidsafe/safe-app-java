package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class MDataKeys {
    private final Pointer appHandle;
    private final long handle;
    private final MutableDataBinding lib = BindingFactory.getInstance()
            .getMutableData();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public MDataKeys(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public interface ForEachCallback {
        void onData(MDataKey mDataKey);

        void completed();
    }

    public CompletableFuture<Long> getLength() {
        final CompletableFuture<Long> future;
        future = new CompletableFuture<>();

        lib.mdata_keys_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(future));

        return future;
    }

    public CompletableFuture<Void> forEachKey(ForEachCallback eachKeyCallback) {
        final CompletableFuture<Void> future;
        final CompletableFuture<Long> lenFuture;

        future = new CompletableFuture<>();
        lenFuture = new CompletableFuture<>();

        lib.mdata_keys_len(appHandle, handle, Pointer.NULL, callbackHelper.getHandleCallBack(lenFuture));

        lenFuture.thenAccept(size -> lib.mdata_keys_for_each(appHandle, handle, callbackHelper.getForEachKeyCallback(eachKeyCallback, size),
                Pointer.NULL, callbackHelper.getResultCallBack(future))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.mdata_keys_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }


}
