package net.maidsafe.api;

import java.lang.annotation.Native;
import java.net.CookieManager;
import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;



public class Crypto {
    private static AppHandle appHandle;

    public Crypto(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    private static NativeHandle getPublicSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signPubKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    public static CompletableFuture<byte[]> generateNonce() {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.generateNonce((result, nonce) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(nonce);
        });
        return future;
    }

    public static CompletableFuture<byte[]> sha3Hash(byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.sha3Hash(data, (result, hashedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(hashedData);
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getAppPublicSignKey() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.appPubSignKey(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new NativeHandle(handle, (key) -> {
                NativeBindings.signPubKeyFree(appHandle.toLong(), key, (freeResult) -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<SignKeyPair> generateSignKeyPair() {
        CompletableFuture<SignKeyPair> future = new CompletableFuture<>();
        NativeBindings.signGenerateKeyPair(appHandle.toLong(),
                (result, pubSignKeyHandle, secSignKeyHandle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(new SignKeyPair(getPublicSignKeyHandle(pubSignKeyHandle),
                            getSecretSignKeyHandle(secSignKeyHandle)));
                });
        return future;
    }

    public CompletableFuture<NativeHandle> getPublicSignKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicSignKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getSecretSignKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signSecKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getSecretSignKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<EncryptKeyPair> generateEncryptKeyPair() {
        CompletableFuture<EncryptKeyPair> future = new CompletableFuture<>();
        NativeBindings.encGenerateKeyPair(appHandle.toLong(),
                (result, pubEncHandle, secEncHandle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    EncryptKeyPair keyPair = new EncryptKeyPair(getPublicEncKeyHandle(pubEncHandle),
                            getSecretEncKeyHandle(secEncHandle));
                    future.complete(keyPair);
                });
        return future;
    }

    public CompletableFuture<NativeHandle> getAppPublicEncryptKey() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.appPubEncKey(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getPublicEncryptKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getSecretEncryptKey(byte[] key) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getSecretEncKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<byte[]> sign(NativeHandle secretSignKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.sign(appHandle.toLong(), data, secretSignKey.toLong(),
                (result, signedData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(signedData);
                });
        return future;
    }

    public CompletableFuture<byte[]> verify(NativeHandle publicSignKey, byte[] signedData) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.verify(appHandle.toLong(), signedData, publicSignKey.toLong(),
                (result, verifiedData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(verifiedData);
                });
        return future;
    }

    public CompletableFuture<byte[]> encrypt(NativeHandle recipientPublicEncryptKey,
                                             NativeHandle senderSecretEncryptKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encrypt(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(),
                senderSecretEncryptKey.toLong(), (result, cipherText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(cipherText);
                });
        return future;
    }

    public CompletableFuture<byte[]> decrypt(NativeHandle senderPublicEncryptKey,
                                             NativeHandle recipientSecretEncryptKey, byte[] cipherText) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.decrypt(appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(),
                recipientSecretEncryptKey.toLong(), (result, plainData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(plainData);
                });
        return future;
    }

    public CompletableFuture<byte[]> encryptSealedBox(NativeHandle recipientPublicEncryptKey, byte[] data) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encryptSealedBox(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(),
                (result, cipherText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(cipherText);
                });
        return future;
    }

    public CompletableFuture<byte[]> decryptSealedBox(NativeHandle senderPublicEncryptKey,
                                                      NativeHandle senderSecretEncryptKey, byte[] cipherText) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.decryptSealedBox(appHandle.toLong(), cipherText,
                senderPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(),
                (result, plainText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(plainText);
                });
        return future;
    }

    public CompletableFuture<byte[]> getRawPublicEncryptKey(NativeHandle publicEncKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encPubKeyGet(appHandle.toLong(), publicEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }

    public CompletableFuture<byte[]> getRawSecretEncryptKey(NativeHandle secretEncKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyGet(appHandle.toLong(), secretEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }

    public CompletableFuture<byte[]> getRawPublicSignKey(NativeHandle publicSignKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signPubKeyGet(appHandle.toLong(), publicSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }

    public CompletableFuture<byte[]> getRawSecretSignKey(NativeHandle secretSignKey) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signSecKeyGet(appHandle.toLong(), secretSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }

    private NativeHandle getSecretSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signSecKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    private NativeHandle getPublicEncKeyHandle(long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encPubKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }

    private NativeHandle getSecretEncKeyHandle(long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encSecretKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }
}