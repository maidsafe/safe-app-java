package net.maidsafe.api;

import net.maidsafe.listener.OnDisconnected;
import net.maidsafe.model.App;
import net.maidsafe.model.AuthResponse;
import net.maidsafe.model.ContainerResponse;
import net.maidsafe.model.DecodeError;
import net.maidsafe.model.DecodeResult;
import net.maidsafe.model.Request;
import net.maidsafe.model.RevokedResponse;
import net.maidsafe.model.ShareMutableDataResponse;
import net.maidsafe.model.UnregisteredClientResponse;
import net.maidsafe.safe_app.AccountInfo;
import net.maidsafe.safe_app.AuthGranted;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.safe_app.CallbackInt;
import net.maidsafe.safe_app.CallbackIntAuthGranted;
import net.maidsafe.safe_app.CallbackIntByteArrayLen;
import net.maidsafe.safe_app.CallbackResultApp;
import net.maidsafe.safe_app.CallbackResultInt;
import net.maidsafe.safe_app.CallbackResultIntString;
import net.maidsafe.safe_app.CallbackVoid;
import net.maidsafe.safe_app.ContainerPermission;
import net.maidsafe.safe_app.ContainersReq;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.ShareMDataReq;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BaseSession {

    public static CallbackVoid disconnectedCb;
    public static NativeHandle appHandle = new NativeHandle(0, res -> {
    });

    public static CompletableFuture<AccountInfo> getAccountInfo() {
        CompletableFuture<AccountInfo> future = new CompletableFuture<>();
        NativeBindings.appAccountInfo(appHandle.toLong(), (result, accountInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(accountInfo);
        });
        return future;
    }

    public static CompletableFuture<Void> resetObjectCache() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.appResetObjectCache(appHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<Void> refreshAccessInfo() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.accessContainerRefreshAccessInfo(appHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<MDataInfo> getContainerMDataInfo(String containerName) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.accessContainerGetContainerMdataInfo(appHandle.toLong(), containerName, (result, mDataInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(mDataInfo);
        });
        return future;
    }

    public static CompletableFuture<List<ContainerPermission>> getContainerPermissions() {
        CompletableFuture<List<ContainerPermission>> future = new CompletableFuture<>();
        NativeBindings.accessContainerFetch(appHandle.toLong(), ((result, containerPerms) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(Arrays.asList(containerPerms));
        }));
        return future;
    }

    public static CompletableFuture<Void> initLogging(String outputFileName) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.appInitLogging(outputFileName, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<String> getLogOutputPath(String logFileName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appOutputLogPath(logFileName, (result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(path);
        });
        return future;
    }

    public static CompletableFuture<String> getAppContainerName(String appId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appContainerName(appId, (result, name) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(name);
        });
        return future;
    }

    public static CompletableFuture<Void> setAdditionalSearchPath(String path) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.appSetAdditionalSearchPath(path, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(null);
        });
        return future;
    }

    public static CompletableFuture<String> getAppStem() {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appExeFileStem((result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(path);
        });
        return future;
    }

    public static CompletableFuture<Request> getAuthRequest(AuthReq req) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeAuthReq(req, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<Request> getContainerRequest(ContainersReq req) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeContainersReq(req, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<Request> getShareMutableDataRequest(ShareMDataReq req) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataReq(req, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<Request> getUnregisteredSessionRequest(App app) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        byte[] id = app.getId().getBytes();
        NativeBindings.encodeUnregisteredReq(id, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<DecodeResult> decodeResponse(String uri) {
        CompletableFuture<DecodeResult> future = new CompletableFuture<>();

        CallbackIntAuthGranted onAuthGranted = (reqId, authGranted) -> {
            future.complete(new AuthResponse(reqId, authGranted));
        };

        CallbackIntByteArrayLen onUnregistered = (reqId, serialisedCfgPtr) -> {
            future.complete(new UnregisteredClientResponse(reqId, serialisedCfgPtr));
        };

        CallbackInt onContainerCb = (reqId) -> {
            future.complete(new ContainerResponse(reqId));
        };

        CallbackInt onShareMdCb = (reqId) -> {
            future.complete(new ShareMutableDataResponse(reqId));
        };

        CallbackVoid onRevoked = () -> {
            future.complete(new RevokedResponse());
        };

        CallbackResultInt onErrorCb = (result, reqId) -> {
            future.complete(new DecodeError(reqId, result));
        };

        NativeBindings.decodeIpcMsg(uri, onAuthGranted, onUnregistered, onContainerCb, onShareMdCb, onRevoked, onErrorCb);
        return future;
    }

    public static CompletableFuture<Void> connect(UnregisteredClientResponse response, OnDisconnected onDisconnected) {
        return connect(response.getBootstrapConfig(), onDisconnected);
    }

    public static CompletableFuture<Void> connect(byte[] bootStrapConfig, OnDisconnected onDisconnected) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CallbackVoid onDisconnectCb = () -> {
            if (onDisconnected != null) {
                onDisconnected.disconnected();
            }
        };
        CallbackResultApp callback = (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            BaseSession.appHandle = new NativeHandle(app, (handle) -> {
                NativeBindings.appFree(handle);
            });
            BaseSession.disconnectedCb = onDisconnectCb;
            future.complete(null);
        };
        NativeBindings.appUnregistered(bootStrapConfig, onDisconnectCb, callback);
        return future;
    }

    public static CompletableFuture<Void> connect(App app, AuthGranted authGranted, OnDisconnected onDisconnected) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CallbackVoid onDisconnectCb = () -> {
            if (onDisconnected != null) {
                onDisconnected.disconnected();
            }
        };
        CallbackResultApp callback = (result, appHandle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            BaseSession.appHandle = new NativeHandle(appHandle, (handle) -> {
                NativeBindings.appFree(handle);
            });
            BaseSession.disconnectedCb = onDisconnectCb;
            future.complete(null);
        };
        NativeBindings.appRegistered(app.getId(), authGranted, onDisconnectCb, callback);
        return future;
    }

    private static CallbackResultIntString handleRequestCallback(CompletableFuture<Request> future) {
        return (result, reqId, uri) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
                return;
            }
            future.complete(new Request(uri, reqId));
        };
    }

}
