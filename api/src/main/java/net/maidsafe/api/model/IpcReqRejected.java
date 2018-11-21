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
 * Represents a rejected IPC Request
 */
public class IpcReqRejected extends IpcRequest {

    private final String message;

    /***
     * Initialise a rejected IPC Request
     * @param message Error message
     */
    public IpcReqRejected(final String message) {
        super();
        this.message = message;
    }

    /**
     * Returns the error message
     * @return Error message as string
     */
    public String getMessage() {
        return message;
    }

}
