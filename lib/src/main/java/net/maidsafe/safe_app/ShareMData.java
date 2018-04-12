package net.maidsafe.safe_app;

/// For use in `ShareMDataReq`. Represents a specific `MutableData` that is being shared.
public class ShareMData {
	private long typeTag;
	private byte[] name;
	private PermissionSet perms;

	public ShareMData() {
		this.name = new byte[] {};
		this.perms = new PermissionSet();
	}
	public ShareMData(long typeTag, byte[] name, PermissionSet perms) {
		this.typeTag = typeTag;
		this.name = name;
		this.perms = perms;
	}
	public long getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(final long val) {
		typeTag = val;
	}

	public byte[] getName() {
		return name;
	}

	public void setName(final byte[] val) {
		name = val;
	}

	public PermissionSet getPerm() {
		return perms;
	}

	public void setPerm(final PermissionSet val) {
		perms = val;
	}

}

