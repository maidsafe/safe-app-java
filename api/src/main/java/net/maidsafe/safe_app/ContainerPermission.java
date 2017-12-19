package net.maidsafe.safe_app;

/// Represents the set of permissions for a given container
public class ContainerPermission {
    private String contName;
    private PermissionSet access;

    public String getContName() {
        return contName;
    }

    public void setContName(final String val) {
        contName = val;
    }

    public PermissionSet getAccess() {
        return access;
    }

    public void setAccess(final PermissionSet val) {
        access = val;
    }

}

