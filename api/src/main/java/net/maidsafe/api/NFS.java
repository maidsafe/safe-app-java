package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.model.NFSFileMetadata;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;


public class NFS {
    private AppHandle appHandle;

    public NFS(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle appHandle) {
        this.appHandle = appHandle;
    }


    public CompletableFuture<NFSFileMetadata> getFileMetadata(final MDataInfo parentInfo, final String fileName) {
        final CompletableFuture<NFSFileMetadata> future = new CompletableFuture<>();
        NativeBindings.dirFetchFile(appHandle.toLong(), parentInfo, fileName,
                (result, ffiFile, version) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(new NFSFileMetadata(ffiFile, version));
                });
        return future;
    }


    public CompletableFuture insertFile(final MDataInfo parentInfo, final String fileName, final File file) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.dirInsertFile(appHandle.toLong(), parentInfo, fileName, file, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }


    public CompletableFuture updateFile(final MDataInfo parentInfo, final String fileName, final File file, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.dirUpdateFile(appHandle.toLong(), parentInfo, fileName,
                file, version, (result) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(null);
                });
        return future;
    }


    public CompletableFuture deleteFile(final MDataInfo parentInfo, final String fileName, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.dirDeleteFile(appHandle.toLong(), parentInfo, fileName, version, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<NativeHandle> fileOpen(final MDataInfo parentInfo, final File file, final NFS.OpenMode openMode) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.fileOpen(appHandle.toLong(), parentInfo, file, openMode.getValue(),
                (result, handle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(new NativeHandle(handle, (fileContext) -> {
                        // TODO implement free function once made available in safe_app
                    }));
                });
        return future;
    }


    public CompletableFuture<Long> getSize(final NativeHandle fileContextHandle) {
        final CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.fileSize(appHandle.toLong(), fileContextHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<byte[]> fileRead(final NativeHandle fileContextHandle, final long position, final long length) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.fileRead(appHandle.toLong(), fileContextHandle.toLong(), position, length,
                (result, data) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(data);
                });
        return future;
    }

    public CompletableFuture fileWrite(final NativeHandle fileContextHandle, final byte[] data) {
        final CompletableFuture<File> future = new CompletableFuture<>();
        NativeBindings.fileWrite(appHandle.toLong(), fileContextHandle.toLong(), data, (result) -> {
                if (result.getErrorCode() != 0) {
                    future.completeExceptionally(Helper.ffiResultToException(result));
                }
                future.complete(null);
        });
        return future;
    }

    public CompletableFuture<File> fileClose(final NativeHandle fileContextHandle) {
        final CompletableFuture<File> future = new CompletableFuture<>();
        NativeBindings.fileClose(appHandle.toLong(), fileContextHandle.toLong(),
                (result, ffiFile) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
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

        OpenMode(final int val) {
            this.val = val;
        }

        public int getValue() {
            return this.val;
        }
    }
}
