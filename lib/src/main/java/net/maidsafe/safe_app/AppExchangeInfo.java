package net.maidsafe.safe_app;

/// Represents an application ID in the process of asking permissions
public class AppExchangeInfo {
	public AppExchangeInfo() { }
	private String id;

	public String getId() {
		return id;
	}

	public void setId(final String val) {
		id = val;
	}

	private String scope;

	public String getScope() {
		return scope;
	}

	public void setScope(final String val) {
		scope = val;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String val) {
		name = val;
	}

	private String vendor;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(final String val) {
		vendor = val;
	}

	public AppExchangeInfo(String id, String scope, String name, String vendor) { }
}

