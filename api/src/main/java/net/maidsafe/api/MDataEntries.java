package net.maidsafe.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.MDataEntry;
import net.maidsafe.safe_app.MDataValue;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

public class MDataEntries {
    private static AppHandle appHandle;

    public MDataEntries(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<NativeHandle> newEntriesHandle() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesNew(appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            NativeHandle entriesHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntriesFree(appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesHandle);
        });
        return future;
    }

    public CompletableFuture<Void> insert(NativeHandle entriesHandle, byte[] key, byte[] value) {
        CompletableFuture future = new CompletableFuture();
            NativeBindings.mdataEntriesInsert(appHandle.toLong(), entriesHandle.toLong(), key, value,
                    result -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }

    public CompletableFuture<Long> length(NativeHandle entriesHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesLen(appHandle.toLong(), entriesHandle.toLong(), (result, len) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(len);
        });
        return future;
    }

    public CompletableFuture<MDataValue> getValue(NativeHandle entriesHandle, byte[] key) {
        CompletableFuture<MDataValue> future = new CompletableFuture<>();
        NativeBindings.mdataEntriesGet(appHandle.toLong(), entriesHandle.toLong(), key,
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

    public CompletableFuture<List<MDataEntry>> listEntries(NativeHandle entriesHandle) {
        CompletableFuture<List<MDataEntry>> future = new CompletableFuture<>();
        NativeBindings.mdataListEntries(appHandle.toLong(), entriesHandle.toLong(),
                (result, entries) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(Arrays.asList(entries));
                });
        return future;
    }
}
