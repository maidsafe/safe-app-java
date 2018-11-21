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
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.SignKeyPair;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;


public class CryptoTest {

    static {
        SessionLoader.load();
    }

    static final int LENGTH = 20;

    @Test
    public void publicSignKeyTest() throws Exception {
        Session session = TestHelper.createSession();
        NativeHandle appPublicSignKey = session.crypto.getAppPublicSignKey().get();
        byte[] rawKey = session.crypto.getRawPublicSignKey(appPublicSignKey).get();
        Assert.assertEquals(Constants.PUBLIC_SIGN_KEY_SIZE, rawKey.length);
        appPublicSignKey = session.crypto.getPublicSignKey(rawKey).get();
        Assert.assertNotNull(appPublicSignKey);
    }

    @Test
    public void secretSignKeyTest() throws Exception {
        Session session = TestHelper.createSession();
        NativeHandle secretSignKey = session.crypto.generateSignKeyPair().get().getSecretSignKey();
        byte[] rawKey = session.crypto.getRawSecretSignKey(secretSignKey).get();
        Assert.assertEquals(Constants.SECRET_SIGN_KEY_SIZE, rawKey.length);
        secretSignKey = session.crypto.getSecretSignKey(rawKey).get();
        Assert.assertNotNull(secretSignKey);
    }

    @Test
    public void secretEncryptKeyTest() throws Exception {
        Session session = TestHelper.createSession();
        NativeHandle secretEncKey = session.crypto.generateEncryptKeyPair().get().getSecretEncryptKey();
        byte[] rawKey = session.crypto.getRawSecretEncryptKey(secretEncKey).get();
        Assert.assertEquals(Constants.SECRET_ENC_KEY_SIZE, rawKey.length);
        secretEncKey = session.crypto.getSecretEncryptKey(rawKey).get();
        Assert.assertNotNull(secretEncKey);
    }

    @Test
    public void publicEncryptKeyTest() throws Exception {
        Session session = TestHelper.createSession();
        NativeHandle appPublicEncKey = session.crypto.getAppPublicEncryptKey().get();
        byte[] rawKey = session.crypto.getRawPublicEncryptKey(appPublicEncKey).get();
        Assert.assertEquals(Constants.PUBLIC_ENC_KEY_SIZE, rawKey.length);
        appPublicEncKey = session.crypto.getPublicEncryptKey(rawKey).get();
        Assert.assertNotNull(appPublicEncKey);
    }

    @Test
    public void sealedEncryption() throws Exception {
        Session session = TestHelper.createSession();
        EncryptKeyPair encryptKeyPair = session.crypto.generateEncryptKeyPair().get();
        byte[] actPlainText = Helper.randomAlphaNumeric(LENGTH).getBytes();
        byte[] cipherText = session.crypto.encryptSealedBox(encryptKeyPair.getPublicEncryptKey(),
                actPlainText).get();
        byte[] plainText = session.crypto.decryptSealedBox(encryptKeyPair.getPublicEncryptKey(),
                encryptKeyPair.getSecretEncryptKey(), cipherText).get();
        Assert.assertEquals(new String(actPlainText), new String(plainText));
    }

    @Test
    public void boxEncryption() throws Exception {
        Session senderClient = TestHelper.createSession();
        Session receiverClient = TestHelper.createSession();
        EncryptKeyPair encryptKeyPairSender = senderClient.crypto.generateEncryptKeyPair().get();
        byte[] senderPublicEncKey = senderClient.crypto.getRawPublicEncryptKey(
                encryptKeyPairSender.getPublicEncryptKey()).get();
        EncryptKeyPair encryptKeyPairReceiver = receiverClient.crypto.generateEncryptKeyPair().get();
        byte[] receiverPublicEncKey = receiverClient.crypto.getRawPublicEncryptKey(
                encryptKeyPairReceiver.getPublicEncryptKey()).get();
        // import key as NativeHandle
        NativeHandle receiverPubEncKeyInSenderApp = senderClient.crypto.getPublicEncryptKey(
                receiverPublicEncKey).get();
        byte[] actPlainText = Helper.randomAlphaNumeric(LENGTH).getBytes();
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
        Session session = TestHelper.createSession();
        SignKeyPair signKeyPair = session.crypto.generateSignKeyPair().get();
        byte[] data = Helper.randomAlphaNumeric(LENGTH).getBytes();
        byte[] signedData = session.crypto.sign(signKeyPair.getSecretSignKey(), data).get();
        byte[] verifiedData = session.crypto.verify(signKeyPair.getPublicSignKey(), signedData).get();
        Assert.assertEquals(new String(verifiedData), new String(data));
    }

    @Test
    public void sha3HashTest() throws Exception {
        Session session = TestHelper.createSession();
        session.crypto.sha3Hash(Helper.randomAlphaNumeric(LENGTH).getBytes()).get();
    }
}
