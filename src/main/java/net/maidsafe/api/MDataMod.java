package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class MDataMod {
    private final App app;
    private final MutableDataBinding mDataBinding;
    private final CallbackHelper callbackHelper;
    private final long mDataInfoHandle;
    private final long mDatapermissionsHandle;
    private final long mDataEntriesHandle;
    private final long mDataSignKeyHandle;
    private final long mDataActionHandle;
    private final long permissionSetHandle;

    public MDataMod(App app, MutableDataBinding mDataBinding, CallbackHelper callbackHelper, long mDataInfoHandle,
                    long mDataEntriesHandle, long mDatapermissionsHandle, long mDataActionHandle,
                    long mDataSignKeyHandle, long permissionSetHandle) {
        this.app = app;
        this.mDataBinding = mDataBinding;
        this.callbackHelper = callbackHelper;
        this.mDataInfoHandle = mDataInfoHandle;
        this.mDatapermissionsHandle = mDatapermissionsHandle;
        this.mDataEntriesHandle = mDataEntriesHandle;
        this.mDataActionHandle = mDataActionHandle;
        this.mDataSignKeyHandle = mDataSignKeyHandle;
        this.permissionSetHandle = permissionSetHandle;
    }

    public CompletableFuture<Void> put(){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_put(app.getAppHandle(), mDataInfoHandle, mDatapermissionsHandle, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<MutableData> getVersion(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_get_version(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }


    public CompletableFuture<byte[]> getValue(byte[] key){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_get_value(app.getAppHandle(), mDataInfoHandle, key, key.length, Pointer.NULL, callbackHelper.getDataWithVersionCallback(future));

        return future;
    }

    public CompletableFuture<MutableData> getEntries(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_entries(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getKeys(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_keys(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getValues(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_values(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> mutateEntries(){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_mutate_entries(app.getAppHandle(), mDataInfoHandle, mDataActionHandle, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<MutableData> getPermissions(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_permissions(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getUserPermissions(){
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_user_permissions(app.getAppHandle(), mDataInfoHandle, mDataSignKeyHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> setUserPermissions(long permissionSetHandle, long version){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_set_user_permissions(app.getAppHandle(), mDataInfoHandle, mDataSignKeyHandle, permissionSetHandle, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> deleteUserPermissions(long version){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_del_user_permissions(app.getAppHandle(), mDataInfoHandle, mDataSignKeyHandle, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> changeOwner(long version){
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_change_owner(app.getAppHandle(), mDataInfoHandle, mDataSignKeyHandle, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }
}
