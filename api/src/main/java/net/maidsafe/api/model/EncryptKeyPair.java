// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api.model;

/***
 * Represents a pair of Encryption keys
 */
public class EncryptKeyPair {

    private final NativeHandle secretEncryptKey;
    private final NativeHandle publicEncryptKey;


    /***
     * Initialises a new EncryptKeyPair instance
     * @param publicEncryptKey The Public Encryption Key
     * @param secretEncryptKey The Private Encryption Key
     */
    public EncryptKeyPair(final NativeHandle publicEncryptKey, final NativeHandle secretEncryptKey) {
        this.secretEncryptKey = secretEncryptKey;
        this.publicEncryptKey = publicEncryptKey;
    }

    /***
     * Returns the public encryption key
     * @return NativeHandle to the public encryption key
     */
    public NativeHandle getPublicEncryptKey() {
        return publicEncryptKey;
    }

    /***
     * Returns the secret encryption key
     * @return NativeHandle to the secret encryption key
     */
    public NativeHandle getSecretEncryptKey() {
        return secretEncryptKey;
    }
}
