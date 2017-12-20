package net.maidsafe.safe_app;

/// Represents the FFI-safe mutable data value.
public class MDataValue {
	public MDataValue() { }
	private byte[] contentPtr;

	public byte[] getContentPtr() {
		return contentPtr;
	}

	public void setContentPtr(final byte[] val) {
		contentPtr = val;
	}

	private long contentLen;

	public long getContentLen() {
		return contentLen;
	}

	public void setContentLen(final long val) {
		contentLen = val;
	}

	private long entryVersion;

	public long getEntryVersion() {
		return entryVersion;
	}

	public void setEntryVersion(final long val) {
		entryVersion = val;
	}

	public MDataValue(byte[] contentPtr, long contentLen, long entryVersion) { }
}

