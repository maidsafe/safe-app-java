package net.maidsafe.api;

import net.maidsafe.model.EncryptKeyPair;
import net.maidsafe.model.SignKeyPair;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

import java.util.concurrent.CompletableFuture;

public class Crypto {

    public static CompletableFuture<NativeHandle> getAppPublicSignKey() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.appPubSignKey(BaseSession.appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NativeHandle(handle, (key) -> {
                NativeBindings.signPubKeyFree(BaseSession.appHandle.toLong(), key, (freeResult) -> {

                });
            }));
        });
        return future;
    }

    public static CompletableFuture<SignKeyPair> generateSignKeyPair() {
        CompletableFuture<SignKeyPair> future = new CompletableFuture<>();
        NativeBindings.signGenerateKeyPair(BaseSession.appHandle.toLong(), (result, pubSignKeyHandle, secSignKeyHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new SignKeyPair(getPublicSignKeyHandle(pubSignKeyHandle), getSecretSignKeyHandle(secSignKeyHandle)));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getPublicSignKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signPubKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(getPublicSignKeyHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getSecretSignKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signSecKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(getSecretSignKeyHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<EncryptKeyPair> generateEncryptKeyPair() {
        CompletableFuture<EncryptKeyPair> future = new CompletableFuture<>();
        NativeBindings.encGenerateKeyPair(BaseSession.appHandle.toLong(), (result, pubEncHandle, secEncHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new EncryptKeyPair(getPublicEncKeyHandle(pubEncHandle), getSecretEncKeyHandle(secEncHandle)));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getAppPublicEncryptKey() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.appPubEncKey(BaseSession.appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getPublicEncryptKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encPubKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getSecretEncryptKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(getSecretEncKeyHandle(handle));
        });
        return future;
    }

    public static CompletableFuture<byte[]> sign(NativeHandle secretSignKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.sign(BaseSession.appHandle.toLong(), data, secretSignKey.toLong(), (result, signedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(signedData);
        });
        return future;
    }

    public static CompletableFuture<byte[]> verify(NativeHandle publicSignKey, byte[] signedData) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.verify(BaseSession.appHandle.toLong(), signedData, publicSignKey.toLong(), (result, verifiedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(verifiedData);
        });
        return future;
    }

    public static CompletableFuture<byte[]> encrypt(NativeHandle recipientPublicEncryptKey, NativeHandle senderSecretEncryptKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encrypt(BaseSession.appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(), (result, cipherText) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(cipherText);
        });
        return future;
    }

    public static CompletableFuture<byte[]> decrypt(NativeHandle senderPublicEncryptKey, NativeHandle recipientSecretEncryptKey, byte[] cipherText) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.decrypt(BaseSession.appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(), recipientSecretEncryptKey.toLong(), (result, plainData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(plainData);
        });
        return future;
    }

    public static CompletableFuture<byte[]> encryptSealedBox(NativeHandle recipientPublicEncryptKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encryptSealedBox(BaseSession.appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), (result, cipherText) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(cipherText);
        });
        return future;
    }

    public static CompletableFuture<byte[]> decryptSealedBox(NativeHandle senderSecretEncryptKey, byte[] cipherText) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encryptSealedBox(BaseSession.appHandle.toLong(), cipherText, senderSecretEncryptKey.toLong(), (result, plainText) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(plainText);
        });
        return future;
    }

    public static CompletableFuture<byte[]> getPublicEncryptKey(NativeHandle publicEncKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encPubKeyGet(BaseSession.appHandle.toLong(), publicEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(key);
        });
        return future;
    }

    public static CompletableFuture<byte[]> getSecretEncryptKey(NativeHandle secretEncKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyGet(BaseSession.appHandle.toLong(), secretEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(key);
        });
        return future;
    }


    public static CompletableFuture<byte[]> getPublicSignKey(NativeHandle publicSignKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signPubKeyGet(BaseSession.appHandle.toLong(), publicSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(key);
        });
        return future;
    }

    public static CompletableFuture<byte[]> getSecretSignKey(NativeHandle secretSignKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signSecKeyGet(BaseSession.appHandle.toLong(), secretSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(key);
        });
        return future;
    }

    private static NativeHandle getPublicSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signPubKeyFree(BaseSession.appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    private static NativeHandle getSecretSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signSecKeyFree(BaseSession.appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    private static NativeHandle getPublicEncKeyHandle(long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encPubKeyFree(BaseSession.appHandle.toLong(), encKey, (result) -> {
            });
        });
    }

    private static NativeHandle getSecretEncKeyHandle(long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encSecretKeyFree(BaseSession.appHandle.toLong(), encKey, (result) -> {
            });
        });
    }
}