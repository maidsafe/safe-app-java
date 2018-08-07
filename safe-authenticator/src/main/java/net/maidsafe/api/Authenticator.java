package net.maidsafe.api;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.Request;
import net.maidsafe.models.IpcRequest;
import net.maidsafe.safe_authenticator.*;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import javax.naming.AuthenticationNotSupportedException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Authenticator {
    private static long auth;

    Authenticator(long auth) {
        this.auth = auth;
    }

    public boolean isMock() {
        return NativeBindings.isMockBuild();
    }

    public static CompletableFuture<Authenticator> createAccount(String accountLocator, String accountPassword, String invitation) {
        CompletableFuture<Authenticator> future = new CompletableFuture<>();
        AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
        NativeBindings.createAcc(accountLocator, accountPassword, invitation, disconnectListener.getCallback(), (result, authenticator) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(new Authenticator(authenticator));
        });
        return future;
    }

    public CompletableFuture<Authenticator> login(String accountLocator, String accountPassword) {
        CompletableFuture<Authenticator> future = new CompletableFuture<>();
        AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
        NativeBindings.login(accountLocator, accountPassword, disconnectListener.getCallback(), (result, authenticator) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(new Authenticator(authenticator));
        });
        return future;
    }

    public CompletableFuture<List<AppExchangeInfo>> listRevokedApps(long auth) {
        CompletableFuture<List<AppExchangeInfo>> future = new CompletableFuture<>();
        NativeBindings.authRevokedApps(auth, (result, appExchangeInfo) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(Arrays.asList(appExchangeInfo));
        });
        return future;
    }

    public CompletableFuture flushAppRevocationQueue(long auth) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.authFlushAppRevocationQueue(auth, result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                }
            });
        });
        return future;
    }

    public static  CompletableFuture<String> encodeUnregisteredResponse(Request request, boolean isGranted) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeUnregisteredResp(request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(response);
        });
        return future;
    }

    public static CompletableFuture<String> encodeAuthResponse(AuthReq authReq, Request request, boolean isGranted) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeAuthResp(auth, authReq, request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(response);
        });
        return future;
    }

    public CompletableFuture<String> encodeContainersResponse(long auth, ContainersReq containersReq, Request request, boolean isGranted) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeContainersResp(auth, containersReq, request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(response);
        });
        return future;
    }

    public CompletableFuture initLogging(String outputFileNameOverride) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.authInitLogging(outputFileNameOverride, result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture<String> outputLogPath(String outputFileName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authOutputLogPath(outputFileName, (result, path) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(path);
        });
        return future;
    }

    public CompletableFuture reconnect(long auth) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.authReconnect(auth, result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture<AccountInfo> getAccountInfo(long auth) {
        CompletableFuture<AccountInfo> future = new CompletableFuture<>();
        NativeBindings.authAccountInfo(auth, (result, accountInfo) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(accountInfo);
        });
        return future;
    }

    public CompletableFuture<String> executeFileStem() {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authExeFileStem((result, appName) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(appName);
        });
        return future;
    }

    public CompletableFuture setAdditionalSearchPath(String newPath) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.authSetAdditionalSearchPath(newPath, result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public void free(long auth) {
        NativeBindings.authFree(auth);
    }

    public CompletableFuture removeRevokedApp(long auth, App app) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.authRmRevokedApp(auth, app.getId(), result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                }
            });
        });
        return future;
    }

    public CompletableFuture<List<RegisteredApp>> getRegisteredApps(long auth) {
        CompletableFuture<List<RegisteredApp>> future = new CompletableFuture<>();
        CallbackResultRegisteredAppArrayLen callback = (result, registeredApp) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            Arrays.asList(registeredApp);
        };
        NativeBindings.authRegisteredApps(auth, callback);
        return future;
    }

    public CompletableFuture<List<AppAccess>> getAppsWithMDataAccess(long auth, MDataInfo mDataInfo) {
        CompletableFuture<List<AppAccess>> future = new CompletableFuture<>();
        NativeBindings.authAppsAccessingMutableData(auth, mDataInfo.getName(), mDataInfo.getTypeTag(), (result, appAccess) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(Arrays.asList(appAccess));
        });
        return future;
    }

    public CompletableFuture<String> encodeShareMDataResponse(long auth, ShareMDataReq shareMDataReq, Request request, boolean isGranted) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataResp(auth, shareMDataReq, request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(response);
        });
        return future;
    }

    public CompletableFuture<String> revokeApp(long auth, App app) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authRevokeApp(auth, app.getId(), (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(response);
        });
        return future;
    }

    public CompletableFuture<List<RegisteredApp>> listRegisteredApps(long auth) {
        CompletableFuture<List<RegisteredApp>> future = new CompletableFuture<>();
        NativeBindings.authRegisteredApps(auth, (result, registeredApp) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(Arrays.asList(registeredApp));
        });
        return future;
    }

    public CompletableFuture<IpcRequest> decodeIpcMessage(String message) {
        CompletableFuture<IpcRequest> future = new CompletableFuture<>();
        CallbackIntAuthReq callbackIntAuthReq = (reqId, req) -> {
            future.complete(new IpcRequest.AuthIpcRequest(reqId, req));
        };
        CallbackIntContainersReq callbackIntContainersReq = (reqId, req) -> {
            future.complete(new IpcRequest.ContainersIpcReq(reqId, req));
        };
        CallbackIntByteArrayLen callbackIntByteArrayLen = (reqId, extraData) -> {
            future.complete(new IpcRequest.UnregisteredIpcRequest(reqId, extraData));
        };
        CallbackIntShareMDataReqMetadataResponse callbackIntShareMDataReqMetadataResponse = (reqId, req, metadata) ->  {
            future.complete(new IpcRequest.ShareMDataIpcRequest(reqId, req, metadata));
        };
        NativeBindings.authDecodeIpcMsg(auth, message, callbackIntAuthReq, callbackIntContainersReq, callbackIntByteArrayLen, callbackIntShareMDataReqMetadataResponse, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.complete(new IpcRequest.IpcReqError(result.getErrorCode(), result.getDescription(), response));
                return;
            }
            future.complete(new IpcRequest.IpcReqRejected(response));
        });
        return future;
    }

    public static CompletableFuture<IpcRequest> unregisteredDecodeIpcMessage(String message) {
        CompletableFuture<IpcRequest> future = new CompletableFuture<>();
        CallbackIntByteArrayLen callback = (reqId, extraData) -> {
            future.complete(new IpcRequest.UnregisteredIpcRequest(reqId, extraData));
        };
        NativeBindings.authUnregisteredDecodeIpcMsg(message, callback, (result, response) -> {
            if (result.getErrorCode() != 0) {
                Helper.ffiResultToException(result);
                return;
            }
            future.complete(new IpcRequest.IpcReqError(result.getErrorCode(), result.getDescription(), response));
        });
        return future;
    }

    public static void dispose() {
        NativeBindings.authFree(auth);
    }

}