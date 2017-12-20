package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.safe_app.UserPermissionSet;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

public class MDataPermission {

    public static Future<NativeHandle> newPermissionHandle() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.mdataPermissionsNew(BaseSession.appHandle.toLong(), (result, permissionsHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(permissionsHandle, handle -> {
                    NativeBindings.mdataPermissionsFree(BaseSession.appHandle.toLong(), handle, res -> {
                    });
                }));
            });
        }));
    }

    public static Future<Long> getLength(NativeHandle permissionHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.mdataPermissionsLen(BaseSession.appHandle.toLong(), permissionHandle.toLong(), (result, len) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(len);
            });
        }));
    }

    public static Future<PermissionSet> getPermissionForUser(NativeHandle permissionHandle, NativeHandle signKey) {
        return Executor.getInstance().submit(new CallbackHelper<PermissionSet>(binder -> {
            NativeBindings.mdataPermissionsGet(BaseSession.appHandle.toLong(), permissionHandle.toLong(), signKey.toLong(), (result, permsSet) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(permsSet);
            });
        }));
    }

    public static Future<List<UserPermissionSet>> listAll(NativeHandle permissionHandle) {
        return Executor.getInstance().submit(new CallbackHelper<List<UserPermissionSet>>(binder -> {
            NativeBindings.mdataListPermissionSets(BaseSession.appHandle.toLong(), permissionHandle.toLong(), (result, permsArray) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(permsArray));
            });
        }));
    }

    public static Future<Void> insert(NativeHandle permissionHandle, NativeHandle publicSignKey, PermissionSet permissionSet) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.mdataPermissionsInsert(BaseSession.appHandle.toLong(), permissionHandle.toLong(), publicSignKey.toLong(), permissionSet, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }
}
