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

/**
 * Exposes API for Immutable Data operations
 */
public class IData {
    private static AppHandle appHandle;

    /**
     * Initialises a new instance of the IData class
     * @param appHandle App handle
     */
    public IData(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle handle) {
        this.appHandle = handle;
    }

    /**
     * Initialises a new self encryptor writer handle
     * @return New Self encryptor {@link NativeHandle} instance
     */
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

    /**
     * Write data to the self encryptor.
     * Data will be written to the network once the close is invoked.
     * @param writerHandle Self encryptor handle as {@link NativeHandle}
     * @param data Data to be self encrypted
     */
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

    /**
     * Closes the self encryptor and write the Immutable Data to the network
     * @param writerHandle Self encryptor handle as {@link NativeHandle}
     * @param cipherOptHandle Cipher options as {@link NativeHandle}
     * @return Address of the Immutable Data as byte array
     */
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

    /**
     * Get self encryptor reader for existent data on the network
     * @param address Address of the data as byte array
     * @return New Self encryptor {@link NativeHandle}
     */
    public CompletableFuture<NativeHandle> getReader(final byte[] address) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.idataFetchSelfEncryptor(appHandle.toLong(), address, (result, readerHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new NativeHandle(readerHandle, (handle) -> {
                NativeBindings.idataSelfEncryptorReaderFree(appHandle.toLong(), handle, res -> {
                });
            }));
        });
        return future;
    }

    /**
     * Read data from the self encryptor
     * @param readerHandle Self encryptor reader as {@link NativeHandle}
     * @param position Position from which data is to be read
     * @param length Length of the data to be read
     * @return Data as byte array
     */
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

    /**
     * Get the data size from the self encryptor
     * @param readerHandle Self encryptor reader as {@link NativeHandle}
     * @return Size of the data
     */
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

    /**
     * Get size of the serialised Immutable Data
     * @param address Address of the Immutable Data
     * @return Size of the serialised Immutable Data
     */
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
