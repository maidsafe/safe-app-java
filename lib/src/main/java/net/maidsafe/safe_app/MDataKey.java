package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data key.
public class MDataKey {
	private byte[] val;
	private long valLen;

	public MDataKey() {
		this.val = new byte[] {};
	}
	public MDataKey(byte[] val, long valLen) {
		this.val = val;
		this.valLen = valLen;
	}
	public byte[] getVal() {
		return val;
	}

	public void setVal(final byte[] val) {
		this.val = val;
	}

	public long getValLen() {
		return valLen;
	}

	public void setValLen(final long val) {
		this.valLen = val;
	}

}

