package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.safe_authenticator.CallbackVoid;


public class AuthDisconnectListener {

  OnDisconnected onDisconnected;
  private final CallbackVoid callback = new CallbackVoid() {
    @Override
    public void call() {
      if (onDisconnected == null) {
        return;
      }
      onDisconnected.disconnected(null);
    }
  };

  AuthDisconnectListener() {
      // Constructor intentionally empty
  }

  public void setListener(final OnDisconnected onDisconnected) {
    this.onDisconnected = onDisconnected;
  }

  public CallbackVoid getCallback() {
    return callback;
  }

}
