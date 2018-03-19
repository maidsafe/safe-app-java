package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data (key, value) entry.
public class MDataEntry {
	public MDataEntry() { }
	private MDataKey key;

	public MDataKey getKey() {
		return key;
	}

	public void setKey(final MDataKey val) {
		key = val;
	}

	private MDataValue value;

	public MDataValue getValue() {
		return value;
	}

	public void setValue(final MDataValue val) {
		value = val;
	}

	public MDataEntry(MDataKey key, MDataValue value) {
		this.key = key;
		this.value = value;
}
}

