package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;
import net.maidsafe.utils.Constants;
import org.junit.Assert;
import org.junit.Test;


public class CryptoTest {
  static {
    SessionLoader.load();
    Helper.androidSetup();
  }

  @Test
  public void publicSignKeyTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    NativeHandle appPublicSignKey = client.crypto.getAppPublicSignKey().get();
    byte[] rawKey = client.crypto.getRawPublicSignKey(appPublicSignKey).get();
    Assert.assertEquals(Constants.PUBLIC_SIGN_KEY_SIZE, rawKey.length);
    appPublicSignKey = client.crypto.getPublicSignKey(rawKey).get();
    Assert.assertNotNull(appPublicSignKey);
  }

  @Test
  public void secretSignKeyTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    NativeHandle secretSignKey = client.crypto.generateSignKeyPair().get().getSecretSignKey();
    byte[] rawKey = client.crypto.getRawSecretSignKey(secretSignKey).get();
    Assert.assertEquals(Constants.SECRET_SIGN_KEY_SIZE, rawKey.length);
    secretSignKey = client.crypto.getSecretSignKey(rawKey).get();
    Assert.assertNotNull(secretSignKey);
  }

  @Test
  public void secretEncryptKeyTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    NativeHandle secretEncKey = client.crypto.generateEncryptKeyPair().get().getSecretEncryptKey();
    byte[] rawKey = client.crypto.getRawSecretEncryptKey(secretEncKey).get();
    Assert.assertEquals(Constants.SECRET_ENC_KEY_SIZE, rawKey.length);
    secretEncKey = client.crypto.getSecretEncryptKey(rawKey).get();
    Assert.assertNotNull(secretEncKey);
  }

  @Test
  public void publicEncryptKeyTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    NativeHandle appPublicEncKey = client.crypto.getAppPublicEncryptKey().get();
    byte[] rawKey = client.crypto.getRawPublicEncryptKey(appPublicEncKey).get();
    Assert.assertEquals(Constants.PUBLIC_ENC_KEY_SIZE, rawKey.length);
    appPublicEncKey = client.crypto.getPublicEncryptKey(rawKey).get();
    Assert.assertNotNull(appPublicEncKey);
  }

  @Test
  public void sealedEncryption() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    EncryptKeyPair encryptKeyPair = client.crypto.generateEncryptKeyPair().get();
    byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
    byte[] cipherText = client.crypto.encryptSealedBox(encryptKeyPair.getPublicEncryptKey(),
        actPlainText).get();
    byte[] plainText = client.crypto.decryptSealedBox(encryptKeyPair.getPublicEncryptKey(),
        encryptKeyPair.getSecretEncryptKey(), cipherText).get();
    Assert.assertEquals(new String(actPlainText), new String(plainText));
  }

  @Test
  public void boxEncryption() throws Exception {
    Client senderClient = (Client)TestHelper.createSession().get();
    Client receiverClient = (Client)TestHelper.createSession().get();
    EncryptKeyPair encryptKeyPairSender = senderClient.crypto.generateEncryptKeyPair().get();
    byte[] senderPublicEncKey = senderClient.crypto.getRawPublicEncryptKey(
        encryptKeyPairSender.getPublicEncryptKey()).get();
    EncryptKeyPair encryptKeyPairReceiver = receiverClient.crypto.generateEncryptKeyPair().get();
    byte[] receiverPublicEncKey = receiverClient.crypto.getRawPublicEncryptKey(
        encryptKeyPairReceiver.getPublicEncryptKey()).get();
    // import key as NativeHandle
    NativeHandle receiverPubEncKeyInSenderApp = senderClient.crypto.getPublicEncryptKey(
        receiverPublicEncKey).get();
    byte[] actPlainText = Helper.randomAlphaNumeric(20).getBytes();
    byte[] cipherText = senderClient.crypto.encrypt(receiverPubEncKeyInSenderApp,
        encryptKeyPairSender.getSecretEncryptKey(), actPlainText).get();
    // import key as NativeHandle
    NativeHandle senderPubEncKeyInReceiverApp = receiverClient.crypto.getPublicEncryptKey(
        senderPublicEncKey).get();
    byte[] plainText = receiverClient.crypto.decrypt(senderPubEncKeyInReceiverApp,
        encryptKeyPairReceiver.getSecretEncryptKey(), cipherText).get();
    Assert.assertEquals(new String(actPlainText), new String(plainText));
    // Should be able to get key as raw and convert back to native handle
    byte[] rawKey = senderClient.crypto.getRawSecretEncryptKey(
        encryptKeyPairReceiver.getSecretEncryptKey()).get();
    receiverClient.crypto.getSecretEncryptKey(rawKey).get();
  }

  @Test
  public void signTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    SignKeyPair signKeyPair = client.crypto.generateSignKeyPair().get();
    byte[] data = Helper.randomAlphaNumeric(20).getBytes();
    byte[] signedData = client.crypto.sign(signKeyPair.getSecretSignKey(), data).get();
    byte[] verifiedData = client.crypto.verify(signKeyPair.getPublicSignKey(), signedData).get();
    Assert.assertEquals(new String(verifiedData), new String(data));
  }

  @Test
  public void sha3HashTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    client.crypto.sha3Hash(Helper.randomAlphaNumeric(20).getBytes()).get();
  }
}
