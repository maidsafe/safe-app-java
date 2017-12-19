package net.maidsafe.safe_app;

/// Represents the authentication response.
public class AuthGranted {
    private AppKey appKeys;
    private AccessContInfo accessContainerInfo;
    private AccessContainerEntry accessContainerEntry;
    private byte bootstrapConfigPtr;
    private long bootstrapConfigLen;
    private long bootstrapConfigCap;

    public AppKey getAppKey() {
        return appKeys;
    }

    public void setAppKey(final AppKey val) {
        appKeys = val;
    }

    public AccessContInfo getAccessContainerInfo() {
        return accessContainerInfo;
    }

    public void setAccessContainerInfo(final AccessContInfo val) {
        accessContainerInfo = val;
    }

    public AccessContainerEntry getAccessContainerEntry() {
        return accessContainerEntry;
    }

    public void setAccessContainerEntry(final AccessContainerEntry val) {
        accessContainerEntry = val;
    }

    public byte getBootstrapConfigPtr() {
        return bootstrapConfigPtr;
    }

    public void setBootstrapConfigPtr(final byte val) {
        bootstrapConfigPtr = val;
    }

    public long getBootstrapConfigLen() {
        return bootstrapConfigLen;
    }

    public void setBootstrapConfigLen(final long val) {
        bootstrapConfigLen = val;
    }

    public long getBootstrapConfigCap() {
        return bootstrapConfigCap;
    }

    public void setBootstrapConfigCap(final long val) {
        bootstrapConfigCap = val;
    }

}

