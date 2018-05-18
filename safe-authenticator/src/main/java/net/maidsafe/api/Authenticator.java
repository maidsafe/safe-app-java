package net.maidsafe.api;
import net.maidsafe.api.model.App;
import net.maidsafe.models.IpcRequest;
import net.maidsafe.api.model.Request;
import net.maidsafe.safe_authenticator.*;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
class Authenticator {
    private static long auth;
    Authenticator(long auth) {
        this.auth = auth;
    }
    @Override
    public void finalize(){
        NativeBindings.authFree(auth);
    }
    public boolean isMock() {
        return NativeBindings.isMockBuild();
    }
    public static Future<Authenticator> createAccount(String accountLocator, String accountPassword, String invitation) {
        return Executor.getInstance().submit(new CallbackHelper<Authenticator>(binder -> {
            AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
            NativeBindings.createAcc(accountLocator, accountPassword, invitation, disconnectListener.getCallback(), (result, authenticator) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new Authenticator(authenticator));
            });
        }));
    }
    public Future<Authenticator> login(String accountLocator, String accountPassword) {
        return Executor.getInstance().submit(new CallbackHelper<Authenticator>(binder -> {
            AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
            NativeBindings.login(accountLocator, accountPassword, disconnectListener.getCallback(), (result, authenticator) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new Authenticator(authenticator));
            });
        }));
    }
    public Future<List<AppExchangeInfo>> listRevokedApps(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<List<AppExchangeInfo>>(binder -> {
            NativeBindings.authRevokedApps(auth, (result, appExchangeInfo) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(appExchangeInfo));
            });
        }));
    }
    public Future<Void> flushAppRevocationQueue(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.authFlushAppRevocationQueue(auth, result -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
            });
        }));
    }
    public Future<String> encodeUnregisteredResponse(Request request, boolean isGranted) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.encodeUnregisteredResp(request.getReqId(), isGranted, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(response);
            });
        }));
    }
    public Future<String> encodeAuthResponse(AuthReq authReq, Request request, boolean isGranted) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.encodeAuthResp(auth, authReq, request.getReqId(), isGranted, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(response);
            });
        }));
    }
    public Future<String> encodeContainersResponse(long auth, ContainersReq containersReq, Request request, boolean isGranted) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.encodeContainersResp(auth, containersReq, request.getReqId(), isGranted, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(response);
            });
        }));
    }
    public Future<Void> initLogging(String outputFileNameOverride) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.authInitLogging(outputFileNameOverride, result -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }
    public Future<String> outputLogPath(String outputFileName) {
        return  Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.authOutputLogPath(outputFileName, (result, path) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(path);
            });
        }));
    }
    public Future<Void> reconnect(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.authReconnect(auth, result -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }
    public Future<AccountInfo> getAccountInfo(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<AccountInfo>(binder -> {
            NativeBindings.authAccountInfo(auth, (result, accountInfo) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(accountInfo);
            });
        }));
    }
    public Future<String> executeFileStem() {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.authExeFileStem((result, appName) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(appName);
            });
        }));
    }
    public Future<Void> setAdditionalSearchPath(String newPath) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.authSetAdditionalSearchPath(newPath, result -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }
    public void free(long auth) {
        NativeBindings.authFree(auth);
    }
    public Future<Void> removeRevokedApp(long auth, App app) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.authRmRevokedApp(auth, app.getId(), result -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                }
                binder.onResult(null);
            });
        }));
    }
    public Future<List<RegisteredApp>> getRegisteredApps(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<List<RegisteredApp>>(binder -> {
            CallbackResultRegisteredAppArrayLen callback = (result, registeredApp) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(registeredApp));
            };
            NativeBindings.authRegisteredApps(auth, callback);
        }));
    }
    public Future<List<AppAccess>> getAppsWithMDataAccess(long auth, MDataInfo mDataInfo) {
        return Executor.getInstance().submit(new CallbackHelper<List<AppAccess>>(binder -> {
            NativeBindings.authAppsAccessingMutableData(auth, mDataInfo.getName(), mDataInfo.getTypeTag(), (result, appAccess) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(appAccess));
            });
        }));
    }
    public Future<String> encodeShareMDataResponse(long auth, ShareMDataReq shareMDataReq, Request request, boolean isGranted) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.encodeShareMdataResp(auth, shareMDataReq, request.getReqId(), isGranted, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(response);
            });
        }));
    }
    public Future<String> revokeApp(long auth, App app) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.authRevokeApp(auth, app.getId(), (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(response);
            });
        }));
    }
    public Future<List<RegisteredApp>> listRegisteredApps(long auth) {
        return Executor.getInstance().submit(new CallbackHelper<List<RegisteredApp>>(binder -> {
            NativeBindings.authRegisteredApps(auth, (result, registeredApp) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(registeredApp));
            });
        }));
    }
    public Future<IpcRequest> decodeIpcMessage(String message) {
        return Executor.getInstance().submit(new CallbackHelper<IpcRequest>(binder -> {
            CallbackIntAuthReq callbackIntAuthReq = (reqId, req) -> binder.onResult(new IpcRequest.AuthIpcRequest(reqId, req));
            CallbackIntContainersReq callbackIntContainersReq = (reqId, req) -> binder.onResult(new IpcRequest.ContainersIpcReq(reqId, req));
            CallbackIntByteArrayLen callbackIntByteArrayLen = (reqId, extraData) -> binder.onResult(new IpcRequest.UnregisteredIpcRequest(reqId, extraData));
            CallbackIntShareMDataReqMetadataResponse callbackIntShareMDataReqMetadataResponse = (reqId, req, metadata) -> binder.onResult(new IpcRequest.ShareMDataIpcRequest(reqId, req, metadata));
            NativeBindings.authDecodeIpcMsg(auth, message, callbackIntAuthReq, callbackIntContainersReq, callbackIntByteArrayLen, callbackIntShareMDataReqMetadataResponse, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onResult(new IpcRequest.IpcReqError(result.getErrorCode(), result.getDescription(), response));
                    return;
                }
                binder.onResult(new IpcRequest.IpcReqRejected(response));
            });
        }));
    }
    public Future<IpcRequest> unregisteredDecodeIpcMessage(String message) {
        return Executor.getInstance().submit(new CallbackHelper<IpcRequest>(binder -> {
            CallbackIntByteArrayLen callback = (reqId, extraData) -> {
                binder.onResult(new IpcRequest.UnregisteredIpcRequest(reqId, extraData));
            };
            NativeBindings.authUnregisteredDecodeIpcMsg(message, callback, (result, response) -> {
                if(result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new IpcRequest.IpcReqError(result.getErrorCode(), result.getDescription(), response));
            });
        }));
    }
}