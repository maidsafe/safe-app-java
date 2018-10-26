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



public class CipherOpt {

    private static AppHandle appHandle;

    public CipherOpt(final AppHandle appHandle) {
        init(appHandle);
    }

    private void init(final AppHandle appHandle) {
        this.appHandle = appHandle;
    }


    private NativeHandle getNativeHandle(final long handle) {
        return new NativeHandle(handle, (cipherOpt) -> {
            NativeBindings.cipherOptFree(appHandle.toLong(), cipherOpt, (res) -> {
            });
        });
    }

    public CompletableFuture<NativeHandle> getPlainCipherOpt() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewPlaintext(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getSymmetricCipherOpt() {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewSymmetric(appHandle.toLong(), (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(getNativeHandle(handle));
        });
        return future;
    }

    public CompletableFuture<NativeHandle> getAsymmetricCipherOpt(final NativeHandle publicEncryptKey) {
        final CompletableFuture<NativeHandle> future = new CompletableFuture<>();
        NativeBindings.cipherOptNewAsymmetric(appHandle.toLong(), publicEncryptKey.toLong(),
                (result, handle) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(getNativeHandle(handle));
                });
        return future;
    }
}
