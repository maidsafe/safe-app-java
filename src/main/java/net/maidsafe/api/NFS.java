package net.maidsafe.api;

import com.sun.jna.Pointer;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.api.model.NfsFile;
import net.maidsafe.api.model.ValueVersion;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.NfsBinding;
import net.maidsafe.binding.model.NFSFile;
import net.maidsafe.utils.CallbackHelper;

import java.util.concurrent.CompletableFuture;


public class NFS {
    private final MutableData mData;
    private final Pointer appHandle;
    private final NfsBinding nfsBinding;
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public NFS(Pointer appHandle, MutableData mData) {
        this.appHandle= appHandle;
        this.mData = mData;
        this.nfsBinding = BindingFactory.getInstance().getNfs();
    }

    public CompletableFuture<NfsFile> fetch(String fileName) {
        final CompletableFuture<NfsFile> future;
        future = new CompletableFuture<>();

        nfsBinding.file_fetch(appHandle, mData.getMutableDataInfoHandle(), fileName, Pointer.NULL, callbackHelper.getFileFetchCallback(future));

        return future;
    }

    public CompletableFuture<Void> insert(String fileName, byte[] file) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        nfsBinding.file_insert(appHandle, mData.getMutableDataInfoHandle(), fileName, file, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<Void> update(String fileName, byte[] file, long version) {
        final CompletableFuture<Void> future;
        future = new CompletableFuture<>();

        nfsBinding.file_update(appHandle, mData.getMutableDataInfoHandle(), fileName, file, version, Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }
}
