package net.maidsafe.safe_app;

public interface CallbackResultEncryptPubKeyHandleEncryptSecKeyHandle {
	public void call(FfiResult result, long pkH, long skH);
}
