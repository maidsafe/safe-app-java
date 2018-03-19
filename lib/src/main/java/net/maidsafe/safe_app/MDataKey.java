package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data key.
public class MDataKey {
	public MDataKey() { }
	private byte[] val;

	public byte[] getVal() {
		return val;
	}

	public void setVal(final byte[] val) {
		val = val;
	}

	private long valLen;

	public long getValLen() {
		return valLen;
	}

	public void setValLen(final long val) {
		valLen = val;
	}

	public MDataKey(byte[] val, long valLen) {
		this.val = val;
		this.valLen = valLen;
}
}

