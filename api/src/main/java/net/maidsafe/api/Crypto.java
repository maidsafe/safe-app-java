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

/***
 * Exposes API for the cryptographic functions
 */
public class Crypto {
    private static AppHandle appHandle;

    /***
     * Initialises the Crypto object
     * @param handle App handle
     */
    public Crypto(final AppHandle handle) {
        init(handle);
    }

    /***
     * Helper function to convert public sign key from long to {@link NativeHandle}
     * @param handle Public sign key handle
     * @return Public sign key as {@link NativeHandle}
     */
    private static NativeHandle getPublicSignKeyHandle(final long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signPubKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    /***
     * Generates a unique nonce
     * @return Unique nonce as byte array
     */
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

    /***
     * Generates a SHA3 hash of the given data
     * @param data Data to be hashed
     * @return Hash of the data as byte array
     */
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

    private void init(final AppHandle handle) {
        this.appHandle = handle;
    }

    /***
     * Get App's public sign key
     * @return App's public sign key as {@link NativeHandle}
     */
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

    /***
     * Generate a new signing key pair
     * @return New {@link SignKeyPair} instance
     */
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

    /***
     * Create a new public sign key from byte array
     * @param key Byte array
     * @return New public sign key {@link NativeHandle} instance
     */
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

    /***
     * Create a new secret signing key from byte array
     * @param key Byte array
     * @return Secret sign key as {@link NativeHandle}
     */
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

    /**
     * Generate a new encryption key pair
     * @return New {@link EncryptKeyPair} instance
     */
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

    /**
     * Get the public encryption key for the App
     * @return App's public encryption key as {@link NativeHandle}
     */
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

    /**
     * Create a new public encryption key from byte array
     * @param key Raw byte array
     * @return New Public encryption key {@link NativeHandle} instance
     */
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

    /**
     * Create a new secret encryption key from a raw array
     * @param key Raw byte array
     * @return New Secret encryption key {@link NativeHandle} instance
     */
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

    /**
     * Sign data using a secret sign key
     * @param secretSignKey Secret sign key
     * @param data Data to be signed
     * @return Signed data as byte array
     */
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

    /**
     * Verifies signed data using the public sign key
     * @param publicSignKey Public sign key
     * @param signedData Signed data
     * @return Verified data
     */
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

    /**
     * Encrypts data with the sender's public encryption key and recipient's private encryption key
     * @param recipientPublicEncryptKey Recipient's private encryption key
     * @param senderSecretEncryptKey Sender's public encryption key
     * @param data Data to be encrypted
     * @return Encrypted data as byte array
     */
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

    /**
     * Decrypts encrypted data using sender's public encryption key and recipient's private encryption key
     * @param senderPublicEncryptKey Sender's public encryption key
     * @param recipientSecretEncryptKey Recipient's private encryption key
     * @param cipherText Encrypted data
     * @return Decrypted data as byte array
     */
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

    /**
     * Encrypts data using private and public encryption keys with a seal
     * @param recipientPublicEncryptKey Recipient's public encryption key
     * @param data Data to be encrypted
     * @return Encrypted data as byte array
     */
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

    /**
     * Decrypts content encrypted by sealed box encryption
     * @param recipientPublicEncryptKey Recipient's public encryption key
     * @param recipientSecretEncryptKey Recipient's secret encryption key
     * @param cipherText Cipher to be decrypted
     * @return Decrypted data as byte array
     */
    public CompletableFuture<byte[]> decryptSealedBox(final NativeHandle recipientPublicEncryptKey,
                                                      final NativeHandle recipientSecretEncryptKey,
                                                      final byte[] cipherText) {
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

    /**
     * Retrieves the public encryption key as byte array
     * @param publicEncKey Public encryption key as {@link NativeHandle}
     * @return Raw public encryption key as byte array
     */
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

    /**
     * Retrieves secret encryption key as byte array
     * @param secretEncKey Secret encryption key as {@link NativeHandle}
     * @return Raw secret encryption key as byte array
     */
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

    /**
     * Retrieves public sign key as byte array
     * @param publicSignKey Public sign key as {@link NativeHandle}
     * @return Raw public sign key as byte array
     */
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

    /**
     * Retrieves secret sign key as byte array
     * @param secretSignKey Secret sign key as {@link NativeHandle}
     * @return Raw secret sign key as byte array
     */
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

    /**
     * Helper function to initialise secret sign key as {@link NativeHandle}
     * @param handle Address of the secret sign key
     * @return Secret sign key as {@link NativeHandle}
     */
    private NativeHandle getSecretSignKeyHandle(final long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signSecKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    /**
     * Helper function to initialise public encryption key as {@link NativeHandle}
     * @param handle Address of the public encryption key
     * @return Public encryption key as {@link NativeHandle}
     */
    private NativeHandle getPublicEncKeyHandle(final long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encPubKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }

    /**
     * Helper function to initialise secret encryption key as {@link NativeHandle}
     * @param handle Address of the secret encryption key
     * @return Secret encryption key as {@link NativeHandle}
     */
    private NativeHandle getSecretEncKeyHandle(final long handle) {
        return new NativeHandle(handle, (encKey) -> {
            NativeBindings.encSecretKeyFree(appHandle.toLong(), encKey, (result) -> {
            });
        });
    }
}
