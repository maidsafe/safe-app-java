package net.maidsafe.safe_app;

public interface CallbackResultAsymNonce {
	public void call(FfiResult result, byte[] nonce);
}
