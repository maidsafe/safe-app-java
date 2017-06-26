package net.maidsafe.api.test;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import junit.framework.TestCase;
import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.PublicEncryptKey;
import net.maidsafe.api.model.PublicSignKey;
import net.maidsafe.utils.FfiConstant;

public class CryptoTest extends TestCase {

	public void testSha3Hash() throws Exception {
		String expected = "DDAD25FB24BD67C0AD883AC9C747943036EC068837C8A894E44F29244548F4ED";
		SafeClient client = Utils.getTestAppWithAccess();
		byte[] hash = client.crypto().hashSHA3("Demo".getBytes()).get();
		assertEquals(expected, DatatypeConverter.printHexBinary(hash));
	}

	public void testAppPublicSignKey() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicSignKey key = client.crypto().getAppPublicSignKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.SIGN_PUBLICKEYBYTES);
		key = client.crypto().getPublicSignKey(raw).get();
		assert (Arrays.equals(raw, key.getRaw().get()));
	}

	public void testAppPublicSignKeyInvalidSize() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicSignKey key = client.crypto().getAppPublicSignKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.SIGN_PUBLICKEYBYTES);
		try {
			client.crypto().getPublicSignKey(Arrays.copyOf(raw, 10)).get();
		} catch (Exception e) {
			assert (e != null);
		}
		try {
			client.crypto().getPublicSignKey(null).get();
		} catch (Exception e) {
			assert (e != null);
		}
	}

	public void testAppPublicEncryptKey() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicEncryptKey key = client.crypto().getAppPublicEncryptKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.BOX_PUBLICKEYBYTES);
		key = client.crypto().getPublicEncryptKey(raw).get();
		assert (Arrays.equals(raw, key.getRaw().get()));
	}

	public void testAppPublicEncryptKeyInvalidSize() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		PublicEncryptKey key = client.crypto().getAppPublicEncryptKey().get();
		assert (key != null);
		byte[] raw = key.getRaw().get();
		assertEquals(raw.length, FfiConstant.BOX_PUBLICKEYBYTES);
		try {
			client.crypto().getPublicEncryptKey(Arrays.copyOf(raw, 10)).get();
		} catch (Exception e) {
			assert (e != null);
		}
		try {
			client.crypto().getPublicEncryptKey(null).get();
		} catch (Exception e) {
			assert (e != null);
		}
	}

	public void testEncryptKeyPairGeneration() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		EncryptKeyPair pair = client.crypto().generateEncryptKeyPair().get();
		assert (pair.getPublicKey() != null);
		assert (pair.getSecretKey() != null);
		client.crypto().getSecretEncryptKey(pair.getSecretKey().getRaw().get())
				.get();
	}

	public void testGenerateNonce() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		byte[] nonce = client.crypto().generateNonce().get();
		assertEquals(FfiConstant.NONCE, nonce.length);
	}

//	public void testBoxEncryption() throws Exception {
//		SafeClient clientOne = Utils.getTestAppWithAccess();
//
//		final String input = "message " + Math.random();
//		final byte[] rawInput = input.getBytes();
//
//		EncryptKeyPair senderKeys = clientOne.crypto().generateEncryptKeyPair()
//				.get();
//		EncryptKeyPair receiverKeys = clientOne.crypto()
//				.generateEncryptKeyPair().get();
//
//		byte[] cipherText = receiverKeys.getPublicKey()
//				.encrypt(rawInput, senderKeys.getSecretKey()).get();
//		byte[] plainText = receiverKeys.getSecretKey()
//				.decrypt(cipherText, senderKeys.getPublicKey()).get();
//
//		assert (Arrays.equals(plainText, rawInput));
//	}

//	public void testBoxSealedEncryption() throws Exception {
//		SafeClient clientOne = Utils.getTestAppWithAccess();
//
//		EncryptKeyPair recieverKeys = clientOne.crypto()
//				.generateEncryptKeyPair().get();
//
//		byte[] cipherText = recieverKeys.getPublicKey()
//				.encryptSealed("message".getBytes()).get();
//		byte[] plainText = recieverKeys.decryptSealed(cipherText).get();
//
//		assertEquals(new String(plainText), "message");
//	}

}