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

import net.maidsafe.api.model.NFSFileMetadata;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.Helper;

/**
 * Network File System emulation API
 */
public class NFS {

    private AppHandle appHandle;

    public NFS(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle handle) {
        this.appHandle = handle;
    }

    /**
     * Get the metadata for the requested file
     * @param parentInfo Parent Mutable Data info as {@link MDataInfo}
     * @param fileName File name
     * @return File metadata as {@link NFSFileMetadata}
     */
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

    /**
     * Insert the file into the underlying Mutable Data. Directly commit to the network.
     * @param parentInfo Parent Mutable Data info
     * @param fileName File name
     * @param file File to be inserted
     */
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

    /**
     * Update an existing file on the network.
     * @param parentInfo Parent Mutable Data info
     * @param fileName File name
     * @param file The updated file to be stored
     * @param version The next version of the file
     * @return Returns the next version of the file
     */
    public CompletableFuture<Long> updateFile(final MDataInfo parentInfo, final String fileName,
                                        final File file, final long version) {
        final CompletableFuture<Long> future = new CompletableFuture<Long>();
        NativeBindings.dirUpdateFile(appHandle.toLong(), parentInfo, fileName,
                file, version, (result, nextVersion) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(nextVersion);
                });
        return future;
    }

    /**
     * Deletes a file that exists on the network
     * @param parentInfo Parent Mutable Data info
     * @param fileName File name
     * @param version The next version of the file
     * @return Returns the next version of the file
     */
    public CompletableFuture<Long> deleteFile(final MDataInfo parentInfo, final String fileName, final long version) {
        final CompletableFuture<Long> future = new CompletableFuture<Long>();
        NativeBindings.dirDeleteFile(appHandle.toLong(), parentInfo, fileName, version, (result, nextVersion) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(nextVersion);
        });
        return future;
    }

    /**
     * Open a file for reading or writing
     * @param parentInfo Parent Mutable Data info
     * @param file File object
     * @param openMode File opening mode
     * @return File handle as {@link NativeHandle}
     */
    public CompletableFuture<NativeHandle> fileOpen(final MDataInfo parentInfo, final File file,
                                                    final NFS.OpenMode openMode) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.fileOpen(appHandle.toLong(), parentInfo, file, openMode.getValue(),
                (result, handle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(new NativeHandle(handle, (fileContext) -> {
                    }));
                });
        return future;
    }

    /**
     * Get the size of a file that has been opened
     * @param fileContextHandle File handle as {@link NativeHandle}
     * @return Size of the file
     */
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

    /**
     * Read the data from a file
     * @param fileContextHandle File handle as {@link NativeHandle}
     * @param position Start position
     * @param length Length of the data to be read
     * @return File content as byte array
     */
    public CompletableFuture<byte[]> fileRead(final NativeHandle fileContextHandle, final long position,
                                              final long length) {
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

    /**
     * Write data to a file
     * @param fileContextHandle File handle as {@link NativeHandle}
     * @param data Data to be written
     */
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

    /**
     * Write the content to the network and close the file.
     * @param fileContextHandle File handle as {@link NativeHandle}
     * @return File object
     */
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

    /**
     * The different modes of opening a file in NFS
     */
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
