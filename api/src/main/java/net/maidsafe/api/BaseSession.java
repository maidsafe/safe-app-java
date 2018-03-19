package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.api.model.*;
import net.maidsafe.safe_app.*;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Executor;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

class BaseSession {

    private AppHandle appHandle;
    private OnDisconnected onDisconnected;
    private DisconnectListener disconnectListener;
    private boolean disconnected = false;
    public CipherOpt cipherOpt;
    public Crypto crypto;
    public IData iData;
    public  MData mData;
    public MDataEntries mDataEntries;
    public MDataEntryAction mDataEntryAction;
    public MDataPermission mDataPermission;
    public NFS nfs;

    private BaseSession(AppHandle appHandle, DisconnectListener disconnectListener) {
        this.appHandle = appHandle;
        this.disconnectListener = disconnectListener;
        init();
    }

    private void init() {
        this.disconnectListener.setListener((val) -> {
            if (onDisconnected == null) {
                return;
            }
            disconnected = true;
            onDisconnected.disconnected(this);
        });

        this.cipherOpt = new CipherOpt(this.appHandle);
        this.crypto = new Crypto(this.appHandle);
        this.iData = new IData(this.appHandle);
        this.mData = new MData(this.appHandle);
        this.mDataEntries = new MDataEntries(this.appHandle);
        this.mDataEntryAction = new MDataEntryAction(this.appHandle);
        this.mDataPermission = new MDataPermission(this.appHandle);
        this.nfs = new NFS(this.appHandle);
    }

    public boolean isConnected() {
        return !disconnected;
    }

    public void setOnDisconnectListener(OnDisconnected onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public Future<AccountInfo> getAccountInfo() {
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

    public Future<Void> resetObjectCache() {
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

    public Future<Void> refreshAccessInfo() {
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

    public Future<MDataInfo> getContainerMDataInfo(String containerName) {
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

    public Future<List<ContainerPermissions>> getContainerPermissions() {
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

    public static Future<BaseSession> connect(UnregisteredClientResponse response) {
        return connect(response.getBootstrapConfig());
    }

    public static Future<BaseSession> connect(byte[] bootStrapConfig) {
        return Executor.getInstance().submit(new CallbackHelper<BaseSession>(binder -> {
            DisconnectListener disconnectListener = new DisconnectListener();
            CallbackResultApp callback = (result, app) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }

                AppHandle appHandle = new AppHandle(app);
                binder.onResult(new BaseSession(appHandle, disconnectListener));
            };
            NativeBindings.appUnregistered(bootStrapConfig, disconnectListener.getCallback(), callback);
        }));
    }

    public static Future<BaseSession> connect(App app, AuthGranted authGranted) {
        return Executor.getInstance().submit(new CallbackHelper<BaseSession>(binder -> {
            DisconnectListener disconnectListener = new DisconnectListener();
            CallbackResultApp callback = (result, handle) -> {
                if (result.getErrorCode() != 0) {
                    binder.onException(Helper.ffiResultToException(result));
                    return;
                }
                AppHandle appHandle = new AppHandle(handle);
                binder.onResult(new BaseSession(appHandle, disconnectListener));
            };
            NativeBindings.appRegistered(app.getId(), authGranted, disconnectListener.getCallback(), callback);
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
