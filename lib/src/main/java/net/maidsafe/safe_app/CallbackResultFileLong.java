package net.maidsafe.safe_app;

public interface CallbackResultFileLong {
	public void call(FfiResult result, File file, long version);
}
