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

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.safe_app.CallbackVoid;

class DisconnectListener {

    OnDisconnected onDisconnected;
    private final CallbackVoid callback = new CallbackVoid() {
        @Override
        public void call() {
            if (onDisconnected == null) {
                return;
            }
            onDisconnected.disconnected(null);
        }
    };

    DisconnectListener() {
        // Constructor intentionally empty
    }

    public void setListener(final OnDisconnected onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public CallbackVoid getCallback() {
        return callback;
    }

}
