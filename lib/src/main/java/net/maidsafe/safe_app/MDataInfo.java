package net.maidsafe.safe_app;

/// FFI wrapper for `MDataInfo`.
public class MDataInfo {

	public MDataInfo() {
		this.name = new byte[] {};
		this.encNonce = new byte[] {};
		this.encKey = new byte[] {};
		this.newEncKey = new byte [] {};
		this.newEncNonce = new byte [] {};
	}

	private byte[] name;

	public byte[] getName() {
		return name;
	}

	public void setName(final byte[] val) {
		name = val;
	}

	private long typeTag;

	public long getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(final long val) {
		typeTag = val;
	}

	private boolean hasEncInfo;

	public boolean getHasEncInfo() {
		return hasEncInfo;
	}

	public void setHasEncInfo(final boolean val) {
		hasEncInfo = val;
	}

	private byte[] encKey;

	public byte[] getEncKey() {
		return encKey;
	}

	public void setEncKey(final byte[] val) {
		encKey = val;
	}

	private byte[] encNonce;

	public byte[] getEncNonce() {
		return encNonce;
	}

	public void setEncNonce(final byte[] val) {
		encNonce = val;
	}

	private boolean hasNewEncInfo;

	public boolean getHasNewEncInfo() {
		return hasNewEncInfo;
	}

	public void setHasNewEncInfo(final boolean val) {
		hasNewEncInfo = val;
	}

	private byte[] newEncKey;

	public byte[] getNewEncKey() {
		return newEncKey;
	}

	public void setNewEncKey(final byte[] val) {
		newEncKey = val;
	}

	private byte[] newEncNonce;

	public byte[] getNewEncNonce() {
		return newEncNonce;
	}

	public void setNewEncNonce(final byte[] val) {
		newEncNonce = val;
	}

	public MDataInfo(byte[] name, long typeTag, boolean hasEncInfo, byte[] encKey, byte[] encNonce, boolean hasNewEncInfo, byte[] newEncKey, byte[] newEncNonce) {
		this.name = name;
		this.typeTag = typeTag;
		this.hasEncInfo = hasEncInfo;
		this.encKey = encKey;
		this.encNonce = encNonce;
		this.hasNewEncInfo = hasNewEncInfo;
		this.newEncKey = newEncKey;
		this.newEncNonce = newEncNonce;
}
}

