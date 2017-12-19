package net.maidsafe.safe_app;

/// FFI wrapper for `MDataInfo`.
public class MDataInfo {
    private byte[] name;
    private long typeTag;
    private boolean hasEncInfo;
    private byte[] encKey;
    private byte[] encNonce;
    private boolean hasNewEncInfo;
    private byte[] newEncKey;
    private byte[] newEncNonce;

    public byte[] getName() {
        return name;
    }

    public void setName(final byte[] val) {
        name = val;
    }

    public long getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(final long val) {
        typeTag = val;
    }

    public boolean getHasEncInfo() {
        return hasEncInfo;
    }

    public void setHasEncInfo(final boolean val) {
        hasEncInfo = val;
    }

    public byte[] getEncKey() {
        return encKey;
    }

    public void setEncKey(final byte[] val) {
        encKey = val;
    }

    public byte[] getEncNonce() {
        return encNonce;
    }

    public void setEncNonce(final byte[] val) {
        encNonce = val;
    }

    public boolean getHasNewEncInfo() {
        return hasNewEncInfo;
    }

    public void setHasNewEncInfo(final boolean val) {
        hasNewEncInfo = val;
    }

    public byte[] getNewEncKey() {
        return newEncKey;
    }

    public void setNewEncKey(final byte[] val) {
        newEncKey = val;
    }

    public byte[] getNewEncNonce() {
        return newEncNonce;
    }

    public void setNewEncNonce(final byte[] val) {
        newEncNonce = val;
    }

}

