package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CryptoTest {

    private final long PUBLIC_SIGN_KEY_SIZE = 32;
    private final long SECRET_SIGN_KEY_SIZE = 64;
    private final long PUBLIC_ENC_KEY_SIZE = 32;
    private final long SECRET_ENC_KEY_SIZE = 32;

    static {
        SessionLoader.load();
    }

    @Test
    public void PublicSignKeyTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle appPublicSignKey = Crypto.getAppPublicSignKey().get();
        byte[] rawKey = Crypto.getRawPublicSignKey(appPublicSignKey).get();
        Assert.assertEquals(PUBLIC_SIGN_KEY_SIZE, rawKey.length);
        appPublicSignKey = Crypto.getPublicSignKey(rawKey).get();
        Assert.assertNotNull(appPublicSignKey);
    }

    @Test
    public void SecretSignKeyTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle secretSignKey = Crypto.generateSignKeyPair().get().getSecretSignKey();
        byte[] rawKey = Crypto.getRawSecretSignKey(secretSignKey).get();
        Assert.assertEquals(SECRET_SIGN_KEY_SIZE, rawKey.length);
        secretSignKey = Crypto.getSecretSignKey(rawKey).get();
        Assert.assertNotNull(secretSignKey);
    }

    @Test
    public void SecretEncryptKeyTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle secretEncKey = Crypto.generateEncryptKeyPair().get().getSecretEncryptKey();
        byte[] rawKey = Crypto.getRawSecretEncryptKey(secretEncKey).get();
        Assert.assertEquals(SECRET_ENC_KEY_SIZE, rawKey.length);
        secretEncKey = Crypto.getSecretEncryptKey(rawKey).get();
        Assert.assertNotNull(secretEncKey);
    }

    @Test
    public void PublicEncryptKeyTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle appPublicEncKey = Crypto.getAppPublicEncryptKey().get();
        byte[] rawKey = Crypto.getRawPublicEncryptKey(appPublicEncKey).get();
        Assert.assertEquals(PUBLIC_ENC_KEY_SIZE, rawKey.length);
        appPublicEncKey = Crypto.getPublicEncryptKey(rawKey).get();
        Assert.assertNotNull(appPublicEncKey);
    }

    @Test
    public void sealedEncryption() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        EncryptKeyPair encryptKeyPair = Crypto.generateEncryptKeyPair().get();
        byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
        byte[] cipherText = Crypto.encryptSealedBox(encryptKeyPair.getPublicEncryptKey(), actPlainText).get();
        byte[] plainText = Crypto.decryptSealedBox(encryptKeyPair.getPublicEncryptKey(), encryptKeyPair.getSecretEncryptKey(), cipherText).get();
        Assert.assertEquals(new String(actPlainText), new String(plainText));
    }

    @Ignore
    @Test
    public void boxEncryption() throws Exception {
        NativeHandle sender = TestHelper.createTestApp(Helper.APP_ID).get();
        NativeHandle receiver = TestHelper.createTestApp(Helper.APP_ID + "_second").get();
        Session.appHandle = sender;
        EncryptKeyPair encryptKeyPairSender = Crypto.generateEncryptKeyPair().get();
        byte[] senderPublicEncKey = Crypto.getRawPublicEncryptKey(encryptKeyPairSender.getPublicEncryptKey()).get();
        Session.appHandle = receiver;
        EncryptKeyPair encryptKeyPairReceiver = Crypto.generateEncryptKeyPair().get();
        byte[] receiverPublicEncKey = Crypto.getRawPublicEncryptKey(encryptKeyPairReceiver.getPublicEncryptKey()).get();
        // Sender encrypts the data
        Session.appHandle = sender;
        // import key as NativeHandle
        NativeHandle receiverPubEncKeyInSenderApp = Crypto.getPublicEncryptKey(receiverPublicEncKey).get();
        byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
        byte[] cipherText = Crypto.encrypt(receiverPubEncKeyInSenderApp, encryptKeyPairSender.getSecretEncryptKey(), actPlainText).get();
        // Receiver decrypts the cipherText
        Session.appHandle = receiver;
        // import key as NativeHandle
        NativeHandle senderPubEncKeyInReceiverApp = Crypto.getPublicEncryptKey(senderPublicEncKey).get();
        byte[] plainText = Crypto.decrypt(senderPubEncKeyInReceiverApp, encryptKeyPairReceiver.getSecretEncryptKey(), cipherText).get();
        Assert.assertEquals(new String(actPlainText), new String(plainText));
        // Should be able to get key as raw and convert back to native handle
        byte[] rawKey = Crypto.getRawSecretEncryptKey(encryptKeyPairReceiver.getSecretEncryptKey()).get();
        Crypto.getSecretEncryptKey(rawKey).get();
    }

    @Test
    public void signTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        SignKeyPair signKeyPair = Crypto.generateSignKeyPair().get();
        byte[] data = Helper.randomAlphaNumeric(20).getBytes();
        byte[] signedData = Crypto.sign(signKeyPair.getSecretSignKey(), data).get();
        byte[] verifiedData = Crypto.verify(signKeyPair.getPublicSignKey(), signedData).get();
        Assert.assertEquals(new String(verifiedData), new String(data));
    }

    @Test
    public void sha3HashTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Crypto.sha3Hash(Helper.randomAlphaNumeric(20).getBytes()).get();
    }

}
