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

import net.maidsafe.safe_authenticator.ContainersReq;

/***
 * Represents a container IPC Request
 */
public class ContainersIpcReq extends IpcRequest {

    private final ContainersReq containersReq;

    /***
     * Initialises a ContainersIpcReq object
     * @param reqId Request ID
     * @param containersReq Containers Request
     */
    public ContainersIpcReq(final int reqId, final ContainersReq containersReq) {
        super(reqId);
        this.containersReq = containersReq;
    }

    /**
     * Returns the {@link net.maidsafe.safe_app.ContainersReq}
     * @return Containers request
     */
    public ContainersReq getContainersReq() {
        return containersReq;
    }

}

