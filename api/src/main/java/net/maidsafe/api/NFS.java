package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.corba.se.spi.copyobject.CopierManager;
import net.maidsafe.api.model.NFSFileMetadata;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;



public class NFS {
    private AppHandle appHandle;

    public NFS(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<NFSFileMetadata> getFileMetadata(MDataInfo parentInfo, String fileName) {
        CompletableFuture<NFSFileMetadata> future = new CompletableFuture<>();
        NativeBindings.dirFetchFile(appHandle.toLong(), parentInfo, fileName,
                (result, ffiFile, version) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                        return;
                    }
                    future.complete(new NFSFileMetadata(ffiFile, version));
                });
        return future;
    }

    public CompletableFuture insertFile(MDataInfo parentInfo, String fileName, File file) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.dirInsertFile(appHandle.toLong(), parentInfo, fileName, file, (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture updateFile(MDataInfo parentInfo, String fileName, File file, long version) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.dirUpdateFile(appHandle.toLong(), parentInfo, fileName,
                    (net.maidsafe.safe_app.File) file, version, (result) -> {
                        if (result.getErrorCode() != 0) {
                            Helper.ffiResultToException(result);
                            return;
                        }
                    });
        });
        return future;
    }

    public CompletableFuture deleteFile(MDataInfo parentInfo, String fileName, File file, long version) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.dirDeleteFile(appHandle.toLong(), parentInfo, fileName, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture<NativeHandle> fileOpen(MDataInfo parentInfo, File file, NFS.OpenMode openMode) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.fileOpen(appHandle.toLong(), parentInfo, file, openMode.getValue(),
                (result, handle) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
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
        NativeBindings.fileSize(appHandle.toLong(), fileContextHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<byte[]> fileRead(NativeHandle fileContextHandle, long position, long length) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.fileRead(appHandle.toLong(), fileContextHandle.toLong(), position, length,
                (result, data) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                        return;
                    }
                    future.complete(data);
                });
        return future;
    }

    public CompletableFuture fileWrite(NativeHandle fileContextHandle, byte[] data) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.fileWrite(appHandle.toLong(), fileContextHandle.toLong(), data, (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture<File> fileClose(NativeHandle fileContextHandle) {
        CompletableFuture<File> future = new CompletableFuture<>();
        NativeBindings.fileClose(appHandle.toLong(), fileContextHandle.toLong(),
                (result, ffiFile) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
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
