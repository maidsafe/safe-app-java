package net.maidsafe.api;

import net.maidsafe.api.model.NFSFileMetadata;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

public class NFS {
    private AppHandle appHandle;

    public NFS(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<NFSFileMetadata> getFileMetadata(MDataInfo parentInfo, String fileName) {
        return Executor.getInstance().submit(new CallbackHelper<NFSFileMetadata>(binder -> {
            NativeBindings.dirFetchFile(appHandle.toLong(), parentInfo, fileName, (result, ffiFile, version) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NFSFileMetadata(ffiFile, version));
            });
        }));
    }

    public Future<Void> insertFile(MDataInfo parentInfo, String fileName, File file) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.dirInsertFile(appHandle.toLong(), parentInfo, fileName, file, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Void> updateFile(MDataInfo parentInfo, String fileName, File file, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.dirUpdateFile(appHandle.toLong(), parentInfo, fileName, (net.maidsafe.safe_app.File) file, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<Void> deleteFile(MDataInfo parentInfo, String fileName, File file, long version) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.dirDeleteFile(appHandle.toLong(), parentInfo, fileName, version, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<NativeHandle> fileOpen(MDataInfo parentInfo, File file, NFS.OpenMode openMode) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.fileOpen(appHandle.toLong(), parentInfo, file, openMode.getValue(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(handle, (fileContext) -> {
                    // TODO implement free function once made available in safe_app
                }));
            });
        }));
    }

    public Future<Long> getSize(NativeHandle fileContextHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.fileSize(appHandle.toLong(), fileContextHandle.toLong(), (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }

    public Future<byte[]> fileRead(NativeHandle fileContextHandle, long position, long length) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.fileRead(appHandle.toLong(), fileContextHandle.toLong(), position, length, (result, data) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(data);
            });
        }));
    }

    public Future<Void> fileWrite(NativeHandle fileContextHandle, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.fileWrite(appHandle.toLong(), fileContextHandle.toLong(), data, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<File> fileClose(NativeHandle fileContextHandle) {
        return Executor.getInstance().submit(new CallbackHelper<File>(binder -> {
            NativeBindings.fileClose(appHandle.toLong(), fileContextHandle.toLong(), (result, ffiFile) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NFSFileMetadata(ffiFile, 0));
            });
        }));
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
