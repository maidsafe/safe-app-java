// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;


public class IData {
    private static AppHandle appHandle;


    public IData(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle handle) {
        this.appHandle = handle;
    }


    public CompletableFuture<NativeHandle> getWriter() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
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


    public CompletableFuture<Void> write(final NativeHandle writerHandle, final byte[] data) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.idataWriteToSelfEncryptor(appHandle.toLong(), writerHandle.toLong(), data,
                (result) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(null);
                });
        return future;
    }


    public CompletableFuture<byte[]> close(final NativeHandle writerHandle, final NativeHandle cipherOptHandle) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataCloseSelfEncryptor(appHandle.toLong(), writerHandle.toLong(),
                cipherOptHandle.toLong(), (result, name) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(name);
                });
        return future;
    }


    public CompletableFuture<NativeHandle> getReader(final byte[] address) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataFetchSelfEncryptor(appHandle.toLong(), address, (result, readerHandle) -> {
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


    public CompletableFuture<byte[]> read(final NativeHandle readerHandle, final long position, final long length) {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        NativeBindings.idataReadFromSelfEncryptor(appHandle.toLong(), readerHandle.toLong(),
                position, length, (result, data) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(data);
                });
        return future;
    }


    public CompletableFuture<Long> getSize(final NativeHandle readerHandle) {
        final CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSize(appHandle.toLong(), readerHandle.toLong(), (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }


    public CompletableFuture<Long> getSerialisedSize(final byte[] address) {
        final CompletableFuture<Long> future = new CompletableFuture<>();
        NativeBindings.idataSerialisedSize(appHandle.toLong(), address, (result, size) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(size);
        });
        return future;
    }
}
