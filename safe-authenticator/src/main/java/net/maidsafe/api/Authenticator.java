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

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AuthIpcRequest;
import net.maidsafe.api.model.ContainersIpcReq;
import net.maidsafe.api.model.IpcReqError;
import net.maidsafe.api.model.IpcReqRejected;
import net.maidsafe.api.model.IpcRequest;
import net.maidsafe.api.model.Request;
import net.maidsafe.api.model.ShareMDataIpcRequest;
import net.maidsafe.api.model.UnregisteredIpcRequest;

import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_authenticator.CallbackIntByteArrayLen;
import net.maidsafe.safe_authenticator.NativeBindings;
import net.maidsafe.safe_authenticator.CallbackIntContainersReq;
import net.maidsafe.safe_authenticator.ContainersReq;
import net.maidsafe.safe_authenticator.CallbackIntAuthReq;
import net.maidsafe.safe_authenticator.CallbackIntShareMDataReqMetadataResponseArrayLen;
import net.maidsafe.safe_authenticator.ShareMDataReq;
import net.maidsafe.safe_authenticator.CallbackResultRegisteredAppArrayLen;
import net.maidsafe.safe_authenticator.AppAccess;
import net.maidsafe.safe_authenticator.RegisteredApp;
import net.maidsafe.safe_authenticator.AccountInfo;
import net.maidsafe.safe_authenticator.AppExchangeInfo;
import net.maidsafe.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Authenticator {
    private static long auth;

    public Authenticator(final long auth) {
        init(auth);
    }

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

    public static boolean isMock() {
        return NativeBindings.authIsMock();
    }

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

    public CompletableFuture<String> encodeContainersResponse(final ContainersReq containersReq,
                                                              final Request request, final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeContainersResp(auth, containersReq, request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

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

    public static CompletableFuture<String> executeFileStem() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authExeFileStem((result, appName) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(appName);
        });
        return future;
    }

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

    public CompletableFuture removeRevokedApp(final App app) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        NativeBindings.authRmRevokedApp(auth, app.getId(), result -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(null);
        });
        return future;
    }

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

    public CompletableFuture<String> encodeShareMDataResponse(final ShareMDataReq shareMDataReq,
                                                              final Request request, final boolean isGranted) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.encodeShareMdataResp(auth, shareMDataReq, request.getReqId(), isGranted, (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

    public CompletableFuture<String> revokeApp(final App app) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        NativeBindings.authRevokeApp(auth, app.getId(), (result, response) -> {
            if (result.getErrorCode() != 0) {
                future.completeExceptionally(Helper.ffiResultToException(result));
            }
            future.complete(response);
        });
        return future;
    }

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
