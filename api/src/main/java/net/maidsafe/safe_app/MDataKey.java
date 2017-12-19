package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data key.
public class MDataKey {
    private byte valPtr;
    private long valLen;

    public byte getValPtr() {
        return valPtr;
    }

    public void setValPtr(final byte val) {
        valPtr = val;
    }

    public long getValLen() {
        return valLen;
    }

    public void setValLen(final long val) {
        valLen = val;
    }

}

