package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;
import java.util.concurrent.CompletableFuture;



public class MDataPermissions {
    private final App app;
    private final MutableDataBinding mDataBinding;
    private final CallbackHelper callbackHelper;
    private final long mDataEntriesHandle;
    private  final long mDataPermissionHandle;
    private final long signKeyHandle;
    private final long permissionSetHandle;

    public MDataPermissions(App app, MutableDataBinding mDataBinding, CallbackHelper callbackHelper, long mDataEntriesHandle,
                            long mDataPermissionHandle, long signKeyHandle, long permissionSetHandle) {
        this.app = app;
        this.mDataBinding = mDataBinding;
        this.callbackHelper = callbackHelper;
        this.mDataEntriesHandle = mDataEntriesHandle;
        this.signKeyHandle = signKeyHandle;
        this.mDataPermissionHandle = mDataPermissionHandle;
        this.permissionSetHandle = permissionSetHandle;
    }

    public CompletableFuture<MutableData> getNewPermissionSet(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permission_set_new(app.getAppHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> getAllowPermissionSet(int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_allow(app.getAppHandle(), mDataPermissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> setDenyPermission(int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_deny(app.getAppHandle(), mDataPermissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> clearPermissions(int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_clear(app.getAppHandle(), mDataPermissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    //TODO: Free permission set from memory to be implemented

    public CompletableFuture<MutableData> createNewPermission(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permissions_new(app.getAppHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getpermissionLength(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permissions_len(app.getAppHandle(), mDataPermissionHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getPermissions(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permissions_get(app.getAppHandle(), mDataPermissionHandle, signKeyHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    //TODO: For each permissions to be implemented

    public CompletableFuture<Void> insertPermission(){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_insert(app.getAppHandle(), mDataPermissionHandle, signKeyHandle, permissionSetHandle, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    //TODO: Free permission set from memory
}
