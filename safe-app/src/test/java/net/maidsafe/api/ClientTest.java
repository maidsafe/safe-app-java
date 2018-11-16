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

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;

public class ClientTest {

    static {
        SessionLoader.load();
    }

    @Test
    public void unregisteredAccessTest() throws Exception {
        Session unregisteredSession = TestHelper.createUnregisteredSession();
        EncryptKeyPair encryptKeyPair = unregisteredSession.crypto.generateEncryptKeyPair().get();
        Assert.assertNotNull(encryptKeyPair);
        byte[] cipherText = unregisteredSession.crypto.encrypt(encryptKeyPair.getPublicEncryptKey(),
                encryptKeyPair.getSecretEncryptKey(), "Hello".getBytes()).get();
        Assert.assertEquals("Hello", new String(
                unregisteredSession.crypto.decrypt(encryptKeyPair.getPublicEncryptKey(),
                        encryptKeyPair.getSecretEncryptKey(), cipherText).get()));
    }

    @Test
    public void disconnectionTest() throws Exception {
        Session client = TestHelper.createSession();
        client.setOnDisconnectListener(o -> {
            Assert.assertFalse(client.isConnected());
            try {
                client.reconnect().get();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to reconnect");
            }
        });
        client.testSimulateDisconnect().get();
    }
}
