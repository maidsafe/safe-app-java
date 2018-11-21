// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.safe_authenticator;

/***
 *  FFI result wrapper
 */
public class FfiResult {
    private int errorCode;
    private String description;

    public FfiResult() {
        this.description = new String();
    }

    public FfiResult(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final int val) {
        this.errorCode = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String val) {
        this.description = val;
    }

}

