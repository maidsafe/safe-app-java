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

final class ClientTypeFactory {
    private static ClientTypeFactory instance;
    private final Class clientType;

    private ClientTypeFactory(final Class clientType) {
        this.clientType = clientType;
    }

    public static ClientTypeFactory load(final Class clientType) {
        synchronized (ClientTypeFactory.class) {
            if (instance == null) {
                instance = new ClientTypeFactory(clientType);
            }
            return instance;
        }
    }

    public Class getClientType() {
        return clientType;
    }

}
