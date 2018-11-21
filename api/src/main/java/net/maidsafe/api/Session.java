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

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.listener.OnDisconnected;
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
import net.maidsafe.utils.Helper;

/**
 * Holds a client session with the network. This is the primary interface to interact with the
 * network. As such it provides all API-Providers connected through this session.
 */
public class Session {

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

    /**
     * Initialises logging to a file.
     * @param outputFileName File name
     */
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

    /**
     * Get the full path for the log file
     * @param logFileName Log file name
     * @return Path to the log file
     */
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

    /**
     * Get the App's container name
     * @param appId Application ID
     * @return App container name
     */
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

    /**
     * Sets additional search paths in `config_file_handler` to search for files
     * @param path New path to be added
     */
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

    /**
     * Returns the expected name for the app's executable without the extension
     * @return App executable name
     */
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

    /**
     * Encodes an Auth request for authentication
     * @param req The {@link AuthReq} to be encoded
     * @return Encoded auth request as {@link Request}
     */
    public static CompletableFuture<Request> encodeAuthReq(final AuthReq req) {
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeAuthReq(req, handleRequestCallback(future));
        return future;
    }

    /**
     * Encodes a share Mutable Data request
     * @param req Share Mutable Data request
     * @return Encoded share Mutable Data request as {@link Request}
     */
    public static CompletableFuture<Request> getShareMutableDataRequest(final ShareMDataReq req) {
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataReq(req, handleRequestCallback(future));
        return future;
    }

    /**
     * Encodes a request for an unregistered session
     * @param appId Application ID
     * @return Encoded unregistered session Request as {@link Request}
     */
    public static CompletableFuture<Request> getUnregisteredSessionRequest(final String appId) {
        final byte[] id = appId.getBytes(Charset.forName("UTF-8"));
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeUnregisteredReq(id, handleRequestCallback(future));
        return future;
    }

    /**
     * Decodes the encoded response from the Authenticator
     * @param response The encoded response from the Authenticator
     * @return Decoded response as {@link DecodeResult}
     */
    public static CompletableFuture<DecodeResult> decodeIpcMessage(final String response) {
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

        NativeBindings.decodeIpcMsg(response, onAuthGranted, onUnregistered, onContainerCb,
                onShareMdCb, onRevoked, onErrorCb);
        return future;
    }

    /**
     * Establish a new unregistered session
     * @param response Unregistered response from the Authenticator
     * @return New {@link Session} instance
     */
    public static CompletableFuture<Session> connect(final UnregisteredClientResponse response) {
        return connect(response.getBootstrapConfig());
    }

    /**
     * Encode a Containers request that is to be sent to the Authenticator
     * @param containersReq Containers request
     * @return Encoded containers request
     */
    public static CompletableFuture<Request> getContainersReq(final ContainersReq containersReq) {
        final CompletableFuture<Request> future = new CompletableFuture<>();
        NativeBindings.encodeContainersReq(containersReq, handleRequestCallback(future));
        return  future;
    }

    /**
     * Establish a new unregistered session using the bootstrap config from the response
     * @param bootStrapConfig Bootstrap configuration
     * @return A new {@link Session} instance
     */
    public static CompletableFuture<Session> connect(final byte[] bootStrapConfig) {
        final CompletableFuture<Session> future = new CompletableFuture<>();
        final DisconnectListener disconnectListener = new DisconnectListener();
        final CallbackResultApp callback = (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }

            AppHandle handle = new AppHandle(app);
            future.complete(new Session(handle, disconnectListener));
        };
        NativeBindings.appUnregistered(bootStrapConfig, disconnectListener.getCallback(), callback);
        return future;
    }

    /**
     * Establish a new registered session with the network for the app
     * @param appId Application ID
     * @param authGranted Authentication reponse.
     * @return A new {@link Session} object
     */
    public static CompletableFuture<Session> connect(final String appId, final AuthGranted authGranted) {
        final CompletableFuture<Session> future = new CompletableFuture<>();
        final DisconnectListener disconnectListener = new DisconnectListener();
        final CallbackResultApp callback = (result, handle) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            AppHandle appH = new AppHandle(handle);
            future.complete(new Session(appH, disconnectListener));
        };
        NativeBindings.appRegistered(appId, authGranted, disconnectListener.getCallback(),
                callback);
        return future;
    }

    /**
     * Helper function to handle the request callbacks
     */
    private static CallbackResultIntString handleRequestCallback(final CompletableFuture<Request> future) {
        return (result, reqId, uri) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Request(uri, reqId));
        };
    }

    /**
     * Returns true if libraries were compiled for mock routing
     */
    public static boolean isMock() {
        return NativeBindings.appIsMock();
    }

    /**
     * Returns true if the client is connected to the network
     * @return Connected status as boolean
     */
    public boolean isConnected() {
        return !disconnected;
    }

    /**
     * Sets the disconnected listener to perform an action when the client is disconnected
     * @param disconnectedAction Action to be performed on disconnection
     */
    public void setOnDisconnectListener(final OnDisconnected disconnectedAction) {
        this.onDisconnected = disconnectedAction;
    }

    /**
     * Get the account usage statistics (mutations done and mutations available)
     * @return Account information as {@link AccountInfo}
     */
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

    /**
     * Resets the object cache. Removes all objects currently in the object cache
     * and invalidates all existing object handles.
     */
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

    /**
     * Fetch latest access information from the network
     */
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

    /**
     * Get the mDataInfo for the App container
     * @param containerName App container name
     * @return mDataInfo for the App container Mutable Data
     */
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

    /**
     * Retrieve a list of container names that an app has access to.
     * @return List of container permissions given to an application as List&lt;{@link ContainerPermissions}&gt;
     */
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

    /**
     * Attempt to restore a failed connection with the network.
     */
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

    /**
     * Simulates the disconnect event
     */
    public CompletableFuture testSimulateDisconnect() {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        NativeBindings.testSimulateNetworkDisconnect(appHandle.toLong(), result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            disconnected = true;
            future.complete(null);
        });
        return future;
    }

    /**
     * Creates a random app instance for testing
     * @param appId Application ID
     * @return New {@link Session} instance
     */
    public CompletableFuture<Session> createTestApp(final String appId) {
        final CompletableFuture future = new CompletableFuture();
        NativeBindings.testCreateApp(appId, (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Session(new AppHandle(app), new DisconnectListener()));
        });
        return future;
    }

    /**
     * Creates a random app instance for testing
     * @param authReq Auth request
     * @return New {@link Session} instance
     */
    public CompletableFuture<Session> createTestAppWithAccess(final AuthReq authReq) {
        final CompletableFuture future = new CompletableFuture();
        NativeBindings.testCreateAppWithAccess(authReq, (result, app) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Session(new AppHandle(app), new DisconnectListener()));
        });
        return future;
    }
}
