package net.maidsafe.safe_app;

/// Represents the FFI-safe mutable data value.
public class MDataValue {
	private byte[] content;
	private long contentLen;
	private long entryVersion;

	public MDataValue() {

	}
	public MDataValue(byte[] content, long contentLen, long entryVersion) {
		this.content = content;
		this.contentLen = contentLen;
		this.entryVersion = entryVersion;
	}
	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] val) {
		content = val;
	}

	public long getContentLen() {
		return contentLen;
	}

	public void setContentLen(final long val) {
		contentLen = val;
	}

	public long getEntryVersion() {
		return entryVersion;
	}

	public void setEntryVersion(final long val) {
		entryVersion = val;
	}

}

