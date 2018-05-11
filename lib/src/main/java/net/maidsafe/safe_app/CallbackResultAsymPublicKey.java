package net.maidsafe.safe_app;

public interface CallbackResultAsymPublicKey {
	public void call(FfiResult result, byte[] pubEncKey);
}
