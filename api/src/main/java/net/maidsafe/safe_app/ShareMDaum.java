package net.maidsafe.safe_app;

/// For use in `ShareMDataReq`. Represents a specific `MutableData` that is being shared.
public class ShareMDaum {
    private long typeTag;
    private byte[] name;
    private PermissionSet perms;

    public long getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(final long val) {
        typeTag = val;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(final byte[] val) {
        name = val;
    }

    public PermissionSet getPerm() {
        return perms;
    }

    public void setPerm(final PermissionSet val) {
        perms = val;
    }

}

