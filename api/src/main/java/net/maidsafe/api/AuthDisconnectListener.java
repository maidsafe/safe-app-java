package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.safe_authenticator.CallbackVoid;


public class AuthDisconnectListener {

  private OnDisconnected onDisconnected;
  private CallbackVoid callback = new CallbackVoid() {
    @Override
    public void call() {
      if (onDisconnected == null) {
        return;
      }
      onDisconnected.disconnected(null);
    }
  };

  public void setListener(OnDisconnected onDisconnected) {
    this.onDisconnected = onDisconnected;
  }

  public CallbackVoid getCallback() {
    return callback;
  }

}
