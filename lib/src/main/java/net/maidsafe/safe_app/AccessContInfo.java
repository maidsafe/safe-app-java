package net.maidsafe.safe_app;

/// Access container info.
public class AccessContInfo {
	public AccessContInfo() { }
	private byte[] id;

	public byte[] getId() {
		return id;
	}

	public void setId(final byte[] val) {
		id = val;
	}

	private long tag;

	public long getTag() {
		return tag;
	}

	public void setTag(final long val) {
		tag = val;
	}

	private byte[] nonce;

	public byte[] getNonce() {
		return nonce;
	}

	public void setNonce(final byte[] val) {
		nonce = val;
	}

	public AccessContInfo(byte[] id, long tag, byte[] nonce) { }
}

