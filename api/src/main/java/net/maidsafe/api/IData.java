package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;



public class IData {
    private static AppHandle appHandle;

    public IData(AppHandle appHandle) {
        this.appHandle = appHandle;
    }

    public CompletableFuture<NativeHandle> getWriter() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataNewSelfEncryptor(appHandle.toLong(), (result, writerHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new NativeHandle(writerHandle, (handle) -> {
                NativeBindings.idataSelfEncryptorWriterFree(appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<Void> write(NativeHandle writerHandle, byte[] data) {
        CompletableFuture future = new CompletableFuture();
            NativeBindings.idataWriteToSelfEncryptor(appHandle.toLong(), writerHandle.toLong(), data,
                    (result) -> {
                        if (result.getErrorCode() != 0) {
                            future.completeExceptionally(Helper.ffiResultToException(result));
                        }
                        future.complete(null);
                    });
        return future;
    }

    public CompletableFuture<byte[]> close(NativeHandle writerHandle, NativeHandle cipherOptHandle) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataCloseSelfEncryptor(appHandle.toLong(), writerHandle.toLong(),
                cipherOptHandle.toLong(), (result, name) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                    }
                    future.complete(name);
                });
        return future;
    }

    public CompletableFuture<NativeHandle> getReader(byte[] name) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataFetchSelfEncryptor(appHandle.toLong(), name, (result, readerHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new NativeHandle(readerHandle, (handle) -> {
                NativeBindings.idataSelfEncryptorWriterFree(appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<byte[]> read(NativeHandle readerHandle, long position, long length) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataReadFromSelfEncryptor(appHandle.toLong(), readerHandle.toLong(),
                position, length, (result, data) -> {
                    if (result.getErrorCode() != 0) {
                        Helper.ffiResultToException(result);
                    }
                    future.complete(data);
                });
        return future;
    }

    public CompletableFuture<Long> getSize(NativeHandle readerHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSize(appHandle.toLong(), readerHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<Long> getSerialisedSize(byte[] name) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSerialisedSize(appHandle.toLong(), name, (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }
}
