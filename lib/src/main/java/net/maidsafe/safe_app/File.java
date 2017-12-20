package net.maidsafe.safe_app;

/// FFI-wrapper for `File`.
public class File {
	public File() { }
	private long size;

	public long getSize() {
		return size;
	}

	public void setSize(final long val) {
		size = val;
	}

	private long createdSec;

	public long getCreatedSec() {
		return createdSec;
	}

	public void setCreatedSec(final long val) {
		createdSec = val;
	}

	private int createdNsec;

	public int getCreatedNsec() {
		return createdNsec;
	}

	public void setCreatedNsec(final int val) {
		createdNsec = val;
	}

	private long modifiedSec;

	public long getModifiedSec() {
		return modifiedSec;
	}

	public void setModifiedSec(final long val) {
		modifiedSec = val;
	}

	private int modifiedNsec;

	public int getModifiedNsec() {
		return modifiedNsec;
	}

	public void setModifiedNsec(final int val) {
		modifiedNsec = val;
	}

	private byte[] userMetadataPtr;

	public byte[] getUserMetadataPtr() {
		return userMetadataPtr;
	}

	public void setUserMetadataPtr(final byte[] val) {
		userMetadataPtr = val;
	}

	private long userMetadataLen;

	public long getUserMetadataLen() {
		return userMetadataLen;
	}

	public void setUserMetadataLen(final long val) {
		userMetadataLen = val;
	}

	private long userMetadataCap;

	public long getUserMetadataCap() {
		return userMetadataCap;
	}

	public void setUserMetadataCap(final long val) {
		userMetadataCap = val;
	}

	private byte[] dataMapName;

	public byte[] getDataMapName() {
		return dataMapName;
	}

	public void setDataMapName(final byte[] val) {
		dataMapName = val;
	}

	public File(long size, long createdSec, int createdNsec, long modifiedSec, int modifiedNsec, byte[] userMetadataPtr, long userMetadataLen, long userMetadataCap, byte[] dataMapName) { }
}

