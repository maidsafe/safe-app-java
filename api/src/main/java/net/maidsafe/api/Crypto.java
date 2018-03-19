package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

class Crypto {
    private static AppHandle appHandle;

    public Crypto(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<NativeHandle> getAppPublicSignKey() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.appPubSignKey(appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(handle, (key) -> {
                    NativeBindings.signPubKeyFree(appHandle.toLong(), key, (freeResult) -> {

                    });
                }));
            });
        }));
    }

    public Future<SignKeyPair> generateSignKeyPair() {
        return Executor.getInstance().submit(new CallbackHelper<SignKeyPair>(binder -> {
            NativeBindings.signGenerateKeyPair(appHandle.toLong(), (result, pubSignKeyHandle, secSignKeyHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new SignKeyPair(getPublicSignKeyHandle(pubSignKeyHandle), getSecretSignKeyHandle(secSignKeyHandle)));
            });
        }));
    }

    public Future<NativeHandle> getPublicSignKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.signPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicSignKeyHandle(handle));
            });
        }));
    }

    public Future<NativeHandle> getSecretSignKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.signSecKeyNew(appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getSecretSignKeyHandle(handle));
            });
        }));
    }

    public Future<EncryptKeyPair> generateEncryptKeyPair() {
        return Executor.getInstance().submit(new CallbackHelper<EncryptKeyPair>(binder -> {
            NativeBindings.encGenerateKeyPair(appHandle.toLong(), (result, pubEncHandle, secEncHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                EncryptKeyPair keyPair = new EncryptKeyPair(getPublicEncKeyHandle(pubEncHandle), getSecretEncKeyHandle(secEncHandle));
                binder.onResult(keyPair);
            });
        }));
    }

    public Future<NativeHandle> getAppPublicEncryptKey() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.appPubEncKey(appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicEncKeyHandle(handle));
            });
        }));
    }

    public Future<NativeHandle> getPublicEncryptKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.encPubKeyNew(appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicEncKeyHandle(handle));
            });
        }));
    }

    public Future<NativeHandle> getSecretEncryptKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.encSecretKeyNew(appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getSecretEncKeyHandle(handle));
            });
        }));
    }

    public Future<byte[]> sign(NativeHandle secretSignKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.sign(appHandle.toLong(), data, secretSignKey.toLong(), (result, signedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(signedData);
            });
        }));
    }

    public Future<byte[]> verify(NativeHandle publicSignKey, byte[] signedData) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.verify(appHandle.toLong(), signedData, publicSignKey.toLong(), (result, verifiedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(verifiedData);
            });
        }));
    }

    public Future<byte[]> encrypt(NativeHandle recipientPublicEncryptKey, NativeHandle senderSecretEncryptKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encrypt(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(), (result, cipherText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(cipherText);
            });
        }));
    }

    public Future<byte[]> decrypt(NativeHandle senderPublicEncryptKey, NativeHandle recipientSecretEncryptKey, byte[] cipherText) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.decrypt(appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(), recipientSecretEncryptKey.toLong(), (result, plainData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(plainData);
            });
        }));
    }

    public Future<byte[]> encryptSealedBox(NativeHandle recipientPublicEncryptKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encryptSealedBox(appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), (result, cipherText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(cipherText);
            });
        }));
    }

    public Future<byte[]> decryptSealedBox(NativeHandle senderPublicEncryptKey, NativeHandle senderSecretEncryptKey, byte[] cipherText) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.decryptSealedBox(appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(), (result, plainText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(plainText);
            });
        }));
    }

    public Future<byte[]> getRawPublicEncryptKey(NativeHandle publicEncKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encPubKeyGet(appHandle.toLong(), publicEncKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }

    public Future<byte[]> getRawSecretEncryptKey(NativeHandle secretEncKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encSecretKeyGet(appHandle.toLong(), secretEncKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }


    public Future<byte[]> getRawPublicSignKey(NativeHandle publicSignKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.signPubKeyGet(appHandle.toLong(), publicSignKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }

    public Future<byte[]> getRawSecretSignKey(NativeHandle secretSignKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.signSecKeyGet(appHandle.toLong(), secretSignKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }

    private static NativeHandle getPublicSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signPubKeyFree(appHandle.toLong(), signKey, (result) -> {
            });
        });
    }

    public static Future<byte[]> generateNonce() {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.generateNonce((result, nonce) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(nonce);
            });
        }));
    }

    public static Future<byte[]> sha3Hash(byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.sha3Hash(data, (result, hashedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(hashedData);
            });
        }));
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