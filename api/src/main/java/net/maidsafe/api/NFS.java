package net.maidsafe.api;

import net.maidsafe.model.NFSFileMetadata;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

import java.util.concurrent.CompletableFuture;

public class NFS {

    public static CompletableFuture<NFSFileMetadata> getFileMetadata(MDataInfo parentInfo, String fileName) {
        CompletableFuture<NFSFileMetadata> future = new CompletableFuture<>();
        NativeBindings.dirFetchFile(BaseSession.appHandle.toLong(), parentInfo, fileName, (result, ffiFile, version) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NFSFileMetadata(ffiFile, version));
        });
        return future;
    }

    public static CompletableFuture<Void> insertFileMetadata(MDataInfo parentInfo, String fileName, NFSFileMetadata file) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.dirInsertFile(BaseSession.appHandle.toLong(), parentInfo, fileName, (File) file, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> updateFileMetadata(MDataInfo parentInfo, String fileName, NFSFileMetadata file) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.dirUpdateFile(BaseSession.appHandle.toLong(), parentInfo, fileName, (net.maidsafe.safe_app.File) file, file.getVersion(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> deleteFileMetadata(MDataInfo parentInfo, String fileName, NFSFileMetadata file) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.dirDeleteFile(BaseSession.appHandle.toLong(), parentInfo, fileName, file.getVersion(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<NativeHandle> fileOpen(MDataInfo parentInfo, File file, NFS.OpenMode openMode) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.fileOpen(BaseSession.appHandle.toLong(), parentInfo, file, openMode.getValue(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NativeHandle(handle, (fileContext) -> {
                // TODO implement free function once made available in safe_app
            }));
        });
        return future;
    }

    public CompletableFuture<Long> getSize(NativeHandle fileContextHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.fileSize(BaseSession.appHandle.toLong(), fileContextHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<byte[]> fileRead(NativeHandle fileContextHandle, long position, long length) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.fileRead(BaseSession.appHandle.toLong(), fileContextHandle.toLong(), position, length, (result, data) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(data);
        });
        return future;
    }

    public CompletableFuture<Void> fileWrite(NativeHandle fileContextHandle, byte[] data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.fileWrite(BaseSession.appHandle.toLong(), fileContextHandle.toLong(), data, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<File> fileClose(NativeHandle fileContextHandle) {
        CompletableFuture<File> future = new CompletableFuture<>();
        NativeBindings.fileClose(BaseSession.appHandle.toLong(), fileContextHandle.toLong(), (result, ffiFile) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NFSFileMetadata(ffiFile, 0));
        });
        return future;
    }

    public enum OpenMode {
        OVER_WRITE(1),
        APPEND(2),
        READ(4);
        private int val;

        OpenMode(int val) {
            this.val = val;
        }

        public int getValue() {
            return this.val;
        }
    }
}
