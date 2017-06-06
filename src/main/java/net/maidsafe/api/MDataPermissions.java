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


    public MDataPermissions(App app, MutableDataBinding mDataBinding, CallbackHelper callbackHelper, long mDataEntriesHandle) {
        this.app = app;
        this.mDataBinding = mDataBinding;
        this.callbackHelper = callbackHelper;
        this.mDataEntriesHandle = mDataEntriesHandle;
    }

    public CompletableFuture<MutableData> setNewPermission(){
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

    public CompletableFuture<Void> setAllowPermission(long permissionHandle, int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_allow(app.getAppHandle(), permissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> setDenyPermission(long permissionHandle, int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_deny(app.getAppHandle(), permissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> clearPermissions(long permissionHandle, int action){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_set_clear(app.getAppHandle(), permissionHandle, action, Pointer.NULL, callbackHelper.getResultCallBack(future));

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

    public CompletableFuture<MutableData> getpermissionLength(long permissionHandle){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permissions_len(app.getAppHandle(), permissionHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getPermissions(long permissionHandle, long signKeyHandle){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_permissions_get(app.getAppHandle(), permissionHandle, signKeyHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    //TODO: For each permissions to be implemented

    public CompletableFuture<Void> insertPermission(long permissionHandle, long signKeyHandle, long permissionSetHandle){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_permissions_insert(app.getAppHandle(), permissionHandle, signKeyHandle, permissionSetHandle, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    //TODO: Free permission set from memory
}
