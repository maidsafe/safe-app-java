package net.maidsafe.safe_app;

/// Represents the FFI-safe mutable data value.
public class MdataValue {
    private byte contentPtr;
    private long contentLen;
    private long entryVersion;

    public byte getContentPtr() {
        return contentPtr;
    }

    public void setContentPtr(final byte val) {
        contentPtr = val;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(final long val) {
        contentLen = val;
    }

    public long getEntryVersion() {
        return entryVersion;
    }

    public void setEntryVersion(final long val) {
        entryVersion = val;
    }

}

