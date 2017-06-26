package net.maidsafe.api.model;


public class MDataPermission {
    private long signKeyHandle;
    private long permissionSetHandle;

    public MDataPermission(long signKeyHandle, long permissionSetHandle) {
        this.signKeyHandle = signKeyHandle;
        this.permissionSetHandle = permissionSetHandle;
    }


    public long getSignKeyHandle() {
        return signKeyHandle;
    }

    public long getPermissionSetHandle() {
        return permissionSetHandle;
    }
}
