package net.maidsafe.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.*;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.FfiConstant;

public class Crypto {

    private App app;
    private CryptoBinding lib = BindingFactory.getInstance().getCrypto();
    private CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public Crypto(App app) {
        this.app = app;
    }

    public CompletableFuture<PublicSignKey> getAppPublicSignKey() {
        final CompletableFuture<PublicSignKey> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();
        lib.app_pub_sign_key(app.getAppHandle(), Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new PublicSignKey(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<PublicSignKey> getPublicSignKey(byte[] raw) {
        final CompletableFuture<PublicSignKey> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        if (raw == null || raw.length != FfiConstant.SIGN_PUBLICKEYBYTES) {
            future.completeExceptionally(new Exception(
                    CustomError.INVALID_SIGN_PUB_KEY_SIZE));
            return future;
        }
        cbFuture = new CompletableFuture<>();
        lib.sign_key_new(app.getAppHandle(), raw, Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new PublicSignKey(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<PublicEncryptKey> getAppPublicEncryptKey() {
        final CompletableFuture<PublicEncryptKey> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();
        lib.app_pub_enc_key(app.getAppHandle(), Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new PublicEncryptKey(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<PublicEncryptKey> getPublicEncryptKey(byte[] raw) {
        final CompletableFuture<PublicEncryptKey> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        if (raw == null || raw.length != FfiConstant.BOX_PUBLICKEYBYTES) {
            future.completeExceptionally(new Exception(
                    CustomError.INVALID_BOX_PUB_KEY_SIZE));
            return future;
        }
        cbFuture = new CompletableFuture<>();
        lib.enc_pub_key_new(app.getAppHandle(), raw, Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new PublicEncryptKey(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<SecretEncryptKey> getSecretEncryptKey(byte[] raw) {
        final CompletableFuture<SecretEncryptKey> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();
        lib.enc_secret_key_new(app.getAppHandle(), raw, Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new SecretEncryptKey(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    // generate keys
    public CompletableFuture<EncryptKeyPair> generateEncryptKeyPair() {
        final CompletableFuture<EncryptKeyPair> future;
        final CompletableFuture<List<Long>> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();
        lib.enc_generate_key_pair(app.getAppHandle(), Pointer.NULL,
                callbackHelper.getTwoHandleCallBack(cbFuture));
        cbFuture.thenAccept(handles -> {
            PublicEncryptKey publicEncryptKey = new PublicEncryptKey(app.getAppHandle(), handles.get(0));
            SecretEncryptKey secretKey = new SecretEncryptKey(app.getAppHandle(), handles.get(1));
            future.complete(new EncryptKeyPair(app.getAppHandle(), secretKey, publicEncryptKey));
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<byte[]> hashSHA3(byte[] data) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();
        lib.sha3_hash(data, data.length, Pointer.NULL, callbackHelper.getDataCallback(future));
        return future;
    }

    public CompletableFuture<byte[]> generateNonce() {
        final CompletableFuture<byte[]> future;
        final CompletableFuture<Pointer> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        lib.generate_nonce(Pointer.NULL, callbackHelper.getPointerCallback(cbFuture));

        cbFuture.thenAccept(pointer -> {
            future.complete(pointer.getByteArray(0, FfiConstant.NONCE));
        }).exceptionally(e -> {
           future.completeExceptionally(e);
           return null;
        });

        return future;
    }

}