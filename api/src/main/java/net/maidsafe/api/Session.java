package net.maidsafe.api;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.ContainerResponse;
import net.maidsafe.api.model.DecodeError;
import net.maidsafe.api.model.DecodeResult;
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
import net.maidsafe.utils.Helper;

import javax.annotation.processing.Completion;


public class Session {

    public CipherOpt cipherOpt;
    public Crypto crypto;
    public IData iData;
    public MData mData;
    public MDataEntries mDataEntries;
    public MDataEntryAction mDataEntryAction;
    public MDataPermission mDataPermission;
    public NFS nfs;
    private AppHandle appHandle;
    private OnDisconnected onDisconnected;
    private DisconnectListener disconnectListener;
    private boolean disconnected = false;
    protected static ClientTypeFactory clientTypeFactory;

    protected Session(AppHandle appHandle, DisconnectListener disconnectListener) {
        this.appHandle = appHandle;
        this.disconnectListener = disconnectListener;
        init();
    }

    public static <T extends Session> T create(AppHandle appHandle, DisconnectListener disconnectListener) {
        try {
            Constructor constructor = clientTypeFactory.getClientType().getDeclaredConstructor(new Class[] {AppHandle.class, DisconnectListener.class});
            return (T) constructor.newInstance(appHandle, disconnectListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CompletableFuture initLogging(String outputFileName) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.appInitLogging(outputFileName, (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public static CompletableFuture<String> getLogOutputPath(String logFileName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appOutputLogPath(logFileName, (result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
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
            }
            future.complete(name);
        });
        return future;
    }

    public static CompletableFuture setAdditionalSearchPath(String path) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.appSetAdditionalSearchPath(path, (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public static CompletableFuture<String> getAppStem() {
        CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appExeFileStem((result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(path);
        });
        return future;
    }

    public static CompletableFuture<Request> encodeAuthReq(AuthReq req) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeAuthReq(req, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<Request> getShareMutableDataRequest(ShareMDataReq req) {
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataReq(req, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<Request> getUnregisteredSessionRequest(App app) {
        byte[] id = app.getId().getBytes();
        CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeUnregisteredReq(id, handleRequestCallback(future));
        return future;
    }

    public static CompletableFuture<DecodeResult> decodeIpcMessage(String uri) {
        CompletableFuture<DecodeResult> future = new CompletableFuture<>();
        CallbackIntAuthGranted onAuthGranted = (reqId, authGranted) ->
                future.complete(new AuthResponse(reqId, authGranted));

        CallbackIntByteArrayLen onUnregistered = (reqId, serialisedCfgPtr) ->
                future.complete(new UnregisteredClientResponse(reqId, serialisedCfgPtr));

        CallbackInt onContainerCb = reqId -> future.complete(new ContainerResponse(reqId));

        CallbackInt onShareMdCb = (reqId) -> future.complete(new ShareMutableDataResponse(reqId));

        CallbackVoid onRevoked = () -> future.complete(new RevokedResponse());

        CallbackResultInt onErrorCb = (result, reqId) ->
                future.complete(new DecodeError(reqId, result));

        NativeBindings.decodeIpcMsg(uri, onAuthGranted, onUnregistered, onContainerCb,
                onShareMdCb, onRevoked, onErrorCb);
        return future;
    }

    public static CompletableFuture<Object> connect(UnregisteredClientResponse response) {
        return connect(response.getBootstrapConfig());
    }

    public static CompletableFuture<Object> connect(byte[] bootStrapConfig) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        DisconnectListener disconnectListener = new DisconnectListener();
        CallbackResultApp callback = (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }

            AppHandle appHandle = new AppHandle(app);
            future.complete(Session.create(appHandle, disconnectListener));
        };
        NativeBindings.appUnregistered(bootStrapConfig, disconnectListener.getCallback(), callback);
        return  future;
    }

    public static CompletableFuture<Object> connect(App app, AuthGranted authGranted) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        DisconnectListener disconnectListener = new DisconnectListener();
        CallbackResultApp callback = (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            AppHandle appHandle = new AppHandle(handle);
            future.complete(Session.create(appHandle, disconnectListener));
        };
        NativeBindings.appRegistered(app.getId(), authGranted, disconnectListener.getCallback(),
                callback);
        return future;
    }

    private static CallbackResultIntString handleRequestCallback(CompletableFuture<Request> future) {
        return (result, reqId, uri) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Request(uri, reqId));
        };
    }

    private void init() {
        if (this.disconnectListener != null) {
            this.disconnectListener.setListener((val) -> {
                if (onDisconnected == null) {
                    return;
                }
                disconnected = true;
                onDisconnected.disconnected(this);
            });
        }

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

    public static boolean isMock() {
        return NativeBindings.isMockBuild();
    }

    public void setOnDisconnectListener(OnDisconnected onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public CompletableFuture<AccountInfo> getAccountInfo() {
        CompletableFuture<AccountInfo> future = new CompletableFuture<>();
        NativeBindings.appAccountInfo(appHandle.toLong(), (result, accountInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(accountInfo);
        });
        return future;
    }

    public CompletableFuture resetObjectCache() {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.appResetObjectCache(appHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture refreshAccessInfo() {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.accessContainerRefreshAccessInfo(appHandle.toLong(), (result) -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
            });
        });
        return future;
    }

    public CompletableFuture<MDataInfo> getContainerMDataInfo(String containerName) {
        CompletableFuture<MDataInfo> future = new CompletableFuture<>();
        NativeBindings.accessContainerGetContainerMdataInfo(appHandle.toLong(), containerName,
                (result, mDataInfo) -> {
                    if (result.getErrorCode() != 0) {
                        future.completeExceptionally(Helper.ffiResultToException(result));
                    }
                    future.complete(mDataInfo);
                });
        return future;
    }

    public CompletableFuture<List<ContainerPermissions>> getContainerPermissions() {
        CompletableFuture<List<ContainerPermissions>> future = new CompletableFuture<>();
        NativeBindings.accessContainerFetch(appHandle.toLong(), ((result, containerPerms) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(containerPerms));
        }));
        return future;
    }

    public CompletableFuture reconnect() {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            NativeBindings.appReconnect(appHandle.toLong(), result -> {
                if (result.getErrorCode() != 0) {
                    Helper.ffiResultToException(result);
                    return;
                }
                disconnected = false;
            });
        });
        return future;
    }
}
