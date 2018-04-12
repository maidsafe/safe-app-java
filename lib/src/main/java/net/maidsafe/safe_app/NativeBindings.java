package net.maidsafe.safe_app;


                         public class NativeBindings {

                         	/**
	 * Returns true if this crate was compiled against mock-routing.
	 */
	public static native boolean isMockBuild();
	
	/**
	 * Create unregistered app.
	 * The `user_data` parameter corresponds to the first parameter of the
	 * `o_cb` and `o_disconnect_notifier_cb` callbacks.
	 *
	 * Callback parameters: user data, error code, app
	 */
	public static native void appUnregistered(byte[] bootstrapConfig, CallbackVoid oDisconnectNotifierCb, CallbackResultApp oCb);
	
	/**
	 * Create a registered app.
	 * The `user_data` parameter corresponds to the first parameter of the
	 * `o_cb` and `o_disconnect_notifier_cb` callbacks.
	 *
	 * Callback parameters: user data, error code, app
	 */
	public static native void appRegistered(String appId, AuthGranted authGranted, CallbackVoid oDisconnectNotifierCb, CallbackResultApp oCb);
	
	/**
	 * Try to restore a failed connection with the network.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void appReconnect(long app, CallbackResult oCb);
	
	/**
	 * Get the account usage statistics (mutations done and mutations available).
	 *
	 * Callback parameters: user data, error code, account info
	 */
	public static native void appAccountInfo(long app, CallbackResultAccountInfo oCb);
	
	/**
	 * Returns the expected name for the application executable without an extension
	 */
	public static native void appExeFileStem(CallbackResultString oCb);
	
	/**
	 * Sets the additional path in `config_file_handler` to search for files
	 */
	public static native void appSetAdditionalSearchPath(String newPath, CallbackResult oCb);
	
	/**
	 * Discard and clean up the previously allocated app instance.
	 * Use this only if the app is obtained from one of the auth
	 * functions in this crate. Using `app` after a call to this
	 * function is undefined behaviour.
	 */
	public static native void appFree(long app);
	
	/**
	 * Resets the object cache. Removes all objects currently in the object cache
	 * and invalidates all existing object handles.
	 */
	public static native void appResetObjectCache(long app, CallbackResult oCb);
	
	/**
	 * Returns the name of the app's container.
	 */
	public static native void appContainerName(String appId, CallbackResultString oCb);
	
	/**
	 * Fetch access info from the network.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void accessContainerRefreshAccessInfo(long app, CallbackResult oCb);
	
	/**
	 * Retrieve a list of container names that an app has access to.
	 *
	 * Callback parameters: user data, error code, container permissions vector, vector size
	 */
	public static native void accessContainerFetch(long app, CallbackResultContainerPermissionsArrayLen oCb);
	
	/**
	 * Retrieve `MDataInfo` for the given container name from the access container.
	 *
	 * Callback parameters: user data, error code, mdata info handle
	 */
	public static native void accessContainerGetContainerMdataInfo(long app, String name, CallbackResultMDataInfo oCb);
	
	/**
	 * Construct `CipherOpt::PlainText` handle.
	 *
	 * Callback parameters: user data, error code, cipher opt handle
	 */
	public static native void cipherOptNewPlaintext(long app, CallbackResultCipherOptHandle oCb);
	
	/**
	 * Construct `CipherOpt::Symmetric` handle.
	 *
	 * Callback parameters: user data, error code, cipher opt handle
	 */
	public static native void cipherOptNewSymmetric(long app, CallbackResultCipherOptHandle oCb);
	
	/**
	 * Construct `CipherOpt::Asymmetric` handle.
	 *
	 * Callback parameters: user data, error code, cipher opt handle
	 */
	public static native void cipherOptNewAsymmetric(long app, long peerEncryptKeyH, CallbackResultCipherOptHandle oCb);
	
	/**
	 * Free `CipherOpt` handle.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void cipherOptFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Get the public signing key of the app.
	 *
	 * Callback parameters: user data, error code, sign key handle
	 */
	public static native void appPubSignKey(long app, CallbackResultSignPubKeyHandle oCb);
	
	/**
	 * Generate a new sign key pair (public & private key).
	 *
	 * Callback parameters: user data, error code, public sign key handle, secret sign key handle
	 */
	public static native void signGenerateKeyPair(long app, CallbackResultSignPubKeyHandleSignSecKeyHandle oCb);
	
	/**
	 * Create new public signing key from raw array.
	 *
	 * Callback parameters: user data, error code, sign key handle
	 */
	public static native void signPubKeyNew(long app, byte[] data, CallbackResultSignPubKeyHandle oCb);
	
	/**
	 * Retrieve the public signing key as raw array.
	 *
	 * Callback parameters: user data, error code, public sign key
	 */
	public static native void signPubKeyGet(long app, long handle, CallbackResultSignPublicKey oCb);
	
	/**
	 * Free public signing key from memory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void signPubKeyFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Create new secret signing key from raw array.
	 *
	 * Callback parameters: user data, error code, sign key handle
	 */
	public static native void signSecKeyNew(long app, byte[] data, CallbackResultSignSecKeyHandle oCb);
	
	/**
	 * Retrieve the secret signing key as raw array.
	 *
	 * Callback parameters: user data, error code, public sign key
	 */
	public static native void signSecKeyGet(long app, long handle, CallbackResultSignSecretKey oCb);
	
	/**
	 * Free secret signing key from memory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void signSecKeyFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Get the public encryption key of the app.
	 *
	 * Callback parameters: user data, error code, public encrypt key handle
	 */
	public static native void appPubEncKey(long app, CallbackResultEncryptPubKeyHandle oCb);
	
	/**
	 * Generate a new encryption key pair (public & private key).
	 *
	 * Callback parameters: user data, error code, public encrypt key handle, secret encrypt key handle
	 */
	public static native void encGenerateKeyPair(long app, CallbackResultEncryptPubKeyHandleEncryptSecKeyHandle oCb);
	
	/**
	 * Create new public encryption key from raw array.
	 *
	 * Callback parameters: user data, error code, public encrypt key handle
	 */
	public static native void encPubKeyNew(long app, byte[] data, CallbackResultEncryptPubKeyHandle oCb);
	
	/**
	 * Retrieve the public encryption key as raw array.
	 *
	 * Callback parameters: user data, error code, public encrypt key
	 */
	public static native void encPubKeyGet(long app, long handle, CallbackResultAsymPublicKey oCb);
	
	/**
	 * Free encryption key from memory
	 *
	 * Callback parameters: user data, error code, secret encrypt key
	 */
	public static native void encPubKeyFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Create new private encryption key from raw array.
	 *
	 * Callback parameters: user data, error code, secret encrypt key handle
	 */
	public static native void encSecretKeyNew(long app, byte[] data, CallbackResultEncryptSecKeyHandle oCb);
	
	/**
	 * Retrieve the private encryption key as raw array.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void encSecretKeyGet(long app, long handle, CallbackResultAsymSecretKey oCb);
	
	/**
	 * Free private key from memory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void encSecretKeyFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Signs arbitrary data using a given secret sign key.
	 * If `sign_sk_h` is `SIGN_WITH_APP`, then uses the app's own secret key to sign.
	 *
	 * Callback parameters: user data, error code, signed data vector, vector size
	 */
	public static native void sign(long app, byte[] data, long signSkH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Verifies signed data using a given public sign key.
	 * Returns an error if the message could not be verified.
	 *
	 * Callback parameters: user data, error code, verified data vector, vector size
	 */
	public static native void verify(long app, byte[] signedData, long signPkH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Encrypts arbitrary data using a given key pair.
	 * You should provide a recipient's public key and a sender's secret key.
	 *
	 * Callback parameters: user data, error code, ciphertext vector, vector size
	 */
	public static native void encrypt(long app, byte[] data, long pkH, long skH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Decrypts arbitrary data using a given key pair.
	 * You should provide a sender's public key and a recipient's secret key.
	 *
	 * Callback parameters: user data, error code, plaintext vector, vector size
	 */
	public static native void decrypt(long app, byte[] data, long pkH, long skH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Encrypts arbitrary data for a single recipient.
	 * You should provide a recipient's public key.
	 *
	 * Callback parameters: user data, error code, ciphertext vector, vector size
	 */
	public static native void encryptSealedBox(long app, byte[] data, long pkH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Decrypts arbitrary data for a single recipient.
	 * You should provide a recipients's private and public key.
	 *
	 * Callback parameters: user data, error code, plaintext vector, vector size
	 */
	public static native void decryptSealedBox(long app, byte[] data, long pkH, long skH, CallbackResultByteArrayLen oCb);
	
	/**
	 * Returns a sha3 hash for a given data.
	 *
	 * Callback parameters: user data, error code, hash vector, vector size
	 */
	public static native void sha3Hash(byte[] data, CallbackResultByteArrayLen oCb);
	
	/**
	 * Generates a unique nonce and returns the result.
	 *
	 * Callback parameters: user data, error code, nonce
	 */
	public static native void generateNonce(CallbackResultAsymNonce oCb);
	
	/**
	 * Get a Self Encryptor.
	 *
	 * Callback parameters: user data, error code, SE handle
	 */
	public static native void idataNewSelfEncryptor(long app, CallbackResultSEWriterHandle oCb);
	
	/**
	 * Write to Self Encryptor.
	 *
	 * Callback parameters: user data, error code, data, size
	 */
	public static native void idataWriteToSelfEncryptor(long app, long seH, byte[] data, CallbackResult oCb);
	
	/**
	 * Close Self Encryptor and free the Self Encryptor Writer handle.
	 *
	 * Callback parameters: user data, error code, xor name
	 */
	public static native void idataCloseSelfEncryptor(long app, long seH, long cipherOptH, CallbackResultXorNameArray oCb);
	
	/**
	 * Fetch Self Encryptor.
	 *
	 * Callback parameters: user data, error code, SE handle
	 */
	public static native void idataFetchSelfEncryptor(long app, byte[] name, CallbackResultSEReaderHandle oCb);
	
	/**
	 * Get serialised size of `ImmutableData`.
	 *
	 * Callback parameters: user data, error code, serialised size
	 */
	public static native void idataSerialisedSize(long app, byte[] name, CallbackResultLong oCb);
	
	/**
	 * Get data size from Self Encryptor.
	 *
	 * Callback parameters: user data, error code, size
	 */
	public static native void idataSize(long app, long seH, CallbackResultLong oCb);
	
	/**
	 * Read from Self Encryptor.
	 *
	 * Callback parameters: user data, error code, data, size
	 */
	public static native void idataReadFromSelfEncryptor(long app, long seH, long fromPos, long len, CallbackResultByteArrayLen oCb);
	
	/**
	 * Free Self Encryptor Writer handle.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void idataSelfEncryptorWriterFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Free Self Encryptor Reader handle.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void idataSelfEncryptorReaderFree(long app, long handle, CallbackResult oCb);
	
	/**
	 * Encode `AuthReq`.
	 *
	 * Callback parameters: user data, error code, request id, encoded request
	 */
	public static native void encodeAuthReq(AuthReq req, CallbackResultIntString oCb);
	
	/**
	 * Encode `ContainersReq`.
	 *
	 * Callback parameters: user data, error code, request id, encoded request
	 */
	public static native void encodeContainersReq(ContainersReq req, CallbackResultIntString oCb);
	
	/**
	 * Encode `AuthReq` for an unregistered client.
	 *
	 * Callback parameters: user data, error code, request id, encoded request
	 */
	public static native void encodeUnregisteredReq(byte[] extraData, CallbackResultIntString oCb);
	
	/**
	 * Encode `ShareMDataReq`.
	 *
	 * Callback parameters: user data, error code, request id, encoded request
	 */
	public static native void encodeShareMdataReq(ShareMDataReq req, CallbackResultIntString oCb);
	
	/**
	 * Decode IPC message.
	 */
	public static native void decodeIpcMsg(String msg, CallbackIntAuthGranted oAuth, CallbackIntByteArrayLen oUnregistered, CallbackInt oContainers, CallbackInt oShareMdata, CallbackVoid oRevoked, CallbackResultInt oErr);
	
	/**
	 * This function should be called to enable logging to a file.
	 * If `output_file_name_override` is provided, then this path will be used for
	 * the log output file.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void appInitLogging(String outputFileNameOverride, CallbackResult oCb);
	
	/**
	 * This function should be called to find where log file will be created. It
	 * will additionally create an empty log file in the path in the deduced
	 * location and will return the file name along with complete path to it.
	 *
	 * Callback parameters: user data, error code, log path
	 */
	public static native void appOutputLogPath(String outputFileName, CallbackResultString oCb);
	
	/**
	 * Create encrypted mdata info with explicit data name and a
	 * provided private key.
	 *
	 * Callback parameters: user data, error code, mdata info handle
	 */
	public static native void mdataInfoNewPrivate(byte[] name, long typeTag, byte[] secretKey, byte[] nonce, CallbackResultMDataInfo oCb);
	
	/**
	 * Create random, non-encrypted mdata info.
	 *
	 * Callback parameters: user data, error code, mdata info handle
	 */
	public static native void mdataInfoRandomPublic(long typeTag, CallbackResultMDataInfo oCb);
	
	/**
	 * Create random, encrypted mdata info.
	 *
	 * Callback parameters: user data, error code, mdata info handle
	 */
	public static native void mdataInfoRandomPrivate(long typeTag, CallbackResultMDataInfo oCb);
	
	/**
	 * Encrypt mdata entry key using the corresponding mdata info.
	 *
	 * Callback parameters: user data, error code, encrypted entry key vector, vector size
	 */
	public static native void mdataInfoEncryptEntryKey(MDataInfo info, byte[] input, CallbackResultByteArrayLen oCb);
	
	/**
	 * Encrypt mdata entry value using the corresponding mdata info.
	 *
	 * Callback parameters: user data, error code, encrypted entry value vector, vector size
	 */
	public static native void mdataInfoEncryptEntryValue(MDataInfo info, byte[] input, CallbackResultByteArrayLen oCb);
	
	/**
	 * Decrypt mdata entry value or a key using the corresponding mdata info.
	 *
	 * Callback parameters: user data, error code, decrypted mdata info vector, vector size
	 */
	public static native void mdataInfoDecrypt(MDataInfo info, byte[] input, CallbackResultByteArrayLen oCb);
	
	/**
	 * Serialise `MDataInfo`.
	 *
	 * Callback parameters: user data, error code, serialised mdata info
	 */
	public static native void mdataInfoSerialise(MDataInfo info, CallbackResultByteArrayLen oCb);
	
	/**
	 * Deserialise `MDataInfo`.
	 *
	 * Callback parameters: user data, error code, mdata info handle
	 */
	public static native void mdataInfoDeserialise(byte[] encodedPtr, CallbackResultMDataInfo oCb);
	
	/**
	 * Create new mutable data and put it on the network.
	 *
	 * `permissions_h` is a handle to permissions to be set on the mutable data.
	 * If `PERMISSIONS_EMPTY`, the permissions will be empty.
	 *
	 * `entries_h` is a handle to entries for the mutable data.
	 * If `ENTRIES_EMPTY`, the entries will be empty.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataPut(long app, MDataInfo info, long permissionsH, long entriesH, CallbackResult oCb);
	
	/**
	 * Get version of the mutable data.
	 *
	 * Callback parameters: user data, error code, version
	 */
	public static native void mdataGetVersion(long app, MDataInfo info, CallbackResultLong oCb);
	
	/**
	 * Get size of serialised mutable data.
	 *
	 * Callback parameters: user data, error code, serialised size
	 */
	public static native void mdataSerialisedSize(long app, MDataInfo info, CallbackResultLong oCb);
	
	/**
	 * Get value at the given key from the mutable data.
	 * The arguments to the callback are:
	 *     1. user data
	 *     2. error code
	 *     3. pointer to content
	 *     4. content length
	 *     5. entry version
	 *
	 * Please notice that if a value is fetched from a private `MutableData`,
	 * it's not automatically decrypted.
	 */
	public static native void mdataGetValue(long app, MDataInfo info, byte[] key, CallbackResultByteArrayLenLong oCb);
	
	/**
	 * Get a handle to the complete list of entries in the mutable data.
	 *
	 * Callback parameters: user data, error code, entries handle
	 */
	public static native void mdataEntries(long app, MDataInfo info, CallbackResultMDataEntriesHandle oCb);
	
	/**
	 * Get list of all keys in the mutable data.
	 *
	 * Callback parameters: user data, error code, vector of keys, vector size
	 */
	public static native void mdataListKeys(long app, MDataInfo info, CallbackResultMDataKeyArrayLen oCb);
	
	/**
	 * Get list of all values in the mutable data.
	 *
	 * Callback parameters: user data, error code, vector of values, vector size
	 */
	public static native void mdataListValues(long app, MDataInfo info, CallbackResultMDataValueArrayLen oCb);
	
	/**
	 * Mutate entries of the mutable data.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataMutateEntries(long app, MDataInfo info, long actionsH, CallbackResult oCb);
	
	/**
	 * Get list of all permissions set on the mutable data
	 *
	 * Callback parameters: user data, error code, permission handle
	 */
	public static native void mdataListPermissions(long app, MDataInfo info, CallbackResultMDataPermissionsHandle oCb);
	
	/**
	 * Get list of permissions set on the mutable data for the given user.
	 *
	 * User is either handle to a signing key or `USER_ANYONE`.
	 *
	 * Callback parameters: user data, error code, permission set handle
	 */
	public static native void mdataListUserPermissions(long app, MDataInfo info, long userH, CallbackResultPermissionSet oCb);
	
	/**
	 * Set permissions set on the mutable data for the given user.
	 *
	 * User is either handle to a signing key or `USER_ANYONE`.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataSetUserPermissions(long app, MDataInfo info, long userH, PermissionSet permissionSet, long version, CallbackResult oCb);
	
	/**
	 * Delete permissions set on the mutable data for the given user.
	 *
	 * User is either handle to a signing key or `USER_ANYONE`.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataDelUserPermissions(long app, MDataInfo info, long userH, long version, CallbackResult oCb);
	
	/**
	 * Create new empty entries.
	 *
	 * Callback parameters: user data, error code, entries handle
	 */
	public static native void mdataEntriesNew(long app, CallbackResultMDataEntriesHandle oCb);
	
	/**
	 * Insert an entry to the entries.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntriesInsert(long app, long entriesH, byte[] key, byte[] value, CallbackResult oCb);
	
	/**
	 * Returns the number of entries.
	 *
	 * Callback parameters: user data, error code, length
	 */
	public static native void mdataEntriesLen(long app, long entriesH, CallbackResultSize oCb);
	
	/**
	 * Get the entry value at the given key.
	 *
	 * The callbacks arguments are: user data, error code, pointer to value,
	 * value length, entry version. The caller must NOT free the pointer.
	 */
	public static native void mdataEntriesGet(long app, long entriesH, byte[] key, CallbackResultByteArrayLenLong oCb);
	
	/**
	 * Iterate over the entries.
	 *
	 * The `o_each_cb` callback is invoked once for each entry,
	 * passing user data, pointer to key, key length, pointer to value, value length
	 * and entry version in that order.
	 *
	 * The `o_done_cb` callback is invoked after the iteration is done, or in case of error.
	 */
	public static native void mdataListEntries(long app, long entriesH, CallbackResultMDataEntryArrayLen oCb);
	
	/**
	 * Free the entries from memory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntriesFree(long app, long entriesH, CallbackResult oCb);
	
	/**
	 * Create new entry actions.
	 *
	 * Callback parameters: user data, error code, entry actions handle
	 */
	public static native void mdataEntryActionsNew(long app, CallbackResultMDataEntryActionsHandle oCb);
	
	/**
	 * Add action to insert new entry.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntryActionsInsert(long app, long actionsH, byte[] key, byte[] value, CallbackResult oCb);
	
	/**
	 * Add action to update existing entry.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntryActionsUpdate(long app, long actionsH, byte[] key, byte[] value, long entryVersion, CallbackResult oCb);
	
	/**
	 * Add action to delete existing entry.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntryActionsDelete(long app, long actionsH, byte[] key, long entryVersion, CallbackResult oCb);
	
	/**
	 * Free the entry actions from memory
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataEntryActionsFree(long app, long actionsH, CallbackResult oCb);
	
	/**
	 * Serialize metadata.
	 *
	 * Callback parameters: user data, error code, encoded metadata vector, vector size
	 */
	public static native void mdataEncodeMetadata(MetadataResponse metadata, CallbackResultByteArrayLen oCb);
	
	/**
	 * Create new permissions.
	 *
	 * Callback parameters: user data, error code, permissions handle
	 */
	public static native void mdataPermissionsNew(long app, CallbackResultMDataPermissionsHandle oCb);
	
	/**
	 * Get the number of entries in the permissions.
	 *
	 * Callback parameters: user data, error code, size
	 */
	public static native void mdataPermissionsLen(long app, long permissionsH, CallbackResultSize oCb);
	
	/**
	 * Get the permission set corresponding to the given user.
	 * Use a constant `USER_ANYONE` for anyone.
	 *
	 * Callback parameters: user data, error code, permission set handle
	 */
	public static native void mdataPermissionsGet(long app, long permissionsH, long userH, CallbackResultPermissionSet oCb);
	
	/**
	 * Return each (user, permission set) pair in the permissions.
	 *
	 * Callback parameters: user data, error code, vector of user/permission set objects, vector size
	 */
	public static native void mdataListPermissionSets(long app, long permissionsH, CallbackResultUserPermissionSetArrayLen oCb);
	
	/**
	 * Insert permission set for the given user to the permissions.
	 *
	 * To insert permissions for "Anyone", pass `USER_ANYONE` as the user handle.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataPermissionsInsert(long app, long permissionsH, long userH, PermissionSet permissionSet, CallbackResult oCb);
	
	/**
	 * Free the permissions from memory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void mdataPermissionsFree(long app, long permissionsH, CallbackResult oCb);
	
	/**
	 * Retrieve file with the given name, and its version, from the directory.
	 *
	 * Callback parameters: user data, error code, file, version
	 */
	public static native void dirFetchFile(long app, MDataInfo parentInfo, String fileName, CallbackResultFileLong oCb);
	
	/**
	 * Insert the file into the parent directory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void dirInsertFile(long app, MDataInfo parentInfo, String fileName, File file, CallbackResult oCb);
	
	/**
	 * Replace the file in the parent directory.
	 * If `version` is 0, the correct version is obtained automatically.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void dirUpdateFile(long app, MDataInfo parentInfo, String fileName, File file, long version, CallbackResult oCb);
	
	/**
	 * Delete the file in the parent directory.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void dirDeleteFile(long app, MDataInfo parentInfo, String fileName, long version, CallbackResult oCb);
	
	/**
	 * Open the file to read of write its contents.
	 *
	 * Callback parameters: user data, error code, file context handle
	 */
	public static native void fileOpen(long app, MDataInfo parentInfo, File file, long openMode, CallbackResultFileContextHandle oCb);
	
	/**
	 * Get a size of file opened for read.
	 *
	 * Callback parameters: user data, error code, file size
	 */
	public static native void fileSize(long app, long fileH, CallbackResultLong oCb);
	
	/**
	 * Read data from file.
	 *
	 * Callback parameters: user data, error code, file data vector, vector size
	 */
	public static native void fileRead(long app, long fileH, long position, long len, CallbackResultByteArrayLen oCb);
	
	/**
	 * Write data to file in smaller chunks.
	 *
	 * Callback parameters: user data, error code
	 */
	public static native void fileWrite(long app, long fileH, byte[] data, CallbackResult oCb);
	
	/**
	 * Close is invoked only after all the data is completely written. The
	 * file is saved only when `close` is invoked.
	 *
	 * If the file was opened in any of the read modes, returns the modified
	 * file structure as a result. If the file was opened in the read mode,
	 * returns the original file structure that was passed as an argument to
	 * `file_open`.
	 *
	 * Frees the file context handle.
	 *
	 * Callback parameters: user data, error code, file
	 */
	public static native void fileClose(long app, long fileH, CallbackResultFile oCb);
	
	/**
	 * Creates a random app instance for testing.
	 */
	public static native void testCreateApp(String appId, CallbackResultApp oCb);
	
	/**
	 * Create a random app instance for testing, with access to containers.
	 */
	public static native void testCreateAppWithAccess(AuthReq authReq, CallbackResultApp oCb);
	
	/**
	 * Simulate a network disconnect when testing.
	 */
	public static native void testSimulateNetworkDisconnect(long app, CallbackResult oCb);
	


                         }