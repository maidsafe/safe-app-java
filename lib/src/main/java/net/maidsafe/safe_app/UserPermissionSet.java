package net.maidsafe.safe_app;

/// FFI object representing a (User, Permission Set) pair.
public class UserPermissionSet {
	private long userH;
	private PermissionSet permSet;

	public UserPermissionSet() {
		this.permSet = new PermissionSet();
	}
	public UserPermissionSet(long userH, PermissionSet permSet) {
		this.userH = userH;
		this.permSet = permSet;
	}
	public long getUserH() {
		return userH;
	}

	public void setUserH(final long val) {
		this.userH = val;
	}

	public PermissionSet getPermSet() {
		return permSet;
	}

	public void setPermSet(final PermissionSet val) {
		this.permSet = val;
	}

}

