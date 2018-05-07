package net.maidsafe.safe_app;

public interface CallbackResultInt {
	public void call(FfiResult result, int reqId);
}
