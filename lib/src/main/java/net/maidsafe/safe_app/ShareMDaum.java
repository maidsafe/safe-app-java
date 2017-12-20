package net.maidsafe.safe_app;

/// For use in `ShareMDataReq`. Represents a specific `MutableData` that is being shared.
public class ShareMDaum {
	private long typeTag;

	public long getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(final long val) {
		typeTag = val;
	}

	private byte[] name;

	public byte[] getName() {
		return name;
	}

	public void setName(final byte[] val) {
		name = val;
	}

	private PermissionSet perms;

	public PermissionSet getPerm() {
		return perms;
	}

	public void setPerm(final PermissionSet val) {
		perms = val;
	}

}

