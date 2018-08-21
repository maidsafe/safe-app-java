package net.maidsafe.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.*;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;


public class MData {
    private static AppHandle appHandle;

    public MData(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<MDataInfo> getPrivateMData(byte[] name, long typeTag, byte[] secretKey,
                                                        byte[] nonce) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoNewPrivate(name, typeTag, secretKey, nonce, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public CompletableFuture<MDataInfo> getRandomPrivateMData(long typeTag) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoRandomPrivate(typeTag, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public CompletableFuture<MDataInfo> getRandomPublicMData(long typeTag) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoRandomPublic(typeTag, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public CompletableFuture<byte[]> encryptEntryKey(MDataInfo mDataInfo, byte[] key) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoEncryptEntryKey(mDataInfo, key, (result, encryptedKey) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(encryptedKey);
        });
        return future;
    }

    public CompletableFuture<byte[]> encryptEntryValue(MDataInfo mDataInfo, byte[] value) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoEncryptEntryValue(mDataInfo, value, (result, encryptedValue) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(encryptedValue);
        });
        return future;
    }

    public CompletableFuture<byte[]> decrypt(MDataInfo mDataInfo, byte[] value) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoDecrypt(mDataInfo, value, (result, decryptedValue) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(decryptedValue);
        });
        return future;
    }

    public CompletableFuture<byte[]> serialise(MDataInfo mDataInfo) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoSerialise(mDataInfo, (result, serialisedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(serialisedData);
        });
        return future;
    }

    public CompletableFuture<MDataInfo> deserialise(byte[] serialisedMData) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoDeserialise(serialisedMData, (result, mDataInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(mDataInfo);
        });
        return future;
    }

    public CompletableFuture<Void> put(MDataInfo mDataInfo, NativeHandle permissionHandle,
                                       NativeHandle entriesHandle) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            NativeBindings.mdataPut(appHandle.toLong(), mDataInfo, permissionHandle.toLong(),
                    entriesHandle.toLong(), (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });
        });
        return future;
    }

    public CompletableFuture<Long> getVersion(MDataInfo mDataInfo) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataGetVersion(appHandle.toLong(), mDataInfo, (result, version) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(version);
        });
        return future;
    }

    public CompletableFuture<Long> getSerialisedSize(MDataInfo mDataInfo) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataSerialisedSize(appHandle.toLong(), mDataInfo, (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<MDataValue> getValue(MDataInfo mDataInfo, byte[] key) {
        CompletableFuture<MDataValue> future = new CompletableFuture<>();
        NativeBindings.mdataGetValue(appHandle.toLong(), mDataInfo, key,
                (result, value, version) -> {
                    if (result.getErrorCode() != 0) {
                      future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    MDataValue mDataValue = new MDataValue();
                    mDataValue.setContent(value);
                    mDataValue.setContentLen(value.length);
                    mDataValue.setEntryVersion(version);
                    future.complete(mDataValue);
                });
        return future;
    }

    public CompletableFuture<NativeHandle> getEntriesHandle(MDataInfo mDataInfo) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntries(appHandle.toLong(), mDataInfo, (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
              future.completeExceptionally(Helper.ffiResultToException(result));
            }

            future.complete(new NativeHandle(entriesH,
                    (h) -> NativeBindings.mdataEntriesFree(appHandle.toLong(), entriesH, (r) -> {
                    })));
        });
        return future;
    }

    public CompletableFuture<List<MDataKey>> getKeys(MDataInfo mDataInfo) {
        CompletableFuture<List<MDataKey>> future = new CompletableFuture<>();
        NativeBindings.mdataListKeys(appHandle.toLong(), mDataInfo, (result, keys) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(keys));
        });
        return future;
    }

    public CompletableFuture<List<MDataValue>> getValues(MDataInfo mDataInfo) {
        CompletableFuture<List<MDataValue>> future = new CompletableFuture<>();
        NativeBindings.mdataListValues(appHandle.toLong(), mDataInfo, (result, values) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(values));
        });
        return future;
    }

    public CompletableFuture<Void> mutateEntries(MDataInfo mDataInfo, NativeHandle actionHandle) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.mdataMutateEntries(appHandle.toLong(), mDataInfo, actionHandle.toLong(),
                    (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getPermission(MDataInfo mDataInfo) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataListPermissions(appHandle.toLong(), mDataInfo, (result, permsHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            NativeHandle permissionHandle = new NativeHandle(permsHandle, (handle) -> {
                NativeBindings.mdataPermissionsFree(appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(permissionHandle);
        });
        return future;
    }

    public CompletableFuture<PermissionSet> getPermissionForUser(NativeHandle publicSignKey,
                                                                 MDataInfo mDataInfo) {
        CompletableFuture<PermissionSet> future = new CompletableFuture<>();
        NativeBindings.mdataListUserPermissions(appHandle.toLong(), mDataInfo,
                publicSignKey.toLong(), (result, permissionSet) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(permissionSet);
                });
        return future;
    }

    public CompletableFuture<Void> setUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo,
                                                     PermissionSet permissionSet, long version) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.mdataSetUserPermissions(appHandle.toLong(), mDataInfo,
                    publicSignKey.toLong(), permissionSet, version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });
        });
        return future;
    }

    public CompletableFuture<Void> deleteUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo,
                                                        long version) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.mdataDelUserPermissions(appHandle.toLong(), mDataInfo,
                    publicSignKey.toLong(), version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });

        });
        return future;
    }

    public CompletableFuture<byte[]> encodeMetadata(MetadataResponse metadataResponse) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataEncodeMetadata(metadataResponse, (result, encodedMetadata) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(encodedMetadata);
        });
        return future;
    }
}
