package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.utils.Helper;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;

import java.util.concurrent.Future;

public class TestHelper {

    public static Future<NativeHandle> createTestApp(String appId) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.testCreateApp(appId, (result, appHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(appHandle, (handle) -> {
                    NativeBindings.appFree(handle);
                }));
            });
        }));
    }


    public static Future<NativeHandle> createTestAppWithAccess(AuthReq authReq) {
        return Executor.getInstance().submit(new CallbackHelper<NativeHandle>(binder -> {
            NativeBindings.testCreateAppWithAccess(authReq, (result, appHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new NativeHandle(appHandle, (handle) -> {
                    NativeBindings.appFree(handle);
                }));
            });
        }));
    }
}
