package net.maidsafe.safe_app;

/// Represents the needed keys to work with the data.
public class AppKey {
	private byte[] ownerKey;

	public byte[] getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(final byte[] val) {
		ownerKey = val;
	}

	private byte[] encKey;

	public byte[] getEncKey() {
		return encKey;
	}

	public void setEncKey(final byte[] val) {
		encKey = val;
	}

	private byte[] signPk;

	public byte[] getSignPk() {
		return signPk;
	}

	public void setSignPk(final byte[] val) {
		signPk = val;
	}

	private byte[] signSk;

	public byte[] getSignSk() {
		return signSk;
	}

	public void setSignSk(final byte[] val) {
		signSk = val;
	}

	private byte[] encPk;

	public byte[] getEncPk() {
		return encPk;
	}

	public void setEncPk(final byte[] val) {
		encPk = val;
	}

	private byte[] encSk;

	public byte[] getEncSk() {
		return encSk;
	}

	public void setEncSk(final byte[] val) {
		encSk = val;
	}

}

