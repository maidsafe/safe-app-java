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

/**
 * Represents IPC Requests sent to the Authenticator
 */
public class IpcRequest {

    private final int reqId;

    /**
     * Initialises an IpcRequest
     * @param reqId Request ID
     */
    public IpcRequest(final int reqId) {
        this.reqId = reqId;
    }

    /**
     * Initialises an errored IpcRequest
     */
    public IpcRequest() {
        reqId = -1;
    }

    /**
     * Returns the Request ID
     * @return Request ID as integer
     */
    public int getReqId() {
        return reqId;
    }
}


