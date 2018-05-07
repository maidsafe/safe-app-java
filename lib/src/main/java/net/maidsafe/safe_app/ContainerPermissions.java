package net.maidsafe.safe_app;

/// Represents the set of permissions for a given container
public class ContainerPermissions {
	private String contName;
	private PermissionSet access;

	public ContainerPermissions() {
		this.contName = new String();
		this.access = new PermissionSet();
	}
	public ContainerPermissions(String contName, PermissionSet access) {
		this.contName = contName;
		this.access = access;
	}
	public String getContName() {
		return contName;
	}

	public void setContName(final String val) {
		this.contName = val;
	}

	public PermissionSet getAccess() {
		return access;
	}

	public void setAccess(final PermissionSet val) {
		this.access = val;
	}

}

