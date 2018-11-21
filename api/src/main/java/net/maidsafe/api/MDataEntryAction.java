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
 * Exposes API for Mutable Data entry transactions
 */
public class MDataEntryAction {

    private AppHandle appHandle;

    public MDataEntryAction(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle handle) {
        this.appHandle = handle;
    }

    /**
     * Get a new handle to perform Mutable Data entry actions
     * @return New entry {@link NativeHandle} instance
     */
    public CompletableFuture<NativeHandle> newEntryAction() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.mdataEntryActionsNew(appHandle.toLong(), (result, entriesH) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            final NativeHandle entriesActionHandle = new NativeHandle(entriesH, handle -> {
                NativeBindings.mdataEntryActionsFree(appHandle.toLong(), handle, res -> {
                });
            });
            future.complete(entriesActionHandle);
        });
        return future;
    }

    /**
     * Store a new insert action for a new entry
     * @param actionHandle The action handle as {@link NativeHandle}
     * @param key The key to be inserted
     * @param value The value to be inserted
     */
    public CompletableFuture insert(final NativeHandle actionHandle, final byte[] key, final byte[] value) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.mdataEntryActionsInsert(appHandle.toLong(), actionHandle.toLong(), key, value,
                (result) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(null);
                });
        return future;
    }

    /**
     * Store a new update action for updating an existing entry
     * @param actionHandle The action handle as {@link NativeHandle}
     * @param key The key to be updated
     * @param value The new value
     * @param version The next version of the entry
     */
    public CompletableFuture update(final NativeHandle actionHandle, final byte[] key,
                                    final byte[] value, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.mdataEntryActionsUpdate(appHandle.toLong(), actionHandle.toLong(), key, value,
                version, (result) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(null);
                });
        return future;
    }

    /**
     * Store a new delete action to remove an existing entry
     * @param actionHandle The action handle as {@link NativeHandle}
     * @param key The key of the entry to be deleted
     * @param version The next version of the entry
     */
    public CompletableFuture<Void> delete(final NativeHandle actionHandle, final byte[] key, final long version) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.mdataEntryActionsDelete(appHandle.toLong(), actionHandle.toLong(), key,
                version, (result) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(null);
                });
        return future;
    }
}
