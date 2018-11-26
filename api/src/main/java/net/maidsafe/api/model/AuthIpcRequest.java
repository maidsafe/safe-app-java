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

import net.maidsafe.safe_authenticator.AuthReq;

/***
 * Represents an Authentication IPC Request
 */
public class AuthIpcRequest extends IpcRequest {

    private final AuthReq authReq;

    /***
     * Initialises an AuthIpcRequest object
     * @param reqId Request ID
     * @param authReq Auth Request
     */
    public AuthIpcRequest(final int reqId, final AuthReq authReq) {
        super(reqId);
        this.authReq = authReq;
    }

    /***
     * Returns the Auth Request
     */
    public AuthReq getAuthReq() {
        return authReq;
    }

}
