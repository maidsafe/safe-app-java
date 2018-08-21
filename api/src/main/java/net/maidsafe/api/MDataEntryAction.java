package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;

public class MDataEntryAction {
    private AppHandle appHandle;

    public MDataEntryAction(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<NativeHandle> newEntryAction() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsNew(appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            NativeHandle entriesActionHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntryActionsFree(appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesActionHandle);
        });
        return future;
    }

    public CompletableFuture insert(NativeHandle actionHandle, byte[] key, byte[] value) {
        CompletableFuture future = new CompletableFuture();
            NativeBindings.mdataEntryActionsInsert(appHandle.toLong(), actionHandle.toLong(), key, value,
                    (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }

    public CompletableFuture update(NativeHandle actionHandle, byte[] key, byte[] value, long version) {
        CompletableFuture future = new CompletableFuture();
            NativeBindings.mdataEntryActionsUpdate(appHandle.toLong(), actionHandle.toLong(), key, value,
                    version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }

    public CompletableFuture<Void> delete(NativeHandle actionHandle, byte[] key, long version) {
        CompletableFuture future = new CompletableFuture();
            NativeBindings.mdataEntryActionsDelete(appHandle.toLong(), actionHandle.toLong(), key,
                    version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }
}
