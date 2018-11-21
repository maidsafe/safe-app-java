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

import net.maidsafe.api.model.AuthIpcRequest;
import net.maidsafe.api.model.ContainersIpcReq;
import net.maidsafe.api.model.IpcReqError;
import net.maidsafe.api.model.IpcReqRejected;
import net.maidsafe.api.model.IpcRequest;
import net.maidsafe.api.model.ShareMDataIpcRequest;
import net.maidsafe.api.model.UnregisteredIpcRequest;

import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_authenticator.CallbackIntByteArrayLen;
import net.maidsafe.safe_authenticator.NativeBindings;
import net.maidsafe.safe_authenticator.CallbackIntContainersReq;
import net.maidsafe.safe_authenticator.CallbackIntAuthReq;
import net.maidsafe.safe_authenticator.CallbackIntShareMDataReqMetadataResponseArrayLen;
import net.maidsafe.safe_authenticator.CallbackResultRegisteredAppArrayLen;
import net.maidsafe.safe_authenticator.AppAccess;
import net.maidsafe.safe_authenticator.RegisteredApp;
import net.maidsafe.safe_authenticator.AccountInfo;
import net.maidsafe.safe_authenticator.AppExchangeInfo;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * Represents an Authenticator session
 */
public class Authenticator {

    private static long auth;

    /**
     * Initialises a new Authenticator instance
     * @param auth Auth handle
     */
    public Authenticator(final long auth) {
        init(auth);
    }

    /**
     * Creates a new account and logs in
     * @param accountLocator User locator/username
     * @param accountPassword User secret/password
     * @param invitation Invite token
     * @return An Authenticator instance
     */
    public static CompletableFuture<Authenticator> createAccount(final String accountLocator,
                                                                 final String accountPassword,
                                                                 final String invitation) {
        final CompletableFuture<Authenticator> future = new CompletableFuture<>();
        final AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
        NativeBindings.createAcc(accountLocator, accountPassword,
                invitation, disconnectListener.getCallback(), (result, authenticator) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Authenticator(authenticator));
        });
        return future;
    }

    /**
     * Login to an existing account and return and Authenticator object
     * @param accountLocator User locator/username
     * @param accountPassword User secret/password
     * @return An Authenticator instance
     */
    public static CompletableFuture<Authenticator> login(final String accountLocator, final String accountPassword) {
        final CompletableFuture<Authenticator> future = new CompletableFuture<>();
        final AuthDisconnectListener disconnectListener = new AuthDisconnectListener();
        NativeBindings.login(accountLocator, accountPassword,
                disconnectListener.getCallback(), (result, authenticator) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new Authenticator(authenticator));
        });
        return future;
    }

    /**
     * Encode an unregistered response
     * @param request An IpcRequest for an unregistered request
     * @param isGranted Boolean to allow/deny access for the app
     * @return Encoded response for the unregistered request
     */
    public static CompletableFuture<String> encodeUnregisteredResponse(final IpcRequest request,
                                                                       final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeUnregisteredResp(request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    /**
     * Encodes an authentication response
     * @param request An IpcRequest for an auth request
     * @param isGranted Boolean to allow/deny access for the app
     * @return Encoded response for the auth request
     */
    public CompletableFuture<String> encodeAuthResponse(final AuthIpcRequest request, final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeAuthResp(auth, request.getAuthReq(), request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    /**
     * Decode an unregistered request from an application
     * @param message Encoded unregistered request fom an application
     * @return Decoded IpcRequest
     */
    public static CompletableFuture<IpcRequest> unregisteredDecodeIpcMessage(final String message) {
        final CompletableFuture<IpcRequest> future = new CompletableFuture<>();
        final CallbackIntByteArrayLen callback = (reqId, extraData) -> {
            future.complete(new UnregisteredIpcRequest(reqId, extraData));
        };
        NativeBindings.authUnregisteredDecodeIpcMsg(message, callback, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(new IpcReqError(result.getErrorCode(), result.getDescription(), response));
        });
        return future;
    }

    private void init(final long authenticator) {
        this.auth = authenticator;
    }

    /**
     * Returns true if libraries were compiled against mock routing
     */
    public static boolean isMock() {
        return NativeBindings.authIsMock();
    }

    /**
     * Returns the list of apps that have been revoked
     * @return List of {@link AppExchangeInfo} with the app details
     */
    public CompletableFuture<List<AppExchangeInfo>> listRevokedApps() {
        final CompletableFuture<List<AppExchangeInfo>> future = new CompletableFuture<>();
        NativeBindings.authRevokedApps(auth, (result, appExchangeInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(appExchangeInfo));
        });
        return future;
    }

    /**
     * Flush the App revocation queue
     */
    public CompletableFuture flushAppRevocationQueue() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authFlushAppRevocationQueue(auth, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    /**
     * Encodes a container response
     * @param containersReq The decoded containers request
     * @param isGranted Boolean to allow/deny the containers request
     * @return Encoded response for containers request
     */
    public CompletableFuture<String> encodeContainersResponse(final ContainersIpcReq containersReq,
                                                              final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeContainersResp(auth, containersReq.getContainersReq(),
                containersReq.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    /**
     * Initialises native logging for the Authenticator library
     * @param outputFileNameOverride Name of the output log file
     */
    public static CompletableFuture initLogging(final String outputFileNameOverride) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authInitLogging(outputFileNameOverride, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    /**
     * Returns the path of the log file for native logging
     * @param outputFileName The log file name
     * @return The path to the log file
     */
    public static CompletableFuture<String> outputLogPath(final String outputFileName) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authOutputLogPath(outputFileName, (result, path) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(path);
        });
        return future;
    }

    /**
     * Attempt to re-establish a lost connection with the network
     */
    public CompletableFuture reconnect() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authReconnect(auth, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    /**
     * Fetch the user account information
     * @return New user {@link AccountInfo} instance
     */
    public CompletableFuture<AccountInfo> getAccountInfo() {
        final CompletableFuture<AccountInfo> future = new CompletableFuture<>();
        NativeBindings.authAccountInfo(auth, (result, accountInfo) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(accountInfo);
        });
        return future;
    }

    /**
     * Returns the executable file name without extension
     * @return Executable name
     */
    public static CompletableFuture<String> exeFileStem() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authExeFileStem((result, appName) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(appName);
        });
        return future;
    }

    /**
     * Sets additional search paths in `config_file_handler` to search for configuration files
     * @param newPath Path to be added
     */
    public static CompletableFuture setAdditionalSearchPath(final String newPath) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authSetAdditionalSearchPath(newPath, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    /**
     * Remove an app from the revoked apps list
     * @param appId AppID to be removed
     */
    public CompletableFuture removeRevokedApp(final String appId) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authRmRevokedApp(auth, appId, result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

    /**
     * Return a list of registered apps
     * @return List of apps registered
     */
    public CompletableFuture<List<RegisteredApp>> getRegisteredApps() {
        final CompletableFuture<List<RegisteredApp>> future = new CompletableFuture<>();
        final CallbackResultRegisteredAppArrayLen callback = (result, registeredApp) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            Arrays.asList(registeredApp);
        };
        NativeBindings.authRegisteredApps(auth, callback);
        return future;
    }

    /**
     * Fetch a list of apps with access to a particular Mutable Data
     * @param mDataInfo Mutable data information
     * @return List of apps with access
     */
    public CompletableFuture<List<AppAccess>> getAppsWithMDataAccess(final MDataInfo mDataInfo) {
        final CompletableFuture<List<AppAccess>> future = new CompletableFuture<>();
        NativeBindings.authAppsAccessingMutableData(auth, mDataInfo.getName(),
                mDataInfo.getTypeTag(), (result, appAccess) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(appAccess));
        });
        return future;
    }

    /**
     * Encode a Share Mutable Data response
     * @param shareMDataReq Decoded share Mutable Data request
     * @param isGranted Boolean to allow/deny the shared Mutable Data request
     * @return Encoded share mutable data response
     */
    public CompletableFuture<String> encodeShareMDataResponse(final ShareMDataIpcRequest shareMDataReq,
                                                              final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataResp(auth, shareMDataReq.getShareMDataReq(), shareMDataReq.getReqId(),
                isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    /**
     * Revoke an application
     * @param appId Application ID
     * @return Encoded revoked response
     */
    public CompletableFuture<String> revokeApp(final String appId) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authRevokeApp(auth, appId, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    /**
     * Get the list of registered apps
     * @return List of {@link RegisteredApp}
     */
    public CompletableFuture<List<RegisteredApp>> listRegisteredApps() {
        final CompletableFuture<List<RegisteredApp>> future = new CompletableFuture<>();
        NativeBindings.authRegisteredApps(auth, (result, registeredApp) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(Arrays.asList(registeredApp));
        });
        return future;
    }

    /**
     * Decode an IPC message
     * @param message Encoded request
     * @return Decoded IPC Request
     */
    public CompletableFuture<IpcRequest> decodeIpcMessage(final String message) {
        final CompletableFuture<IpcRequest> future = new CompletableFuture<>();
        final CallbackIntAuthReq callbackIntAuthReq = (reqId, req) -> {
            future.complete(new AuthIpcRequest(reqId, req));
        };
        final CallbackIntContainersReq callbackIntContainersReq = (reqId, req) -> {
            future.complete(new ContainersIpcReq(reqId, req));
        };
        final CallbackIntByteArrayLen callbackIntByteArrayLen = (reqId, extraData) -> {
            future.complete(new UnregisteredIpcRequest(reqId, extraData));
        };
        final CallbackIntShareMDataReqMetadataResponseArrayLen callbackIntShareMDataReqMetadataResponse =
              (reqId, req, metadata) -> {
            future.complete(new ShareMDataIpcRequest(reqId, req, metadata));
        };
        NativeBindings.authDecodeIpcMsg(auth, message, callbackIntAuthReq, callbackIntContainersReq,
                callbackIntByteArrayLen, callbackIntShareMDataReqMetadataResponse, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.complete(new IpcReqError(result.getErrorCode(), result.getDescription(), response));
                return;
            }
            future.complete(new IpcReqRejected(response));
        });
        return future;
    }

}
