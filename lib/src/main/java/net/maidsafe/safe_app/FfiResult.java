package net.maidsafe.safe_app;

/// FFI result wrapper
public class FfiResult {
	private int errorCode;
	private String description;

	public FfiResult() {
		this.description = new String();
	}
	public FfiResult(int errorCode, String description) {
		this.errorCode = errorCode;
		this.description = description;
	}
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final int val) {
		this.errorCode = val;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String val) {
		this.description = val;
	}

}

