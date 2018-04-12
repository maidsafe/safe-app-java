package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data (key, value) entry.
public class MDataEntry {
	private MDataKey key;
	private MDataValue value;

	public MDataEntry() {
		this.key = new MDataKey();
		this.value = new MDataValue();
	}
	public MDataEntry(MDataKey key, MDataValue value) {
		this.key = key;
		this.value = value;
	}
	public MDataKey getKey() {
		return key;
	}

	public void setKey(final MDataKey val) {
		key = val;
	}

	public MDataValue getValue() {
		return value;
	}

	public void setValue(final MDataValue val) {
		value = val;
	}

}

