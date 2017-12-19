package net.maidsafe.api;

import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

import java.util.concurrent.CompletableFuture;

public class IData {

    public CompletableFuture<NativeHandle> getWriter() {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataNewSelfEncryptor(BaseSession.appHandle.toLong(), (result, writerHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NativeHandle(writerHandle, (handle) -> {
                NativeBindings.idataSelfEncryptorWriterFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<Void> write(NativeHandle writerHandle, byte[] data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.idataWriteToSelfEncryptor(BaseSession.appHandle.toLong(), writerHandle.toLong(), data, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<byte[]> close(NativeHandle writerHandle, NativeHandle cipherOptHandle) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataCloseSelfEncryptor(BaseSession.appHandle.toLong(), writerHandle.toLong(), cipherOptHandle.toLong(), (result, name) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(name);
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getReader(byte[] name) {
        CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataFetchSelfEncryptor(BaseSession.appHandle.toLong(), name, (result, readerHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new NativeHandle(readerHandle, (handle) -> {
                NativeBindings.idataSelfEncryptorWriterFree(BaseSession.appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    public CompletableFuture<byte[]> read(NativeHandle readerHandle, long position, long length) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataReadFromSelfEncryptor(BaseSession.appHandle.toLong(), readerHandle.toLong(), position, length, (result, data) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(data);
        });
        return future;
    }

    public CompletableFuture<Long> getSize(NativeHandle readerHandle) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSize(BaseSession.appHandle.toLong(), readerHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }

    public CompletableFuture<Long> getSerialisedSize(byte[] name) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSerialisedSize(BaseSession.appHandle.toLong(), name, (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(size);
        });
        return future;
    }
}
