package net.maidsafe.safe_app;

public interface CallbackResultLong {
	public void call(FfiResult result, long serialisedSize);
}
