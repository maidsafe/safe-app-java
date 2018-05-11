package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

class IData {
    private static AppHandle appHandle;

    public IData(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public Future<NativeHandle> getWriter() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.idataNewSelfEncryptor(appHandle.toLong(), (result, writerHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(writerHandle, (handle) -> {
                    NativeBindings.idataSelfEncryptorWriterFree(appHandle.toLong(), handle, res -> {
                    });
                }));
            });
        }));
    }

    public Future<Void> write(NativeHandle writerHandle, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.idataWriteToSelfEncryptor(appHandle.toLong(), writerHandle.toLong(), data, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<byte[]> close(NativeHandle writerHandle, NativeHandle cipherOptHandle) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.idataCloseSelfEncryptor(appHandle.toLong(), writerHandle.toLong(), cipherOptHandle.toLong(), (result, name) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(name);
            });
        }));
    }

    public Future<NativeHandle> getReader(byte[] name) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.idataFetchSelfEncryptor(appHandle.toLong(), name, (result, readerHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(readerHandle, (handle) -> {
                    NativeBindings.idataSelfEncryptorWriterFree(appHandle.toLong(), handle, res -> {
                    });
                }));
            });
        }));
    }

    public Future<byte[]> read(NativeHandle readerHandle, long position, long length) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.idataReadFromSelfEncryptor(appHandle.toLong(), readerHandle.toLong(), position, length, (result, data) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(data);
            });
        }));
    }

    public Future<Long> getSize(NativeHandle readerHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.idataSize(appHandle.toLong(), readerHandle.toLong(), (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }

    public Future<Long> getSerialisedSize(byte[] name) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.idataSerialisedSize(appHandle.toLong(), name, (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }
}
