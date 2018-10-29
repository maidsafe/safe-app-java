// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.safe_authenticator;


public class NativeBindings {

    /**
     * Returns true if this crate was compiled against mock-routing.
     */
    public static native boolean isMockBuild();

    /**
     * Removes a revoked app from the authenticator config.
     * <p>
     * Callback parameters: user data, error code
     */
    public static native void authRmRevokedApp(long auth, String appId, CallbackResult oCb);

    /**
     * Get a list of apps revoked from authenticator.
     * <p>
     * Callback parameters: user data, error code, app exchange info vector, vector size
     */
    public static native void authRevokedApps(long auth, CallbackResultAppExchangeInfoArrayLen oCb);

    /**
     * Get a list of apps registered in authenticator.
     * <p>
     * Callback parameters: user data, error code, registered app vector, vector size
     */
    public static native void authRegisteredApps(long auth, CallbackResultRegisteredAppArrayLen oCb);

    /**
     * Return a list of apps having access to an arbitrary MD object.
     * `md_name` and `md_type_tag` together correspond to a single MD.
     * <p>
     * Callback parameters: user data, error code, app access vector, vector size
     */
    public static native void authAppsAccessingMutableData(long auth, byte[] mdName, long mdTypeTag, CallbackResultAppAccessArrayLen oCb);

    /**
     * Decodes a given encoded IPC message without requiring an authorised account.
     */
    public static native void authUnregisteredDecodeIpcMsg(String msg, CallbackIntByteArrayLen oUnregistered, CallbackResultString oErr);

    /**
     * Decodes a given encoded IPC message and calls a corresponding callback.
     */
    public static native void authDecodeIpcMsg(long auth, String msg, CallbackIntAuthReq oAuth, CallbackIntContainersReq oContainers, CallbackIntByteArrayLen oUnregistered, CallbackIntShareMDataReqMetadataResponse oShareMdata, CallbackResultString oErr);

    /**
     * Encode share mutable data response.
     * <p>
     * Callback parameters: user data, error code, response ptr
     */
    public static native void encodeShareMdataResp(long auth, ShareMDataReq req, int reqId, boolean isGranted, CallbackResultString oCb);

    /**
     * Revoke app access.
     * <p>
     * Callback parameters: user data, error code, response ptr
     */
    public static native void authRevokeApp(long auth, String appId, CallbackResultString oCb);

    /**
     * Flush the revocation queue.
     * <p>
     * Callback parameters: user data, error code
     */
    public static native void authFlushAppRevocationQueue(long auth, CallbackResult oCb);

    /**
     * Encodes a response to unregistered client authentication request.
     * <p>
     * Callback parameters: user data, error code, response ptr
     */
    public static native void encodeUnregisteredResp(int reqId, boolean isGranted, CallbackResultString oCb);

    /**
     * Provides and encodes an Authenticator response.
     * <p>
     * Callback parameters: user data, error code, response ptr
     */
    public static native void encodeAuthResp(long auth, AuthReq req, int reqId, boolean isGranted, CallbackResultString oCb);

    /**
     * Update containers permissions for an App.
     * <p>
     * Callback parameters: user data, error code, response ptr
     */
    public static native void encodeContainersResp(long auth, ContainersReq req, int reqId, boolean isGranted, CallbackResultString oCb);

    /**
     * This function should be called to enable logging to a file.
     * If `output_file_name_override` is provided, then this path will be used for
     * the log output file.
     * <p>
     * Callback parameters: user data, error code
     */
    public static native void authInitLogging(String outputFileNameOverride, CallbackResult oCb);

    /**
     * This function should be called to find where log file will be created. It
     * will additionally create an empty log file in the path in the deduced
     * location and will return the file name along with complete path to it.
     * <p>
     * Callback parameters: user data, error code, log path
     */
    public static native void authOutputLogPath(String outputFileName, CallbackResultString oCb);

    /**
     * Create a registered client. This or any one of the other companion
     * functions to get an authenticator instance must be called before initiating any
     * operation allowed by this module. The `user_data` parameter corresponds to the
     * first parameter of the `o_cb` and `o_disconnect_notifier_cb` callbacks.
     * <p>
     * Callback parameters: user data, error code, authenticator
     */
    public static native void createAcc(String accountLocator, String accountPassword, String invitation, CallbackVoid oDisconnectNotifierCb, CallbackResultAuthenticator oCb);

    /**
     * Log into a registered account. This or any one of the other companion
     * functions to get an authenticator instance must be called before initiating
     * any operation allowed for authenticator. The `user_data` parameter corresponds to the
     * first parameter of the `o_cb` and `o_disconnect_notifier_cb` callbacks.
     * <p>
     * Callback parameters: user data, error code, authenticator
     */
    public static native void login(String accountLocator, String accountPassword, CallbackVoid oDisconnectNotifierCb, CallbackResultAuthenticator oCb);

    /**
     * Try to restore a failed connection with the network.
     * <p>
     * Callback parameters: user data, error code
     */
    public static native void authReconnect(long auth, CallbackResult oCb);

    /**
     * Get the account usage statistics.
     * <p>
     * Callback parameters: user data, error code, account info
     */
    public static native void authAccountInfo(long auth, CallbackResultAccountInfo oCb);

    /**
     * Returns the expected name for the application executable without an extension
     */
    public static native void authExeFileStem(CallbackResultString oCb);

    /**
     * Sets the additional path in `config_file_handler` to search for files.
     */
    public static native void authSetAdditionalSearchPath(String newPath, CallbackResult oCb);

    /**
     * Discard and clean up the previously allocated authenticator instance.
     * Use this only if the authenticator is obtained from one of the auth
     * functions in this crate (`create_acc` or `login`).
     * Using `auth` after a call to this function is undefined behaviour.
     */
    public static native void authFree(long auth);


}
