package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.MDataEntry;
import net.maidsafe.api.model.MDataValue;
import net.maidsafe.safe_app.CallbackByteArrayLenByteArrayLenLong;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MDataEntries {

    public static Future<NativeHandle> newEntriesHandle() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataEntriesNew(BaseSession.appHandle.toLong(), (result, entriesH) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                NativeHandle entriesHandle = new NativeHandle(entriesH, handle -> {
                    NativeBindings.mdataEntriesFree(BaseSession.appHandle.toLong(), handle, res -> {
                    });
                });
                binder.onResult(entriesHandle);
            });
        }));
    }

    public static Future<Void> insert(NativeHandle entriesHandle, byte[] key, byte[] value) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataEntriesInsert(BaseSession.appHandle.toLong(), entriesHandle.toLong(), key, value, result -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<Long> length(NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataEntriesLen(BaseSession.appHandle.toLong(), entriesHandle.toLong(), (result, len) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(len);
            });
        }));
    }

    public static Future<MDataValue> getValue(NativeHandle entriesHandle, byte[] key) {
        return Executor.getInstance().submit(new CallbackHelper<MDataValue>(binder -> {
            NativeBindings.mdataEntriesGet(BaseSession.appHandle.toLong(), entriesHandle.toLong(), key, (result, value, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new MDataValue(value, version));
            });
        }));
    }

    public static Future<List<MDataEntry>> listEntries(NativeHandle entriesHandle) {
        return Executor.getInstance().submit(new CallbackHelper<List<MDataEntry>>(binder -> {
            List<MDataEntry> entries = new ArrayList<>();
            CallbackByteArrayLenByteArrayLenLong forEachCallback = (key, value, version) -> {
                entries.add(new MDataEntry(key, value, version));
            };
            NativeBindings.mdataEntriesForEach(BaseSession.appHandle.toLong(), entriesHandle.toLong(), forEachCallback, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(entries);
            });
        }));
    }
}
