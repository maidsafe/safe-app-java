// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;

/**
 * Constants available in native code
 */
public final class Constants {
    public static final NativeHandle ANYONE_HANDLE = new NativeHandle(0, (h) -> {
    });
    public static final int XOR_NAME_LENGTH = 32;
    public static final int GET_NEXT_VERSION = 0;
    public static final long PUBLIC_SIGN_KEY_SIZE = 32;
    public static final long SECRET_SIGN_KEY_SIZE = 64;
    public static final long PUBLIC_ENC_KEY_SIZE = 32;
    public static final long SECRET_ENC_KEY_SIZE = 32;

    private Constants() {
        // Constructor intentionally empty
    }

}
