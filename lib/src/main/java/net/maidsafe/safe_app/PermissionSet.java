package net.maidsafe.safe_app;

/// Represents a requested set of changes to the permissions of a mutable data.
public class PermissionSet {
	public PermissionSet() { }
	private boolean read;

	public boolean getRead() {
		return read;
	}

	public void setRead(final boolean val) {
		read = val;
	}

	private boolean insert;

	public boolean getInsert() {
		return insert;
	}

	public void setInsert(final boolean val) {
		insert = val;
	}

	private boolean update;

	public boolean getUpdate() {
		return update;
	}

	public void setUpdate(final boolean val) {
		update = val;
	}

	private boolean delete;

	public boolean getDelete() {
		return delete;
	}

	public void setDelete(final boolean val) {
		delete = val;
	}

	private boolean managePermissions;

	public boolean getManagePermission() {
		return managePermissions;
	}

	public void setManagePermission(final boolean val) {
		managePermissions = val;
	}

	public PermissionSet(boolean read, boolean insert, boolean update, boolean delete, boolean managePermissions) { }
}

