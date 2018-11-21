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
 * Represents a Signing key pair
 */
public class SignKeyPair {

    private final NativeHandle publicSignKey;
    private final NativeHandle secretSignKey;

    /***
     * Initialises a SignKeyPair object
     * @param publicSignKey Public sign key
     * @param secretSignKey Private sign key
     */
    public SignKeyPair(final NativeHandle publicSignKey, final NativeHandle secretSignKey) {
        this.publicSignKey = publicSignKey;
        this.secretSignKey = secretSignKey;
    }

    /***
     * Returns the public sign key
     * @return Public sign key as {@link NativeHandle}
     */
    public NativeHandle getPublicSignKey() {
        return publicSignKey;
    }

    /***
     * Returns the secret sign key
     * @return Secret sign key as {@link NativeHandle}
     */
    public NativeHandle getSecretSignKey() {
        return secretSignKey;
    }
}
