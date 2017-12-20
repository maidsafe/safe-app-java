package net.maidsafe.safe_app;

public interface CallbackResultCipherOptHandle {
	public void call(FfiResult result, long handle);
}
