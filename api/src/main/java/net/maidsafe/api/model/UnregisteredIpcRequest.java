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
 * Represents an unregistered client IPC request
 */
public class UnregisteredIpcRequest extends IpcRequest {

    private final byte[] extraData;

    /***
     * Initialises an UnregisteredIpcRequest instance
     * @param reqId Request ID
     * @param extraData Extra arbitrary data
     */
    public UnregisteredIpcRequest(final int reqId, final byte[] extraData) {
        super(reqId);
        this.extraData = extraData.clone();
    }

    /**
     * Returns the extra arbitrary data
     * @return Arbitrary data as byte array
     */
    public byte[] getExtraData() {
        return extraData.clone();
    }
}
