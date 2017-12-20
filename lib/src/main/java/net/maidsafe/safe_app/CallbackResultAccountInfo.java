package net.maidsafe.safe_app;

public interface CallbackResultAccountInfo {
	public void call(FfiResult result, AccountInfo accountInfo);
}
