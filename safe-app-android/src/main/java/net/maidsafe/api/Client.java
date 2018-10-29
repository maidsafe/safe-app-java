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

import android.system.Os;

public class Client extends Session {

    Client(AppHandle app, DisconnectListener disconnectListener) {
        super(app, disconnectListener);
    }

    public static void load() {
        clientTypeFactory = ClientTypeFactory.load(Client.class);
        System.loadLibrary("safe_app_jni");
        if (Session.isMock()) {
            System.loadLibrary("safe_authenticator_jni");
        }
        try {
            Os.setenv("SAFE_MOCK_VAULT_PATH", "/data/data/net.maidsafe.api.test/cache", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
