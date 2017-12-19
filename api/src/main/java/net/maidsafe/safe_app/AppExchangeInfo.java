package net.maidsafe.safe_app;

/// Represents an application ID in the process of asking permissions
public class AppExchangeInfo {
    private String id;
    private String scope;
    private String name;
    private String vendor;

    public String getId() {
        return id;
    }

    public void setId(final String val) {
        id = val;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(final String val) {
        scope = val;
    }

    public String getName() {
        return name;
    }

    public void setName(final String val) {
        name = val;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(final String val) {
        vendor = val;
    }

}

