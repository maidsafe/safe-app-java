package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.concurrent.Future;

public class CipherOpt {

    private static NativeHandle getNativeHandle(long handle) {
        return new NativeHandle(handle, (cipherOpt) -> {
            NativeBindings.cipherOptFree(BaseSession.appHandle.toLong(), cipherOpt, (res) -> {
            });
        });
    }

    public static Future<NativeHandle> getPlainCipherOpt() {
        return Executor.getInstance().submit(new CallbackHelper(binder -> {
            NativeBindings.cipherOptNewPlaintext(BaseSession.appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }

    public static Future<NativeHandle> getSymmetricCipherOpt() {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.cipherOptNewSymmetric(BaseSession.appHandle.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }

    public static Future<NativeHandle> getAsymmetricCipherOpt(NativeHandle publicEncryptKey) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.cipherOptNewAsymmetric(BaseSession.appHandle.toLong(), publicEncryptKey.toLong(), (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(getNativeHandle(handle));
            });
        }));
    }
}
