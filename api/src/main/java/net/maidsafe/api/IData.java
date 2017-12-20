package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

public class IData {

    public Future<NativeHandle> getWriter() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.idataNewSelfEncryptor(BaseSession.appHandle.toLong(), (result, writerHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(writerHandle, (handle) -> {
                    NativeBindings.idataSelfEncryptorWriterFree(BaseSession.appHandle.toLong(), handle, res -> {
                    });
                }));
            });
        }));
    }

    public Future<Void> write(NativeHandle writerHandle, byte[] data) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.idataWriteToSelfEncryptor(BaseSession.appHandle.toLong(), writerHandle.toLong(), data, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(null);
            });
        }));
    }

    public Future<byte[]> close(NativeHandle writerHandle, NativeHandle cipherOptHandle) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.idataCloseSelfEncryptor(BaseSession.appHandle.toLong(), writerHandle.toLong(), cipherOptHandle.toLong(), (result, name) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(name);
            });
        }));
    }

    public Future<NativeHandle> getReader(byte[] name) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.idataFetchSelfEncryptor(BaseSession.appHandle.toLong(), name, (result, readerHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(readerHandle, (handle) -> {
                    NativeBindings.idataSelfEncryptorWriterFree(BaseSession.appHandle.toLong(), handle, res -> {
                    });
                }));
            });
        }));
    }

    public Future<byte[]> read(NativeHandle readerHandle, long position, long length) {
        return Executor.getInstance().submit(new CallbackHelper<byte[]>(binder -> {
            NativeBindings.idataReadFromSelfEncryptor(BaseSession.appHandle.toLong(), readerHandle.toLong(), position, length, (result, data) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(data);
            });
        }));
    }

    public Future<Long> getSize(NativeHandle readerHandle) {
        return Executor.getInstance().submit(new CallbackHelper<Long>(binder -> {
            NativeBindings.idataSize(BaseSession.appHandle.toLong(), readerHandle.toLong(), (result, size) -> {
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
            NativeBindings.idataSerialisedSize(BaseSession.appHandle.toLong(), name, (result, size) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(size);
            });
        }));
    }
}
