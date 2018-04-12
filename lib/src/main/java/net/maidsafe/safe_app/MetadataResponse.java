package net.maidsafe.safe_app;

/// User metadata for mutable data
public class MetadataResponse {
	private String name;
	private String description;
	private byte[] xorName;
	private long typeTag;

	public MetadataResponse() {
		this.name = new String();
		this.description = new String();
		this.xorName = new byte[] {};
	}
	public MetadataResponse(String name, String description, byte[] xorName, long typeTag) {
		this.name = name;
		this.description = description;
		this.xorName = xorName;
		this.typeTag = typeTag;
	}
	public String getName() {
		return name;
	}

	public void setName(final String val) {
		name = val;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String val) {
		description = val;
	}

	public byte[] getXorName() {
		return xorName;
	}

	public void setXorName(final byte[] val) {
		xorName = val;
	}

	public long getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(final long val) {
		typeTag = val;
	}

}

