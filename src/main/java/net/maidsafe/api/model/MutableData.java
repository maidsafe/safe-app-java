package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;

public class MutableData {

    private final Pointer appHandle;
    private final long mDataInfoHandle;
    private final CallbackHelper callbackHelper;
    private final MutableDataBinding mDataBinding;

    public MutableData(Pointer appHandle, long mDataInfoHandle) {
        this.appHandle = appHandle;
        this.mDataInfoHandle = mDataInfoHandle;
        this.mDataBinding = BindingFactory.getInstance().getMutableData();
        this.callbackHelper = CallbackHelper.getInstance();
    }


    public CompletableFuture<byte[]> encryptKey(byte[] key) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_encrypt_entry_key(appHandle, mDataInfoHandle, key, key.length, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<byte[]> encryptValue(byte[] value) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_encrypt_entry_value(appHandle, mDataInfoHandle, value, value.length, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<Pointer> getNameAndTag() {
        final CompletableFuture<Pointer> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_extract_name_and_type_tag(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getPointerCallback(future));

        return future;
    }

    public CompletableFuture<Long> getVersion() {
        final CompletableFuture<Long> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_get_version(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(future));

        return future;
    }

    public CompletableFuture<byte[]> get(byte[] key) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_get_value(appHandle, mDataInfoHandle, key, key.length, Pointer.NULL, callbackHelper.getDataWithVersionCallback(future));

        return future;
    }

    public CompletableFuture<Void> put(MDataPermissions MDataPermissions, MDataEntries mDataEntries) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_put(appHandle, mDataInfoHandle, MDataPermissions.getHandle(), mDataEntries.getHandle(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<MDataEntries> getEntries() {
        final CompletableFuture<MDataEntries> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_entries(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataEntries(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MDataKeys> getKeys() {
        final CompletableFuture<MDataKeys> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_keys(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataKeys(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MDataValues> getValues() {
        final CompletableFuture<MDataValues> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_values(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataValues(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MDataPermissions> getPermissions() {
        final CompletableFuture<MDataPermissions> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_permissions(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataPermissions(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MDataPermissions> getUserPermissions(PublicSignKey signKey) {
        final CompletableFuture<MDataPermissions> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_list_user_permissions(appHandle, mDataInfoHandle, signKey.getHandle(), Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MDataPermissions(appHandle, handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<Void> deleteUserPermissions(PublicSignKey signKey, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_del_user_permissions(appHandle, mDataInfoHandle, signKey.getHandle(), version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> setUserPermissions(PublicSignKey signKey, MDataPermissionSet MDataPermissionSet, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_set_user_permissions(appHandle, mDataInfoHandle, signKey.getHandle(), MDataPermissionSet.getHandle(), version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> applyMutation(EntryMutationTransaction mutations) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_mutate_entries(appHandle, mDataInfoHandle, mutations.getHandle(), Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mDataBinding.mdata_info_free(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
    }

}
