package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.ContainerResponse;
import net.maidsafe.api.model.DecodeError;
import net.maidsafe.api.model.DecodeResult;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.Request;
import net.maidsafe.api.model.RevokedResponse;
import net.maidsafe.api.model.ShareMutableDataResponse;
import net.maidsafe.api.model.UnregisteredClientResponse;
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
import net.maidsafe.safe_app.ContainerPermissions;
import net.maidsafe.safe_app.ContainersReq;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.ShareMDataReq;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

public class BaseSession {

    public static OnDisconnected onDisconnected;
    public static CallbackVoid onDisconnectCb = () -> {
        if (onDisconnected != null) {
            onDisconnected.disconnected();
        }
    };
    public static NativeHandle appHandle = new NativeHandle(0, res -> {});

    public static Future<AccountInfo> getAccountInfo() {
        return Executor.getInstance().submit(new CallbackHelper(binder -> {
            NativeBindings.appAccountInfo(appHandle.toLong(), (result, accountInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(accountInfo);
            });
        }));
    }

    public static Future<Void> resetObjectCache() {
        return Executor.getInstance().submit(new CallbackHelper<Void>((binder) -> {
            NativeBindings.appResetObjectCache(appHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<Void> refreshAccessInfo() {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.accessContainerRefreshAccessInfo(appHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<MDataInfo> getContainerMDataInfo(String containerName) {
        return Executor.getInstance().submit(new CallbackHelper<MDataInfo>(binder -> {
            NativeBindings.accessContainerGetContainerMdataInfo(appHandle.toLong(), containerName, (result, mDataInfo) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(mDataInfo);
            });
        }));
    }

    public static Future<List<ContainerPermissions>> getContainerPermissions() {
        return Executor.getInstance().submit(new CallbackHelper<List<ContainerPermissions>>(binder -> {
            NativeBindings.accessContainerFetch(appHandle.toLong(), ((result, containerPerms) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(Arrays.asList(containerPerms));
            }));
        }));
    }

    public static Future<Void> initLogging(String outputFileName) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.appInitLogging(outputFileName, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<String> getLogOutputPath(String logFileName) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.appOutputLogPath(logFileName, (result, path) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(path);
            });
        }));
    }

    public static Future<String> getAppContainerName(String appId) {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.appContainerName(appId, (result, name) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(name);
            });
        }));
    }

    public static Future<Void> setAdditionalSearchPath(String path) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            NativeBindings.appSetAdditionalSearchPath(path, (result) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(null);
            });
        }));
    }

    public static Future<String> getAppStem() {
        return Executor.getInstance().submit(new CallbackHelper<String>(binder -> {
            NativeBindings.appExeFileStem((result, path) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(path);
            });
        }));
    }

    public static Future<Request> getAuthRequest(AuthReq req) {
        return Executor.getInstance().submit(new CallbackHelper<Request>(binder -> {
            NativeBindings.encodeAuthReq(req, handleRequestCallback(binder));
        }));
    }

    public static Future<Request> getContainerRequest(ContainersReq req) {
        return Executor.getInstance().submit(new CallbackHelper<Request>(binder -> {
            NativeBindings.encodeContainersReq(req, handleRequestCallback(binder));
        }));
    }

    public static Future<Request> getShareMutableDataRequest(ShareMDataReq req) {
        return Executor.getInstance().submit(new CallbackHelper<Request>(binder -> {
            NativeBindings.encodeShareMdataReq(req, handleRequestCallback(binder));
        }));
    }

    public static Future<Request> getUnregisteredSessionRequest(App app) {
        return Executor.getInstance().submit(new CallbackHelper<Request>(binder -> {
            byte[] id = app.getId().getBytes();
            NativeBindings.encodeUnregisteredReq(id, (result, reqId, uri) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                binder.onResult(new Request(uri, reqId));
            });
        }));
    }

    public static Future<DecodeResult> decodeResponse(String uri) {
        return Executor.getInstance().submit(new CallbackHelper<DecodeResult>((binder) -> {
            CallbackIntAuthGranted onAuthGranted = (reqId, authGranted) -> binder.onResult(new AuthResponse(reqId, authGranted));

            CallbackIntByteArrayLen onUnregistered = (reqId, serialisedCfgPtr) -> binder.onResult(new UnregisteredClientResponse(reqId, serialisedCfgPtr));

            CallbackInt onContainerCb = reqId -> binder.onResult(new ContainerResponse(reqId));

            CallbackInt onShareMdCb = (reqId) -> binder.onResult(new ShareMutableDataResponse(reqId));

            CallbackVoid onRevoked = () -> binder.onResult(new RevokedResponse());

            CallbackResultInt onErrorCb = (result, reqId) -> binder.onResult(new DecodeError(reqId, result));

            NativeBindings.decodeIpcMsg(uri, onAuthGranted, onUnregistered, onContainerCb, onShareMdCb, onRevoked, onErrorCb);
        }));
    }

    public static Future<Void> connect(UnregisteredClientResponse response, OnDisconnected onDisconnected) {
        return connect(response.getBootstrapConfig(), onDisconnected);
    }

    public static Future<Void> connect(byte[] bootStrapConfig, OnDisconnected onDisconnected) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            CallbackResultApp callback = (result, app) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                BaseSession.appHandle = new NativeHandle(app, (handle) -> {
                    NativeBindings.appFree(handle);
                });
                BaseSession.onDisconnected = onDisconnected;
                binder.onResult(null);
            };
            NativeBindings.appUnregistered(bootStrapConfig, onDisconnectCb, callback);
        }));
    }

    public static Future<Void> connect(App app, AuthGranted authGranted, OnDisconnected onDisconnected) {
        return Executor.getInstance().submit(new CallbackHelper<Void>(binder -> {
            BaseSession.onDisconnected = onDisconnected;
            CallbackResultApp callback = (result, appHandle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                BaseSession.appHandle = new NativeHandle(appHandle, (handle) -> {
                    NativeBindings.appFree(handle);
                });
                binder.onResult(null);
            };
            NativeBindings.appRegistered(app.getId(), authGranted, onDisconnectCb, callback);
        }));
    }

    private static CallbackResultIntString handleRequestCallback(CallbackHelper.Binder<Request> binder) {
        return (result, reqId, uri) -> {
            if (result.getErrorCode() != 0) {
                binder.onException(Helper.ffiResultToException(result));
                return;
            }
            binder.onResult(new Request(uri, reqId));
        };
    }

}
