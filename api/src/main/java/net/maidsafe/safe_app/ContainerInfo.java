package net.maidsafe.safe_app;

/// Information about a container (name, `MDataInfo` and permissions)
public class ContainerInfo {
    private String name;
    private MDataInfo MDataInfo;
    private PermissionSet permissions;

    public String getName() {
        return name;
    }

    public void setName(final String val) {
        name = val;
    }

    public MDataInfo getMDataInfo() {
        return MDataInfo;
    }

    public void setMDataInfo(final MDataInfo val) {
        MDataInfo = val;
    }

    public PermissionSet getPermission() {
        return permissions;
    }

    public void setPermission(final PermissionSet val) {
        permissions = val;
    }

}

