package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data key.
public class MDataKey {
	public MDataKey() { }
	private byte[] valPtr;

	public byte[] getValPtr() {
		return valPtr;
	}

	public void setValPtr(final byte[] val) {
		valPtr = val;
	}

	private long valLen;

	public long getValLen() {
		return valLen;
	}

	public void setValLen(final long val) {
		valLen = val;
	}

	public MDataKey(byte[] valPtr, long valLen) { }
}

