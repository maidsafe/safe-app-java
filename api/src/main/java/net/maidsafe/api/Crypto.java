// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;



public class Crypto {
    private static AppHandle appHandle;

    public Crypto(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    private static NativeHandle getPublicSignKeyHandle(final long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signPubKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    public static CompletableFuture<byte[]> generateNonce() {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.generateNonce((result, nonce) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(nonce);
        });
        return future;
    }

    public static CompletableFuture<byte[]> sha3Hash(final byte[] data) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.sha3Hash(data, (result, hashedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(hashedData);
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getAppPublicSignKey() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
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
        final CompletableFuture<SignKeyPair> future = new CompletableFuture<>();
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

    public CompletableFuture<NativeHandle> getPublicSignKey(final byte[] key) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicSignKeyHandle(handle));
        });
        return future;
    }


    public CompletableFuture<NativeHandle> getSecretSignKey(final byte[] key) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.signSecKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getSecretSignKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<EncryptKeyPair> generateEncryptKeyPair() {
        final CompletableFuture<EncryptKeyPair> future = new CompletableFuture<>();
        NativeBindings.encGenerateKeyPair(appHandle.toLong(),
                (result, pubEncHandle, secEncHandle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    final EncryptKeyPair keyPair = new EncryptKeyPair(getPublicEncKeyHandle(pubEncHandle),
                            getSecretEncKeyHandle(secEncHandle));
                    future.complete(keyPair);
                });
        return future;
    }

    public CompletableFuture<NativeHandle> getAppPublicEncryptKey() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.appPubEncKey(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }


    public CompletableFuture<NativeHandle> getPublicEncryptKey(final byte[] key) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getPublicEncKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getSecretEncryptKey(final byte[] key) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyNew(appHandle.toLong(), key, (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getSecretEncKeyHandle(handle));
        });
        return future;
    }

    public CompletableFuture<byte[]> sign(final NativeHandle secretSignKey, final byte[] data) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.sign(appHandle.toLong(), data, secretSignKey.toLong(),
                (result, signedData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(signedData);
                });
        return future;
    }


    public CompletableFuture<byte[]> verify(final NativeHandle publicSignKey, final byte[] signedData) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.verify(appHandle.toLong(), signedData, publicSignKey.toLong(),
                (result, verifiedData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(verifiedData);
                });
        return future;
    }

    public CompletableFuture<byte[]> encrypt(final NativeHandle recipientPublicEncryptKey,
                                             final NativeHandle senderSecretEncryptKey, final byte[] data) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encrypt(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(),
                senderSecretEncryptKey.toLong(), (result, cipherText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(cipherText);
                });
        return future;
    }


    public CompletableFuture<byte[]> decrypt(final NativeHandle senderPublicEncryptKey,
                                             final NativeHandle recipientSecretEncryptKey, final byte[] cipherText) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.decrypt(appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(),
                recipientSecretEncryptKey.toLong(), (result, plainData) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(plainData);
                });
        return future;
    }

    public CompletableFuture<byte[]> encryptSealedBox(final NativeHandle recipientPublicEncryptKey, final byte[] data) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encryptSealedBox(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(),
                (result, cipherText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(cipherText);
                });
        return future;
    }


    public CompletableFuture<byte[]> decryptSealedBox(final NativeHandle recipientPublicEncryptKey,
                                                      final NativeHandle recipientSecretEncryptKey, final byte[] cipherText) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.decryptSealedBox(appHandle.toLong(), cipherText,
                recipientPublicEncryptKey.toLong(), recipientSecretEncryptKey.toLong(),
                (result, plainText) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(plainText);
                });
        return future;
    }


    public CompletableFuture<byte[]> getRawPublicEncryptKey(final NativeHandle publicEncKey) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encPubKeyGet(appHandle.toLong(), publicEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }

    public CompletableFuture<byte[]> getRawSecretEncryptKey(final NativeHandle secretEncKey) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.encSecretKeyGet(appHandle.toLong(), secretEncKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }


    public CompletableFuture<byte[]> getRawPublicSignKey(final NativeHandle publicSignKey) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signPubKeyGet(appHandle.toLong(), publicSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }


    public CompletableFuture<byte[]> getRawSecretSignKey(final NativeHandle secretSignKey) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.signSecKeyGet(appHandle.toLong(), secretSignKey.toLong(), (result, key) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(key);
        });
        return future;
    }


    private NativeHandle getSecretSignKeyHandle(final long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signSecKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }


    private NativeHandle getPublicEncKeyHandle(final long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encPubKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }


    private NativeHandle getSecretEncKeyHandle(final long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encSecretKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }
}