package net.maidsafe.api;

import net.maidsafe.safe_app.NativeBindings;

import java.util.concurrent.CompletableFuture;

public class CipherOpt {

    private static NativeHandle getNativeHandle(long handle) {
        return new NativeHandle(handle, (cipherOpt) -> {
            NativeBindings.cipherOptFree(BaseSession.appHandle.toLong(), cipherOpt, (res) -> {
            });
        });
    }

    public static CompletableFuture<NativeHandle> getPlainCipherOpt() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewPlaintext(BaseSession.appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                return;
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getSymmetricCipherOpt() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewSymmetric(BaseSession.appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                return;
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getAsymmetricCipherOpt(NativeHandle publicEncryptKey) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewAsymmetric(BaseSession.appHandle.toLong(), publicEncryptKey.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                return;
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }
}
