package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.MDataEntry;
import net.maidsafe.safe_app.MDataValue;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

class MDataEntries {
    private static AppHandle appHandle;

    public MDataEntries(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<NativeHandle> newEntriesHandle() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataEntriesNew(appHandle.toLong(), (result, entriesH) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle entriesHandle = new NativeHandle(entriesH, handle -> {
                    NativeBindings.mdataEntriesFree(appHandle.toLong(), handle, res -> {
                    });
                });
                binder.onResult(entriesHandle);
            });
        }));
    }

    public Future<Void> insert(NativeHandle entriesHandle, byte[] key, byte[] value) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataEntriesInsert(appHandle.toLong(), entriesHandle.toLong(), key, value, result -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Long> length(NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataEntriesLen(appHandle.toLong(), entriesHandle.toLong(), (result, len) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(len);
            });
        }));
    }

    public Future<MDataValue> getValue(NativeHandle entriesHandle, byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<MDataValue>(binder -> {
            NativeBindings.mdataEntriesGet(appHandle.toLong(), entriesHandle.toLong(), key, (result, value, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                MDataValue mDataValue = new MDataValue();
                mDataValue.setContent(value);
                mDataValue.setContentLen(value.length);
                mDataValue.setEntryVersion(version);
                binder.onResult(mDataValue);
            });
        }));
    }

    public Future<List<MDataEntry>> listEntries(NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<List<MDataEntry>>(binder -> {
            NativeBindings.mdataListEntries(appHandle.toLong(), entriesHandle.toLong(),(result, entries) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(entries));
            });
        }));
    }
}
