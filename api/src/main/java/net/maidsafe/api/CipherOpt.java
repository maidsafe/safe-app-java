package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.lang.annotation.Native;
import java.util.concurrent.Future;

class CipherOpt {

    private static AppHandle appHandle;

    public CipherOpt(AppHandle appHandle) {
        this.appHandle = appHandle;
    }


    private NativeHandle getNativeHandle(long handle) {
        return new NativeHandle(handle, (cipherOpt) -> {
            NativeBindings.cipherOptFree(appHandle.toLong(), cipherOpt, (res) -> {
            });
        });
    }

    public Future<NativeHandle> getPlainCipherOpt() {
        return Executor.getInstance().submit(new CallbackHelper(binder -> {
            NativeBindings.cipherOptNewPlaintext(appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }

    public Future<NativeHandle> getSymmetricCipherOpt() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.cipherOptNewSymmetric(appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }

    public Future<NativeHandle> getAsymmetricCipherOpt(NativeHandle publicEncryptKey) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.cipherOptNewAsymmetric(appHandle.toLong(), publicEncryptKey.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }
}
