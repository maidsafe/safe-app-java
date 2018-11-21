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
 * Represents an IPC Request error
 */
public class IpcReqError extends IpcRequest {

    private final int code;
    private final String description;
    private final String message;

    /**
     * Initialises an IpcReqError object
     * @param code Error code
     * @param description Error description
     * @param message Error message
     */
    public IpcReqError(final int code, final String description, final String message) {
        super();
        this.code = code;
        this.description = description;
        this.message = message;
    }

    /**
     * Returns the error code
     * @return Error code as integer
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the error description
     * @return Error description as string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the error message
     * @return Error message as string
     */
    public String getMessage() {
        return message;
    }

}
