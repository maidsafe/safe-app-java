package net.maidsafe.safe_app;

/// Access container info.
public class AccessContInfo {
	private byte[] id;
	private long tag;
	private byte[] nonce;

	public AccessContInfo() {
		this.id = new byte[] {};
		this.nonce = new byte[] {};
	}
	public AccessContInfo(byte[] id, long tag, byte[] nonce) {
		this.id = id;
		this.tag = tag;
		this.nonce = nonce;
	}
	public byte[] getId() {
		return id;
	}

	public void setId(final byte[] val) {
		id = val;
	}

	public long getTag() {
		return tag;
	}

	public void setTag(final long val) {
		tag = val;
	}

	public byte[] getNonce() {
		return nonce;
	}

	public void setNonce(final byte[] val) {
		nonce = val;
	}

}

