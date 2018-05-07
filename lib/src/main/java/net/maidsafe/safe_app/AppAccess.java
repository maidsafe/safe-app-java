package net.maidsafe.safe_app;

/// Information about an application that has access to an MD through `sign_key`
public class AppAccess {
	private byte[] signKey;
	private PermissionSet permissions;
	private String name;
	private String appId;

	public AppAccess() {
		this.signKey = new byte[] {};
		this.permissions = new PermissionSet();
		this.name = new String();
		this.appId = new String();
	}
	public AppAccess(byte[] signKey, PermissionSet permissions, String name, String appId) {
		this.signKey = signKey;
		this.permissions = permissions;
		this.name = name;
		this.appId = appId;
	}
	public byte[] getSignKey() {
		return signKey;
	}

	public void setSignKey(final byte[] val) {
		this.signKey = val;
	}

	public PermissionSet getPermission() {
		return permissions;
	}

	public void setPermission(final PermissionSet val) {
		this.permissions = val;
	}

	public String getName() {
		return name;
	}

	public void setName(final String val) {
		this.name = val;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(final String val) {
		this.appId = val;
	}

}

