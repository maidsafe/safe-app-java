package net.maidsafe.safe_app;

public interface CallbackResultEncryptPubKeyHandle {
	public void call(FfiResult result, long pkH);
}
