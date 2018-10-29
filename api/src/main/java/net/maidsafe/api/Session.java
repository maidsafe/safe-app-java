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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
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
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.ShareMDataReq;
import net.maidsafe.utils.Helper;


public class Session {

    protected static ClientTypeFactory clientTypeFactory;
    public final CipherOpt cipherOpt;
    public final Crypto crypto;
    public final IData iData;
    public final MData mData;
    public final MDataEntries mDataEntries;
    public final MDataEntryAction mDataEntryAction;
    public final MDataPermission mDataPermission;
    public final NFS nfs;
    private final AppHandle appHandle;
    private final DisconnectListener disconnectListener;
    private OnDisconnected onDisconnected;
    private boolean disconnected;

    protected Session(final AppHandle appHandle, final DisconnectListener disconnectListener) {
        this.appHandle = appHandle;
        this.disconnectListener = disconnectListener;

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

    private static <T extends Session> T create(final AppHandle appHandle,
                                                final DisconnectListener disconnectListener) {
        try {
            final Class<?> clientType = clientTypeFactory.getClientType();
            final Constructor<?> cons = clientType.getDeclaredConstructor(new Class[] {
                    AppHandle.class, DisconnectListener.class
            });
            return (T) cons.newInstance(appHandle, disconnectListener);
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public static CompletableFuture initLogging(final String outputFileName) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.appInitLogging(outputFileName, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }


    public static CompletableFuture<String> getLogOutputPath(final String logFileName) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appOutputLogPath(logFileName, (result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(path);
        });
        return future;
    }


    public static CompletableFuture<String> getAppContainerName(final String appId) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appContainerName(appId, (result, name) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(name);
        });
        return future;
    }


    public static CompletableFuture setAdditionalSearchPath(final String path) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.appSetAdditionalSearchPath(path, (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }


    public static CompletableFuture<String> getAppStem() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.appExeFileStem((result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(path);
        });
        return future;
    }


    public static CompletableFuture<Request> encodeAuthReq(final AuthReq req) {
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeAuthReq(req, handleRequestCallback(future));
        return future;
    }


    public static CompletableFuture<Request> getShareMutableDataRequest(final ShareMDataReq req) {
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataReq(req, handleRequestCallback(future));
        return future;
    }


    public static CompletableFuture<Request> getUnregisteredSessionRequest(final App app) {
        final byte[] id = app.getId().getBytes(Charset.forName("UTF-8"));
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeUnregisteredReq(id, handleRequestCallback(future));
        return future;
    }


    public static CompletableFuture<DecodeResult> decodeIpcMessage(final String uri) {
        final CompletableFuture<DecodeResult> future = new CompletableFuture<>();
        final CallbackIntAuthGranted onAuthGranted = (reqId, authGranted) ->
                future.complete(new AuthResponse(reqId, authGranted));

        final CallbackIntByteArrayLen onUnregistered = (reqId, serialisedCfgPtr) ->
                future.complete(new UnregisteredClientResponse(reqId, serialisedCfgPtr));

        final CallbackInt onContainerCb = reqId -> future.complete(new ContainerResponse(reqId));

        final CallbackInt onShareMdCb = (reqId) -> future.complete(new ShareMutableDataResponse(reqId));

        final CallbackVoid onRevoked = () -> future.complete(new RevokedResponse());

        final CallbackResultInt onErrorCb = (result, reqId) ->
                future.complete(new DecodeError(reqId, result));

        NativeBindings.decodeIpcMsg(uri, onAuthGranted, onUnregistered, onContainerCb,
                onShareMdCb, onRevoked, onErrorCb);
        return future;
    }


    public static CompletableFuture<Object> connect(final UnregisteredClientResponse response) {
        return connect(response.getBootstrapConfig());
    }

    /**
     * Create a new unregistered session using the bootstrap config from the response
     *
     * @param bootStrapConfig Bootstrap configuration
     * @return A new {@link Session object}
     */
    public static CompletableFuture<Object> connect(final byte[] bootStrapConfig) {
        final CompletableFuture<Object> future = new CompletableFuture<>();
        final DisconnectListener disconnectListener = new DisconnectListener();
        final CallbackResultApp callback = (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }

            AppHandle handle = new AppHandle(app);
            future.complete(Session.create(handle, disconnectListener));
        };
        NativeBindings.appUnregistered(bootStrapConfig, disconnectListener.getCallback(), callback);
        return future;
    }


    public static CompletableFuture<Object> connect(final String appId, final AuthGranted authGranted) {
        final CompletableFuture<Object> future = new CompletableFuture<>();
        final DisconnectListener disconnectListener = new DisconnectListener();
        final CallbackResultApp callback = (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            AppHandle appH = new AppHandle(handle);
            future.complete(Session.create(appH, disconnectListener));
        };
        NativeBindings.appRegistered(appId, authGranted, disconnectListener.getCallback(),
                callback);
        return future;
    }


    private static CallbackResultIntString handleRequestCallback(final CompletableFuture<Request> future) {
        return (result, reqId, uri) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Request(uri, reqId));
        };
    }

    public static boolean isMock() {
        return NativeBindings.isMockBuild();
    }

    public boolean isConnected() {
        return !disconnected;
    }

    public void setOnDisconnectListener(final OnDisconnected disconnectedAction) {
        this.onDisconnected = disconnectedAction;
    }


    public CompletableFuture<AccountInfo> getAccountInfo() {
        final CompletableFuture<AccountInfo> future = new CompletableFuture<>();
        NativeBindings.appAccountInfo(appHandle.toLong(), (result, accountInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(accountInfo);
        });
        return future;
    }


    public CompletableFuture resetObjectCache() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.appResetObjectCache(appHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture refreshAccessInfo() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.accessContainerRefreshAccessInfo(appHandle.toLong(), (result) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<MDataInfo> getContainerMDataInfo(final String containerName) {
        final CompletableFuture<MDataInfo> future = new CompletableFuture<>();
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
        final CompletableFuture<List<ContainerPermissions>> future = new CompletableFuture<>();
        NativeBindings.accessContainerFetch(appHandle.toLong(), (result, containerPerms) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(containerPerms));
        });
        return future;
    }

    public CompletableFuture reconnect() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.appReconnect(appHandle.toLong(), result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            disconnected = false;
            future.complete(null);
        });
        return future;
    }
}
