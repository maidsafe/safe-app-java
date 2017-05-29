package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class PublicSignKey {

    private long handle;
    private CryptoBinding lib = BindingFactory.getInstance().getCrypto();
    private Pointer appHandle;
    private CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public PublicSignKey(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<byte[]> getRaw() {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        final CompletableFuture<Pointer> cbFuture = new CompletableFuture<>();
        lib.sign_key_get(appHandle, handle, Pointer.NULL,
                callbackHelper.getPointerCallback(cbFuture));
        cbFuture.thenAccept(pointer -> {
            future.complete(pointer.getByteArray(0,
                    FfiConstant.SIGN_PUBLICKEYBYTES));
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.sign_key_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }

}
