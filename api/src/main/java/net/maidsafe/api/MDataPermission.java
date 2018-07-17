package net.maidsafe.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.safe_app.UserPermissionSet;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;



public class MDataPermission {
    private AppHandle appHandle;

    public MDataPermission(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<NativeHandle> newPermissionHandle() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsNew(appHandle.toLong(), (result, permissionsHandle) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(new NativeHandle(permissionsHandle, handle -> {
                NativeBindings.mdataPermissionsFree(appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<Long> getLength(NativeHandle permissionHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsLen(appHandle.toLong(), permissionHandle.toLong(),
                (result, len) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                        return;
                    }
                    future.complete(len);
                });
        return future;
    }

    public CompletableFuture<PermissionSet> getPermissionForUser(NativeHandle permissionHandle,
                                                                 NativeHandle signKey) {
        CompletableFuture<PermissionSet> future = new CompletableFuture<>();
        NativeBindings.mdataPermissionsGet(appHandle.toLong(), permissionHandle.toLong(),
                signKey.toLong(), (result, permsSet) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                        return;
                    }
                    future.complete(permsSet);
                });
        return future;
    }

    public CompletableFuture<List<UserPermissionSet>> listAll(NativeHandle permissionHandle) {
        CompletableFuture<List<UserPermissionSet>> future = new CompletableFuture<>();
        NativeBindings.mdataListPermissionSets(appHandle.toLong(), permissionHandle.toLong(),
                (result, permsArray) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                        return;
                    }
                    future.complete(Arrays.asList(permsArray));
                });
        return future;
    }

    public CompletableFuture insert(NativeHandle permissionHandle, NativeHandle publicSignKey,
                                    PermissionSet permissionSet) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.mdataPermissionsInsert(appHandle.toLong(), permissionHandle.toLong(),
                    publicSignKey.toLong(), permissionSet, (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });

        });
        return future;
    }
}
