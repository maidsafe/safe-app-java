package net.maidsafe.safe_app;

/// Represents the FFI-safe mutable data value.
public class MDataValue {
	public MDataValue() { }
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] val) {
		content = val;
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

	public MDataValue(byte[] content, long contentLen, long entryVersion) {
		this.content = content;
		this.contentLen = contentLen;
		this.entryVersion = entryVersion;
}
}

