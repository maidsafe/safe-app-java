package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.NativeHandle;
import net.maidsafe.model.MDataEntry;
import net.maidsafe.model.MDataValue;
import net.maidsafe.safe_app.CallbackByteArrayLenByteArrayLenLong;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MDataEntries {

    public static CompletableFuture<NativeHandle> newEntriesHandle() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesNew(BaseSession.appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            NativeHandle entriesHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntriesFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesHandle);
        });
        return future;
    }

    public static CompletableFuture<Void> insert(NativeHandle entriesHandle, byte[] key, byte[] value) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesInsert(BaseSession.appHandle.toLong(), entriesHandle.toLong(), key, value, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Long> length(NativeHandle entriesHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesLen(BaseSession.appHandle.toLong(), entriesHandle.toLong(), (result, len) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(len);
        });
        return future;
    }

    public static CompletableFuture<MDataValue> getValue(NativeHandle entriesHandle, byte[] key) {
        CompletableFuture<MDataValue> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesGet(BaseSession.appHandle.toLong(), entriesHandle.toLong(), key, (result, value, version) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new MDataValue(value, version));
        });
        return future;
    }

    public static CompletableFuture<List<MDataEntry>> listEntries(NativeHandle entriesHandle) {
        CompletableFuture<List<MDataEntry>> future = new CompletableFuture<>();
        List<MDataEntry> entries = new ArrayList<>();
        CallbackByteArrayLenByteArrayLenLong forEachCallback = (key, value, version) -> {
            entries.add(new MDataEntry(key, value, version));
        };
        NativeBindings.mdataEntriesForEach(BaseSession.appHandle.toLong(), entriesHandle.toLong(), forEachCallback, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(entries);
        });
        return future;
    }
}
