package net.maidsafe.safe_app;

/// Represents the set of permissions for a given container
public class ContainerPermission {
	private String contName;

	public String getContName() {
		return contName;
	}

	public void setContName(final String val) {
		contName = val;
	}

	private PermissionSet access;

	public PermissionSet getAccess() {
		return access;
	}

	public void setAccess(final PermissionSet val) {
		access = val;
	}

}

