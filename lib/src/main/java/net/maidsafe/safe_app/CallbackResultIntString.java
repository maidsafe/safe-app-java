package net.maidsafe.safe_app;

public interface CallbackResultIntString {
	public void call(FfiResult result, int reqId, String encodedPtr);
}
