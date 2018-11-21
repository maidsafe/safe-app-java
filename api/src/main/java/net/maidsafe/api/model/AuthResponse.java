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

import net.maidsafe.safe_app.AuthGranted;

/***
 * Represents a Authentication Response for an AuthRequest
 */
public class AuthResponse extends DecodeResult {
    private final AuthGranted authGranted;

    /***
     * Intialises an AuthResponse instance
     * @param reqId  The request ID
     * @param authGranted  The AuthGranted object
     */
    public AuthResponse(final int reqId, final AuthGranted authGranted) {
        super(reqId);
        this.authGranted = authGranted;
    }

    /***
     * Returns the AuthGranted object
     * @return An AuthGranted object
     */
    public AuthGranted getAuthGranted() {
        return authGranted;
    }
}
