package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

public class Crypto {

    public static Future<NativeHandle> getAppPublicSignKey() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.appPubSignKey(BaseSession.appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(handle, (key) -> {
                    NativeBindings.signPubKeyFree(BaseSession.appHandle.toLong(), key, (freeResult) -> {

                    });
                }));
            });
        }));
    }

    public static Future<SignKeyPair> generateSignKeyPair() {
        return Executor.getInstance().submit(new CallbackHelper<SignKeyPair>(binder -> {
            NativeBindings.signGenerateKeyPair(BaseSession.appHandle.toLong(), (result, pubSignKeyHandle, secSignKeyHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new SignKeyPair(getPublicSignKeyHandle(pubSignKeyHandle), getSecretSignKeyHandle(secSignKeyHandle)));
            });
        }));
    }

    public static Future<NativeHandle> getPublicSignKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.signPubKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicSignKeyHandle(handle));
            });
        }));
    }

    public static Future<NativeHandle> getSecretSignKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.signSecKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getSecretSignKeyHandle(handle));
            });
        }));
    }

    public static Future<EncryptKeyPair> generateEncryptKeyPair() {
        return Executor.getInstance().submit(new CallbackHelper<EncryptKeyPair>(binder -> {
            NativeBindings.encGenerateKeyPair(BaseSession.appHandle.toLong(), (result, pubEncHandle, secEncHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                EncryptKeyPair keyPair = new EncryptKeyPair(getPublicEncKeyHandle(pubEncHandle), getSecretEncKeyHandle(secEncHandle));
                binder.onResult(keyPair);
            });
        }));
    }

    public static Future<NativeHandle> getAppPublicEncryptKey() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.appPubEncKey(BaseSession.appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicEncKeyHandle(handle));
            });
        }));
    }

    public static Future<NativeHandle> getPublicEncryptKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.encPubKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getPublicEncKeyHandle(handle));
            });
        }));
    }

    public static Future<NativeHandle> getSecretEncryptKey(byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.encSecretKeyNew(BaseSession.appHandle.toLong(), key, (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getSecretEncKeyHandle(handle));
            });
        }));
    }

    public static Future<byte[]> sign(NativeHandle secretSignKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.sign(BaseSession.appHandle.toLong(), data, secretSignKey.toLong(), (result, signedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(signedData);
            });
        }));
    }

    public static Future<byte[]> verify(NativeHandle publicSignKey, byte[] signedData) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.verify(BaseSession.appHandle.toLong(), signedData, publicSignKey.toLong(), (result, verifiedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(verifiedData);
            });
        }));
    }

    public static Future<byte[]> encrypt(NativeHandle recipientPublicEncryptKey, NativeHandle senderSecretEncryptKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encrypt(BaseSession.appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(), (result, cipherText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(cipherText);
            });
        }));
    }

    public static Future<byte[]> decrypt(NativeHandle senderPublicEncryptKey, NativeHandle recipientSecretEncryptKey, byte[] cipherText) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.decrypt(BaseSession.appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(), recipientSecretEncryptKey.toLong(), (result, plainData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(plainData);
            });
        }));
    }

    public static Future<byte[]> encryptSealedBox(NativeHandle recipientPublicEncryptKey, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encryptSealedBox(BaseSession.appHandle.toLong(), data, recipientPublicEncryptKey.toLong(), (result, cipherText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(cipherText);
            });
        }));
    }

    public static Future<byte[]> decryptSealedBox(NativeHandle senderPublicEncryptKey, NativeHandle senderSecretEncryptKey, byte[] cipherText) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.decryptSealedBox(BaseSession.appHandle.toLong(), cipherText, senderPublicEncryptKey.toLong(), senderSecretEncryptKey.toLong(), (result, plainText) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(plainText);
            });
        }));
    }

    public static Future<byte[]> getRawPublicEncryptKey(NativeHandle publicEncKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encPubKeyGet(BaseSession.appHandle.toLong(), publicEncKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }

    public static Future<byte[]> getRawSecretEncryptKey(NativeHandle secretEncKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.encSecretKeyGet(BaseSession.appHandle.toLong(), secretEncKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }


    public static Future<byte[]> getRawPublicSignKey(NativeHandle publicSignKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.signPubKeyGet(BaseSession.appHandle.toLong(), publicSignKey.toLong(), (result, key) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(key);
            });
        }));
    }

    public static Future<byte[]> getRawSecretSignKey(NativeHandle secretSignKey) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.signSecKeyGet(BaseSession.appHandle.toLong(), secretSignKey.toLong(), (result, key) -> {
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
            NativeBindings.signPubKeyFree(BaseSession.appHandle.toLong(), signKey, (result) -> {});
        });
    }

    private static NativeHandle getSecretSignKeyHandle(long handle) {
        return new NativeHandle(handle, (signKey) -> {
            NativeBindings.signSecKeyFree(BaseSession.appHandle.toLong(), signKey, (result) -> {});
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