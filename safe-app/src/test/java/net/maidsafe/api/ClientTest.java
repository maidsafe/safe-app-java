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

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.Request;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;

public class ClientTest {

    static {
        SessionLoader.load();
    }

    @Test
    public void unregisteredAccessTest() throws Exception {
        App app = new App("net.maidsafe.java.test", "sample",
                "MaidSafe.net Ltd", "0.1.0");
        Request request = Client.getUnregisteredSessionRequest(app).get();
        Assert.assertTrue(request.getReqId() != 0);
        Assert.assertNotNull(request.getUri());
        Client client = (Client) Client.connect(new byte[0]).get();
        EncryptKeyPair encryptKeyPair = client.crypto.generateEncryptKeyPair().get();
        Assert.assertNotNull(encryptKeyPair);
        byte[] cipherText = client.crypto.encrypt(encryptKeyPair.getPublicEncryptKey(),
                encryptKeyPair.getSecretEncryptKey(), "Hello".getBytes()).get();
        Assert.assertEquals("Hello", new String(
                client.crypto.decrypt(encryptKeyPair.getPublicEncryptKey(),
                        encryptKeyPair.getSecretEncryptKey(), cipherText).get()));
    }

    @Test
    public void disconnectionTest() throws Exception {
        Client client = TestHelper.createSession();
        client.setOnDisconnectListener(o -> {
            Assert.assertFalse(client.isConnected());
            client.reconnect();
            Assert.assertTrue(client.isConnected());
        });
        client.testSimulateDisconnect().get();
    }
}
