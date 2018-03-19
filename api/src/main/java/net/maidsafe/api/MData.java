package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.MDataValue;
import net.maidsafe.safe_app.*;
import net.maidsafe.utils.Convertor;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.List;
import java.util.concurrent.Future;

class MData {
    private static AppHandle appHandle;

    public MData(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<MDataInfo> getPrivateMData(byte[] name, long typeTag, byte[] secretKey, byte[] nonce) {
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

    public Future<MDataInfo> getRandomPrivateMData(long typeTag) {
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

    public Future<MDataInfo> getRandomPublicMData(long typeTag) {
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

    public Future<byte[]> encryptEntryKey(MDataInfo mDataInfo, byte[] key) {
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

    public Future<byte[]> encryptEntryValue(MDataInfo mDataInfo, byte[] value) {
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

    public Future<byte[]> decrypt(MDataInfo mDataInfo, byte[] value) {
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

    public Future<byte[]> serialise(MDataInfo mDataInfo) {
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

    public Future<MDataInfo> deserialise(byte[] serialisedMData) {
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

    public Future<Void> put(MDataInfo mDataInfo, NativeHandle permissionHandle, NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataPut(appHandle.toLong(), mDataInfo, permissionHandle.toLong(), entriesHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Long> getVersion(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataGetVersion(appHandle.toLong(), mDataInfo, (result, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(version);
            });
        }));
    }

    public Future<Long> getSerialisedSize(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataSerialisedSize(appHandle.toLong(), mDataInfo, (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }

    public Future<MDataValue> getValue(MDataInfo mDataInfo, byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<MDataValue>(binder -> {
            NativeBindings.mdataGetValue(appHandle.toLong(), mDataInfo, key, (result, value, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new MDataValue(value, version));
            });
        }));
    }

    public Future<NativeHandle> getEntriesHandle(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataEntries(appHandle.toLong(), mDataInfo, (result, entriesH) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }

                binder.onResult(new NativeHandle(entriesH, (h) -> NativeBindings.mdataEntriesFree(appHandle.toLong(), entriesH, (r) -> {})));
            });
        }));
    }

    public Future<List<byte[]>> getKeys(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<List<byte[]>>(binder -> {
            NativeBindings.mdataListKeys(appHandle.toLong(), mDataInfo, (result, keys) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Convertor.toKeys(keys));
            });
        }));
    }

    public Future<List<MDataValue>> getValues(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<List<MDataValue>>(binder -> {
            NativeBindings.mdataListValues(appHandle.toLong(), mDataInfo, (result, values) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Convertor.toMDataValue(values));
            });
        }));
    }

    public Future<Void> mutateEntries(MDataInfo mDataInfo, NativeHandle actionHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataMutateEntries(appHandle.toLong(), mDataInfo, actionHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<NativeHandle> getPermission(MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataListPermissions(appHandle.toLong(), mDataInfo, (result, permsHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle permissionHandle = new NativeHandle(permsHandle, (handle) -> {
                    NativeBindings.mdataPermissionsFree(appHandle.toLong(), handle, res -> {
                    });
                });
                binder.onResult(permissionHandle);
            });
        }));
    }

    public Future<PermissionSet> getPermissionForUser(NativeHandle publicSignKey, MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<PermissionSet>(binder -> {
            NativeBindings.mdataListUserPermissions(appHandle.toLong(), mDataInfo, publicSignKey.toLong(), (result, permissionSet) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(permissionSet);
            });
        }));
    }

    public Future<Void> setUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, PermissionSet permissionSet, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataSetUserPermissions(appHandle.toLong(), mDataInfo, publicSignKey.toLong(), permissionSet, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Void> deleteUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataDelUserPermissions(appHandle.toLong(), mDataInfo, publicSignKey.toLong(), version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<byte[]> encodeMetadata(MetadataResponse metadataResponse) {
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
