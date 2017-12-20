package net.maidsafe.safe_app;

/// Information about an application that has access to an MD through `sign_key`
public class AppAccess {
	public AppAccess() { }
	private byte[] signKey;

	public byte[] getSignKey() {
		return signKey;
	}

	public void setSignKey(final byte[] val) {
		signKey = val;
	}

	private PermissionSet permissions;

	public PermissionSet getPermission() {
		return permissions;
	}

	public void setPermission(final PermissionSet val) {
		permissions = val;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String val) {
		name = val;
	}

	private String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(final String val) {
		appId = val;
	}

	public AppAccess(byte[] signKey, PermissionSet permissions, String name, String appId) { }
}

