package net.maidsafe.safe_app;

public interface CallbackResultFileContextHandle {
	public void call(FfiResult result, long fileH);
}
