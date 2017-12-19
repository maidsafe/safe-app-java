package net.maidsafe.api.mdata;

import net.maidsafe.api.BaseSession;
import net.maidsafe.api.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.safe_app.UserPermissionSet;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MDataPermission {

    public static CompletableFuture<NativeHandle> newPermissionHandle() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsNew(BaseSession.appHandle.toLong(), (result, permissionsHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NativeHandle(permissionsHandle, handle -> {
                NativeBindings.mdataPermissionsFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public static CompletableFuture<Long> getLength(NativeHandle permissionHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsLen(BaseSession.appHandle.toLong(), permissionHandle.toLong(), (result, len) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(len);
        });
        return future;
    }

    public static CompletableFuture<PermissionSet> getPermissionForUser(NativeHandle permissionHandle, NativeHandle signKey) {
        CompletableFuture<PermissionSet> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsGet(BaseSession.appHandle.toLong(), permissionHandle.toLong(), signKey.toLong(), (result, permsSet) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(permsSet);
        });
        return future;
    }

    public static CompletableFuture<List<UserPermissionSet>> listAll(NativeHandle permissionHandle) {
        CompletableFuture<List<UserPermissionSet>> future = new CompletableFuture<>();
        NativeBindings.mdataListPermissionSets(BaseSession.appHandle.toLong(), permissionHandle.toLong(), (result, permsArray) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(Arrays.asList(permsArray));
        });
        return future;
    }

    public static CompletableFuture<Void> insert(NativeHandle permissionHandle, NativeHandle publicSignKey, PermissionSet permissionSet) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsInsert(BaseSession.appHandle.toLong(), permissionHandle.toLong(), publicSignKey.toLong(), permissionSet, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }
}
