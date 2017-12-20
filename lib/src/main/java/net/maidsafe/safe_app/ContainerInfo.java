package net.maidsafe.safe_app;

/// Information about a container (name, `MDataInfo` and permissions)
public class ContainerInfo {
	public ContainerInfo() { }
	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String val) {
		name = val;
	}

	private MDataInfo mdataInfo;

	public MDataInfo getMdataInfo() {
		return mdataInfo;
	}

	public void setMdataInfo(final MDataInfo val) {
		mdataInfo = val;
	}

	private PermissionSet permissions;

	public PermissionSet getPermission() {
		return permissions;
	}

	public void setPermission(final PermissionSet val) {
		permissions = val;
	}

	public ContainerInfo(String name, MDataInfo mdataInfo, PermissionSet permissions) { }
}

