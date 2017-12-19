package net.maidsafe.safe_app;

/// FFI-wrapper for `File`.
public class File {
    private long size;
    private long createdSec;
    private int createdNsec;
    private long modifiedSec;
    private int modifiedNsec;
    private byte userMetadataPtr;
    private long userMetadataLen;
    private long userMetadataCap;
    private byte[] dataMapName;

    public long getSize() {
        return size;
    }

    public void setSize(final long val) {
        size = val;
    }

    public long getCreatedSec() {
        return createdSec;
    }

    public void setCreatedSec(final long val) {
        createdSec = val;
    }

    public int getCreatedNsec() {
        return createdNsec;
    }

    public void setCreatedNsec(final int val) {
        createdNsec = val;
    }

    public long getModifiedSec() {
        return modifiedSec;
    }

    public void setModifiedSec(final long val) {
        modifiedSec = val;
    }

    public int getModifiedNsec() {
        return modifiedNsec;
    }

    public void setModifiedNsec(final int val) {
        modifiedNsec = val;
    }

    public byte getUserMetadataPtr() {
        return userMetadataPtr;
    }

    public void setUserMetadataPtr(final byte val) {
        userMetadataPtr = val;
    }

    public long getUserMetadataLen() {
        return userMetadataLen;
    }

    public void setUserMetadataLen(final long val) {
        userMetadataLen = val;
    }

    public long getUserMetadataCap() {
        return userMetadataCap;
    }

    public void setUserMetadataCap(final long val) {
        userMetadataCap = val;
    }

    public byte[] getDataMapName() {
        return dataMapName;
    }

    public void setDataMapName(final byte[] val) {
        dataMapName = val;
    }

}

