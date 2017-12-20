package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.MDataValue;
import net.maidsafe.utils.Convertor;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.MDataKey;
import net.maidsafe.safe_app.MetadataResponse;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

public class MData {

    public static Future<MDataInfo> getPrivateMData(byte[] name, long typeTag, byte[] secretKey, byte[] nonce) {
        return Executor.getInstance().submit(new CallbackHelper<MDataInfo>(binder -> {
            NativeBindings.mdataInfoNewPrivate(name, typeTag, secretKey, nonce, (result, mdInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(mdInfo);
            });
        }));
    }

    public static Future<MDataInfo> getRandomPrivateMData(long typeTag) {
        return Executor.getInstance().submit(new CallbackHelper<MDataInfo>(binder -> {
            NativeBindings.mdataInfoRandomPrivate(typeTag, (result, mdInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(mdInfo);
            });
        }));
    }

    public static Future<MDataInfo> getRandomPublicMData(long typeTag) {
        return Executor.getInstance().submit(new CallbackHelper<MDataInfo>(binder -> {
            NativeBindings.mdataInfoRandomPublic(typeTag, (result, mdInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(mdInfo);
            });
        }));
    }

    public static Future<byte[]> encryptEntryKey(MDataInfo mDataInfo, byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.mdataInfoEncryptEntryKey(mDataInfo, key, (result, encryptedKey) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(encryptedKey);
            });
        }));
    }

    public static Future<byte[]> encryptEntryValue(MDataInfo mDataInfo, byte[] value) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.mdataInfoEncryptEntryValue(mDataInfo, value, (result, encryptedValue) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(encryptedValue);
            });
        }));
    }

    public static Future<byte[]> decrypt(MDataInfo mDataInfo, byte[] value) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.mdataInfoDecrypt(mDataInfo, value, (result, decryptedValue) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(decryptedValue);
            });
        }));
    }

    public static Future<byte[]> serialise(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.mdataInfoSerialise(mDataInfo, (result, serialisedData) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(serialisedData);
            });
        }));
    }

    public static Future<MDataInfo> deserialise(byte[] serialisedMData) {
        return Executor.getInstance().submit(new CallbackHelper<MDataInfo>(binder -> {
            NativeBindings.mdataInfoDeserialise(serialisedMData, (result, mDataInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(mDataInfo);
            });
        }));
    }

    public static Future<Void> put(MDataInfo mDataInfo, NativeHandle permissionHandle, NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataPut(BaseSession.appHandle.toLong(), mDataInfo, permissionHandle.toLong(), entriesHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<Long> getVersion(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataGetVersion(BaseSession.appHandle.toLong(), mDataInfo, (result, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(version);
            });
        }));
    }

    public static Future<Long> getSerialisedSize(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataSerialisedSize(BaseSession.appHandle.toLong(), mDataInfo, (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }

    public static Future<MDataValue> getValue(MDataInfo mDataInfo, byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<MDataValue>(binder -> {
            NativeBindings.mdataGetValue(BaseSession.appHandle.toLong(), mDataInfo, key, (result, value, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new MDataValue(value, version));
            });
        }));
    }

    public static Future<NativeHandle> getEntriesHandle(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataListEntries(BaseSession.appHandle.toLong(), mDataInfo, (result, entriesH) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle entriesHandle = new NativeHandle(entriesH, (handle) -> {
                    NativeBindings.mdataEntriesFree(BaseSession.appHandle.toLong(), handle, (res) -> {
                    });
                });
                binder.onResult(entriesHandle);
            });
        }));
    }

    public static Future<List<MDataKey>> getKeys(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<List<MDataKey>>(binder -> {
            NativeBindings.mdataListKeys(BaseSession.appHandle.toLong(), mDataInfo, (result, keys) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(keys));
            });
        }));
    }

    public static Future<List<MDataValue>> getValues(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<List<MDataValue>>(binder -> {
            NativeBindings.mdataListValues(BaseSession.appHandle.toLong(), mDataInfo, (result, values) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Convertor.toMDataValue(values));
            });
        }));
    }

    public static Future<Void> mutateEntries(MDataInfo mDataInfo, NativeHandle actionHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataMutateEntries(BaseSession.appHandle.toLong(), mDataInfo, actionHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<NativeHandle> getPermission(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataListPermissions(BaseSession.appHandle.toLong(), mDataInfo, (result, permsHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle permissionHandle = new NativeHandle(permsHandle, (handle) -> {
                    NativeBindings.mdataPermissionsFree(BaseSession.appHandle.toLong(), handle, res -> {
                    });
                });
                binder.onResult(permissionHandle);
            });
        }));
    }

    public static Future<PermissionSet> getPermissionForUser(NativeHandle publicSignKey, MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<PermissionSet>(binder -> {
            NativeBindings.mdataListUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), (result, permissionSet) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(permissionSet);
            });
        }));
    }

    public static Future<Void> setUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, PermissionSet permissionSet, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataSetUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), permissionSet, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<Void> deleteUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataDelUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<byte[]> encodeMetadata(MetadataResponse metadataResponse) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.mdataEncodeMetadata(metadataResponse, (result, encodedMetadata) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(encodedMetadata);
            });
        }));
    }
}
