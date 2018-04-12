package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;
import net.maidsafe.utils.Constants;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class CryptoTest {

    static {
        SessionLoader.load();
    }

    @Test
    public void PublicSignKeyTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle appPublicSignKey = client.crypto.getAppPublicSignKey().get();
        byte[] rawKey = client.crypto.getRawPublicSignKey(appPublicSignKey).get();
        Assert.assertEquals(Constants.PUBLIC_SIGN_KEY_SIZE, rawKey.length);
        appPublicSignKey = client.crypto.getPublicSignKey(rawKey).get();
        Assert.assertNotNull(appPublicSignKey);
    }

    @Test
    public void SecretSignKeyTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle secretSignKey = client.crypto.generateSignKeyPair().get().getSecretSignKey();
        byte[] rawKey = client.crypto.getRawSecretSignKey(secretSignKey).get();
        Assert.assertEquals(Constants.SECRET_SIGN_KEY_SIZE, rawKey.length);
        secretSignKey = client.crypto.getSecretSignKey(rawKey).get();
        Assert.assertNotNull(secretSignKey);
    }

    @Test
    public void SecretEncryptKeyTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle secretEncKey = client.crypto.generateEncryptKeyPair().get().getSecretEncryptKey();
        byte[] rawKey = client.crypto.getRawSecretEncryptKey(secretEncKey).get();
        Assert.assertEquals(Constants.SECRET_ENC_KEY_SIZE, rawKey.length);
        secretEncKey = client.crypto.getSecretEncryptKey(rawKey).get();
        Assert.assertNotNull(secretEncKey);
    }

    @Test
    public void PublicEncryptKeyTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle appPublicEncKey = client.crypto.getAppPublicEncryptKey().get();
        byte[] rawKey = client.crypto.getRawPublicEncryptKey(appPublicEncKey).get();
        Assert.assertEquals(Constants.PUBLIC_ENC_KEY_SIZE, rawKey.length);
        appPublicEncKey = client.crypto.getPublicEncryptKey(rawKey).get();
        Assert.assertNotNull(appPublicEncKey);
    }

    @Test
    public void sealedEncryption() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        EncryptKeyPair encryptKeyPair = client.crypto.generateEncryptKeyPair().get();
        byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
        byte[] cipherText = client.crypto.encryptSealedBox(encryptKeyPair.getPublicEncryptKey(), actPlainText).get();
        byte[] plainText = client.crypto.decryptSealedBox(encryptKeyPair.getPublicEncryptKey(), encryptKeyPair.getSecretEncryptKey(), cipherText).get();
        Assert.assertEquals(new String(actPlainText), new String(plainText));
    }

//    @Ignore
    @Test
    public void boxEncryption() throws Exception {
        NativeHandle sender = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle receiver = TestHelper.createTestApp(Helper.APP_ID + "_second").get();
//        Client.appHandle = sender;
        Client senderClient = new Client(new AppHandle(sender.toLong()), new DisconnectListener());
        Client receiverClient = new Client(new AppHandle(receiver.toLong()), new DisconnectListener());
        EncryptKeyPair encryptKeyPairSender = senderClient.crypto.generateEncryptKeyPair().get();
        byte[] senderPublicEncKey = senderClient.crypto.getRawPublicEncryptKey(encryptKeyPairSender.getPublicEncryptKey()).get();
//        Client.appHandle = receiver;
        EncryptKeyPair encryptKeyPairReceiver = receiverClient.crypto.generateEncryptKeyPair().get();
        byte[] receiverPublicEncKey = receiverClient.crypto.getRawPublicEncryptKey(encryptKeyPairReceiver.getPublicEncryptKey()).get();
        // Sender encrypts the data
//        Client.appHandle = sender;
        // import key as NativeHandle
        NativeHandle receiverPubEncKeyInSenderApp = senderClient.crypto.getPublicEncryptKey(receiverPublicEncKey).get();
        byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
        byte[] cipherText = senderClient.crypto.encrypt(receiverPubEncKeyInSenderApp, encryptKeyPairSender.getSecretEncryptKey(), actPlainText).get();
        // Receiver decrypts the cipherText
//        Client.appHandle = receiver;
        // import key as NativeHandle
        NativeHandle senderPubEncKeyInReceiverApp = receiverClient.crypto.getPublicEncryptKey(senderPublicEncKey).get();
        byte[] plainText = receiverClient.crypto.decrypt(senderPubEncKeyInReceiverApp, encryptKeyPairReceiver.getSecretEncryptKey(), cipherText).get();
        Assert.assertEquals(new String(actPlainText), new String(plainText));
        // Should be able to get key as raw and convert back to native handle
        byte[] rawKey = senderClient.crypto.getRawSecretEncryptKey(encryptKeyPairReceiver.getSecretEncryptKey()).get();
        receiverClient.crypto.getSecretEncryptKey(rawKey).get();
    }

    @Test
    public void signTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        SignKeyPair signKeyPair = client.crypto.generateSignKeyPair().get();
        byte[] data = Helper.randomAlphaNumeric(20).getBytes();
        byte[] signedData = client.crypto.sign(signKeyPair.getSecretSignKey(), data).get();
        byte[] verifiedData = client.crypto.verify(signKeyPair.getPublicSignKey(), signedData).get();
        Assert.assertEquals(new String(verifiedData), new String(data));
    }

    @Test
    public void sha3HashTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        client.crypto.sha3Hash(Helper.randomAlphaNumeric(20).getBytes()).get();
    }
}