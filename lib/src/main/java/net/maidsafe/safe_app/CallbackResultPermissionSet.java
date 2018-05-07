package net.maidsafe.safe_app;

public interface CallbackResultPermissionSet {
	public void call(FfiResult result, PermissionSet permSet);
}
