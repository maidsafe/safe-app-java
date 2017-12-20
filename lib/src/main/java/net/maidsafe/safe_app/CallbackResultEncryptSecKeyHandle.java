package net.maidsafe.safe_app;

public interface CallbackResultEncryptSecKeyHandle {
	public void call(FfiResult result, long skH);
}
