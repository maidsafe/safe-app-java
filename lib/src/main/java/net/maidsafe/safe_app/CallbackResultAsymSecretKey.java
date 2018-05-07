package net.maidsafe.safe_app;

public interface CallbackResultAsymSecretKey {
	public void call(FfiResult result, byte[] secEncKey);
}
