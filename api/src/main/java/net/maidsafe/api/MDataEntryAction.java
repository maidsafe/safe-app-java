package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;


public class MDataEntryAction {
    private AppHandle appHandle;

    public MDataEntryAction(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle appHandle) {
        this.appHandle = appHandle;
    }


    public CompletableFuture<NativeHandle> newEntryAction() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsNew(appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            final NativeHandle entriesActionHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntryActionsFree(appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesActionHandle);
        });
        return future;
    }

    public CompletableFuture insert(final NativeHandle actionHandle, final byte[] key, final byte[] value) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
            NativeBindings.mdataEntryActionsInsert(appHandle.toLong(), actionHandle.toLong(), key, value,
                    (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }


    public CompletableFuture update(final NativeHandle actionHandle, final byte[] key, final byte[] value, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
            NativeBindings.mdataEntryActionsUpdate(appHandle.toLong(), actionHandle.toLong(), key, value,
                    version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }


    public CompletableFuture<Void> delete(final NativeHandle actionHandle, final byte[] key, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
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
