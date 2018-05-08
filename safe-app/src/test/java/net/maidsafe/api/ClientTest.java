package net.maidsafe.api;

        import net.maidsafe.api.model.*;
        import net.maidsafe.test.utils.SessionLoader;

        import org.junit.Assert;
        import org.junit.Test;

public class ClientTest {

    @Test
    public void unregisteredAccessTest() throws Exception {
        SessionLoader.load();
        App app = new App("net.maidsafe.java.test", "sample", "MaidSafe.net Ltd",  "0.1.0");
        Request request = Client.getUnregisteredSessionRequest(app).get();
        Assert.assertTrue(request.getReqId() != 0);
        Assert.assertNotNull(request.getUri());
        Session session = Client.connect(new byte[0]).get();
        EncryptKeyPair encryptKeyPair = session.crypto.generateEncryptKeyPair().get();
        Assert.assertNotNull(encryptKeyPair);
        byte[] cipherText = session.crypto.encrypt(encryptKeyPair.getPublicEncryptKey(), encryptKeyPair.getSecretEncryptKey(), "Hello".getBytes()).get();
        Assert.assertEquals("Hello", new String(session.crypto.decrypt(encryptKeyPair.getPublicEncryptKey(), encryptKeyPair.getSecretEncryptKey(), cipherText).get()));
    }

}
