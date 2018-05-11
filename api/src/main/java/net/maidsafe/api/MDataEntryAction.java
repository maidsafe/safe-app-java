package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

class MDataEntryAction {
    private AppHandle appHandle;

    public MDataEntryAction(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<NativeHandle> newEntryAction() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataEntryActionsNew(appHandle.toLong(), (result, entriesH) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle entriesActionHandle = new NativeHandle(entriesH, handle -> {
                    NativeBindings.mdataEntryActionsFree(appHandle.toLong(), handle, res -> {
                    });
                });
                binder.onResult(entriesActionHandle);
            });
        }));
    }

    public Future<Void> insert(NativeHandle actionHandle, byte[] key, byte[] value) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataEntryActionsInsert(appHandle.toLong(), actionHandle.toLong(), key, value, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Void> update(NativeHandle actionHandle, byte[] key, byte[] value, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataEntryActionsUpdate(appHandle.toLong(), actionHandle.toLong(), key, value, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Void> delete(NativeHandle actionHandle, byte[] key, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataEntryActionsDelete(appHandle.toLong(), actionHandle.toLong(), key, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }
}
