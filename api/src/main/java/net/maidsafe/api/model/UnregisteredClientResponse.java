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
 * Represents an unregistered client response
 */
public class UnregisteredClientResponse extends DecodeResult {
    private final byte[] bootstrapConfig;

    /***
     * Initialises an unregistered client response instance
     * @param reqId Request ID
     * @param bootstrapConfig Bootstrap configuration
     */
    public UnregisteredClientResponse(final int reqId, final byte[] bootstrapConfig) {
        super(reqId);
        this.bootstrapConfig = bootstrapConfig.clone();
    }

    /***
     * Returns the bootstrap configuration
     * @return Bootstrap configuration as byte array
     */
    public byte[] getBootstrapConfig() {
        return bootstrapConfig.clone();
    }
}
