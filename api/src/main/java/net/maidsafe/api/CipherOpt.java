package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;



public class CipherOpt {

    private static AppHandle appHandle;

    public CipherOpt(AppHandle appHandle) {
        this.appHandle = appHandle;
    }


    private NativeHandle getNativeHandle(long handle) {
        return new NativeHandle(handle, (cipherOpt) -> {
            NativeBindings.cipherOptFree(appHandle.toLong(), cipherOpt, (res) -> {
            });
        });
    }

    public CompletableFuture<NativeHandle> getPlainCipherOpt() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewPlaintext(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getSymmetricCipherOpt() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();

        NativeBindings.cipherOptNewSymmetric(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getAsymmetricCipherOpt(NativeHandle publicEncryptKey) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewAsymmetric(appHandle.toLong(), publicEncryptKey.toLong(),
                (result, handle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(getNativeHandle(handle));
                });
        return future;
    }
}
