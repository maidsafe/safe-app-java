package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.NativeHandle;
import net.maidsafe.model.MDataValue;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.MDataKey;
import net.maidsafe.safe_app.MetadataResponse;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MData {

    public static CompletableFuture<MDataInfo> getPrivateMData(byte[] name, long typeTag, byte[] secretKey, byte[] nonce) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoNewPrivate(name, typeTag, secretKey, nonce, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public static CompletableFuture<MDataInfo> getRandomPrivateMData(long typeTag) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoRandomPrivate(typeTag, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public static CompletableFuture<MDataInfo> getRandomPublicMData(long typeTag) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoRandomPublic(typeTag, (result, mdInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(mdInfo);
        });
        return future;
    }

    public static CompletableFuture<byte[]> encryptEntryKey(MDataInfo mDataInfo, byte[] key) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoEncryptEntryKey(mDataInfo, key, (result, encryptedKey) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(encryptedKey);
        });
        return future;
    }

    public static CompletableFuture<byte[]> encryptEntryValue(MDataInfo mDataInfo, byte[] value) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoEncryptEntryValue(mDataInfo, value, (result, encryptedValue) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(encryptedValue);
        });
        return future;
    }

    public static CompletableFuture<byte[]> decrypt(MDataInfo mDataInfo, byte[] value) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoDecrypt(mDataInfo, value, (result, decryptedValue) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(decryptedValue);
        });
        return future;
    }

    public static CompletableFuture<byte[]> serialise(MDataInfo mDataInfo) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataInfoSerialise(mDataInfo, (result, serialisedData) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(serialisedData);
        });
        return future;
    }

    public static CompletableFuture<MDataInfo> deserialise(byte[] serialisedMData) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.mdataInfoDeserialise(serialisedMData, (result, mDataInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(mDataInfo);
        });
        return future;
    }

    public static CompletableFuture<Void> put(MDataInfo mDataInfo, NativeHandle permissionHandle, NativeHandle entriesHandle) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataPut(BaseSession.appHandle.toLong(), mDataInfo, permissionHandle.toLong(), entriesHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Long> getVersion(MDataInfo mDataInfo) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataGetVersion(BaseSession.appHandle.toLong(), mDataInfo, (result, version) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(version);
        });
        return future;
    }

    public static CompletableFuture<Long> getSerialisedSize(MDataInfo mDataInfo) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataSerialisedSize(BaseSession.appHandle.toLong(), mDataInfo, (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(size);
        });
        return future;
    }

    public static CompletableFuture<MDataValue> getValue(MDataInfo mDataInfo, byte[] key) {
        CompletableFuture<MDataValue> future = new CompletableFuture<>();
        NativeBindings.mdataGetValue(BaseSession.appHandle.toLong(), mDataInfo, key, (result, value, version) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new MDataValue(value, version));
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getEntriesHandle(MDataInfo mDataInfo) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataListEntries(BaseSession.appHandle.toLong(), mDataInfo, (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            NativeHandle entriesHandle = new NativeHandle(entriesH, (handle) -> {
                NativeBindings.mdataEntriesFree(BaseSession.appHandle.toLong(), handle, (res) -> {
                });
            });
            future.complete(entriesHandle);
        });
        return future;
    }

    public static CompletableFuture<List<MDataKey>> getKeys(MDataInfo mDataInfo) {
        CompletableFuture<List<MDataKey>> future = new CompletableFuture<>();
        NativeBindings.mdataListKeys(BaseSession.appHandle.toLong(), mDataInfo, (result, keys) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(Arrays.asList(keys));
        });
        return future;
    }

    public static CompletableFuture<List<MDataValue>> getValues(MDataInfo mDataInfo) {
        CompletableFuture<List<MDataValue>> future = new CompletableFuture<>();
        NativeBindings.mdataListValues(BaseSession.appHandle.toLong(), mDataInfo, (result, values) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(Helper.convertFromFfiMDataValue(values));
        });
        return future;
    }

    public static CompletableFuture<Void> mutateEntries(MDataInfo mDataInfo, NativeHandle actionHandle) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataMutateEntries(BaseSession.appHandle.toLong(), mDataInfo, actionHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<NativeHandle> getPermission(MDataInfo mDataInfo) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataListPermissions(BaseSession.appHandle.toLong(), mDataInfo, (result, permsHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            NativeHandle permissionHandle = new NativeHandle(permsHandle, (handle) -> {
                NativeBindings.mdataPermissionsFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(permissionHandle);
        });
        return future;
    }

    public static CompletableFuture<PermissionSet> getPermissionForUser(NativeHandle publicSignKey, MDataInfo mDataInfo) {
        CompletableFuture<PermissionSet> future = new CompletableFuture<>();
        NativeBindings.mdataListUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), (result, permissionSet) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(permissionSet);
        });
        return future;
    }

    public static CompletableFuture<Void> setUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, PermissionSet permissionSet, long version) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataSetUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), permissionSet, version, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> deleteUserPermission(NativeHandle publicSignKey, MDataInfo mDataInfo, long version) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataDelUserPermissions(BaseSession.appHandle.toLong(), mDataInfo, publicSignKey.toLong(), version, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<byte[]> encodeMetadata(MetadataResponse metadataResponse) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.mdataEncodeMetadata(metadataResponse, (result, encodedMetadata) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(encodedMetadata);
        });
        return future;
    }
}
