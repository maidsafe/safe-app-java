// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.utils;

import net.maidsafe.safe_app.FfiResult;

/**
 * Helper class for exception handling
 */
public final class Helper {

    private Helper() {
        throw new java.lang.RuntimeException("Cannot instantiate utilities classs");
    }

    public static Exception ffiResultToException(final FfiResult result) {
        return new Exception(result.getDescription() + " : " + result.getErrorCode());
    }

    public static Exception ffiResultToException(final net.maidsafe.safe_authenticator.FfiResult result) {
        return new Exception(result.getDescription() + " : " + result.getErrorCode());
    }
}
