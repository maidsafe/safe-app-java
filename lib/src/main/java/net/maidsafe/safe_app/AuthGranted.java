package net.maidsafe.safe_app;

/// Represents the authentication response.
public class AuthGranted {
	public AuthGranted() { }
	private AppKeys appKeys;

	public AppKeys getAppKey() {
		return appKeys;
	}

	public void setAppKey(final AppKeys val) {
		appKeys = val;
	}

	private AccessContInfo accessContainerInfo;

	public AccessContInfo getAccessContainerInfo() {
		return accessContainerInfo;
	}

	public void setAccessContainerInfo(final AccessContInfo val) {
		accessContainerInfo = val;
	}

	private AccessContainerEntry accessContainerEntry;

	public AccessContainerEntry getAccessContainerEntry() {
		return accessContainerEntry;
	}

	public void setAccessContainerEntry(final AccessContainerEntry val) {
		accessContainerEntry = val;
	}

	private byte[] bootstrapConfigPtr;

	public byte[] getBootstrapConfigPtr() {
		return bootstrapConfigPtr;
	}

	public void setBootstrapConfigPtr(final byte[] val) {
		bootstrapConfigPtr = val;
	}

	private long bootstrapConfigLen;

	public long getBootstrapConfigLen() {
		return bootstrapConfigLen;
	}

	public void setBootstrapConfigLen(final long val) {
		bootstrapConfigLen = val;
	}

	private long bootstrapConfigCap;

	public long getBootstrapConfigCap() {
		return bootstrapConfigCap;
	}

	public void setBootstrapConfigCap(final long val) {
		bootstrapConfigCap = val;
	}

	public AuthGranted(AppKeys appKeys, AccessContInfo accessContainerInfo, AccessContainerEntry accessContainerEntry, byte[] bootstrapConfigPtr, long bootstrapConfigLen, long bootstrapConfigCap) { }
}

