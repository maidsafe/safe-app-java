package net.maidsafe.safe_app;

public interface CallbackResultSewriterHandle {
    public void call(FfiResult result, long seH);
}
