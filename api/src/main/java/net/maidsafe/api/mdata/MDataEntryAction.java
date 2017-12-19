package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

import java.util.concurrent.CompletableFuture;

public class MDataEntryAction {

    public static CompletableFuture<NativeHandle> newEntryAction() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsNew(BaseSession.appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            NativeHandle entriesActionHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntryActionsFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesActionHandle);
        });
        return future;
    }

    public static CompletableFuture<Void> insert(NativeHandle actionHandle, byte[] key, byte[] value) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsInsert(BaseSession.appHandle.toLong(), actionHandle.toLong(), key, value, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> update(NativeHandle actionHandle, byte[] key, byte[] value, long version) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsUpdate(BaseSession.appHandle.toLong(), actionHandle.toLong(), key, value, version, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> delete(NativeHandle actionHandle, byte[] key, long version) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsDelete(BaseSession.appHandle.toLong(), actionHandle.toLong(), key, version, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }
}
