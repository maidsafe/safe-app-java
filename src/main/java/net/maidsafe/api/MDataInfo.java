package net.maidsafe.api;


import com.sun.jna.Pointer;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.api.model.XorName;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;

public class MDataInfo {
    private final App app;
    private final MutableDataBinding mDataBinding;
    private final CallbackHelper callbackHelper;
    private final long mDataEntriesHandle;
    private final long mDataInfoHandle;

    public MDataInfo(final App app, long mDataEntriesHandle, long mDataInfoHandle) {
        this.app = app;
        this.mDataEntriesHandle = mDataEntriesHandle;
        this.mDataInfoHandle = mDataInfoHandle;
        this.mDataBinding = BindingFactory.getInstance().getMutableData();
        this.callbackHelper = CallbackHelper.getInstance();
    }

    public CompletableFuture<MutableData> getPublicType(byte[] name, long typeTag) {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_info_new_public(app.getAppHandle(), name, typeTag, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getPrivateType(XorName name, long typeTag, byte[] secretKey, byte[] nonce) {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_info_new_private(app.getAppHandle(), name.getRaw(), typeTag, secretKey, nonce, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getRandomPublicType(long typeTag) {
        return getRandom(typeTag, false);
    }

    public CompletableFuture<MutableData> getRandomPrivateType(long typeTag) {
        return getRandom(typeTag, true);
    }

    public CompletableFuture<byte[]> getEncryptKey(byte[] key){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_encrypt_entry_key(app.getAppHandle(), mDataInfoHandle, key, key.length, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<byte[]> getEncryptValue(byte[] value){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_encrypt_entry_value(app.getAppHandle(), mDataInfoHandle, value, value.length, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<byte[]> getDecrypt(byte[] input){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_decrypt(app.getAppHandle(), mDataInfoHandle, input, input.length, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<Pointer> getNameTypeTag(){
        final CompletableFuture<Pointer> future;
        future = new CompletableFuture<>();


        mDataBinding.mdata_info_extract_name_and_type_tag(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getPointerCallback(future));

        return future;
    }

    public CompletableFuture<byte[]> serialise(){
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        mDataBinding.mdata_info_serialise(app.getAppHandle(), mDataInfoHandle, Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<MutableData> deserialise(byte[] serialisedData) {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        mDataBinding.mdata_info_deserialise(app.getAppHandle(), serialisedData, serialisedData.length, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    private CompletableFuture<MutableData> getRandom(long typeTag, boolean isPrivate) {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        if (isPrivate) {
            mDataBinding.mdata_info_random_private(app.getAppHandle(), typeTag, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));
        } else {
            mDataBinding.mdata_info_random_public(app.getAppHandle(), typeTag, Pointer.NULL, callbackHelper.getHandleCallBack(cbFuture));
        }

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle, mDataEntriesHandle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

}
