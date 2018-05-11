package net.maidsafe.safe_app;

/// FFI-wrapper for `File`.
public class File {
	private long size;
	private long createdSec;
	private int createdNsec;
	private long modifiedSec;
	private int modifiedNsec;
	private byte[] userMetadataPtr;
	private long userMetadataLen;
	private long userMetadataCap;
	private byte[] dataMapName;

	public File() {
		this.userMetadataPtr = new byte[] {};
		this.dataMapName = new byte[] {};
	}
	public File(long size, long createdSec, int createdNsec, long modifiedSec, int modifiedNsec, byte[] userMetadataPtr, long userMetadataLen, long userMetadataCap, byte[] dataMapName) {
		this.size = size;
		this.createdSec = createdSec;
		this.createdNsec = createdNsec;
		this.modifiedSec = modifiedSec;
		this.modifiedNsec = modifiedNsec;
		this.userMetadataPtr = userMetadataPtr;
		this.userMetadataLen = userMetadataLen;
		this.userMetadataCap = userMetadataCap;
		this.dataMapName = dataMapName;
	}
	public long getSize() {
		return size;
	}

	public void setSize(final long val) {
		this.size = val;
	}

	public long getCreatedSec() {
		return createdSec;
	}

	public void setCreatedSec(final long val) {
		this.createdSec = val;
	}

	public int getCreatedNsec() {
		return createdNsec;
	}

	public void setCreatedNsec(final int val) {
		this.createdNsec = val;
	}

	public long getModifiedSec() {
		return modifiedSec;
	}

	public void setModifiedSec(final long val) {
		this.modifiedSec = val;
	}

	public int getModifiedNsec() {
		return modifiedNsec;
	}

	public void setModifiedNsec(final int val) {
		this.modifiedNsec = val;
	}

	public byte[] getUserMetadataPtr() {
		return userMetadataPtr;
	}

	public void setUserMetadataPtr(final byte[] val) {
		this.userMetadataPtr = val;
	}

	public long getUserMetadataLen() {
		return userMetadataLen;
	}

	public void setUserMetadataLen(final long val) {
		this.userMetadataLen = val;
	}

	public long getUserMetadataCap() {
		return userMetadataCap;
	}

	public void setUserMetadataCap(final long val) {
		this.userMetadataCap = val;
	}

	public byte[] getDataMapName() {
		return dataMapName;
	}

	public void setDataMapName(final byte[] val) {
		this.dataMapName = val;
	}

}

