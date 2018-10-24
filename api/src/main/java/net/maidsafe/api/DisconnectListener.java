package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.safe_app.CallbackVoid;

class DisconnectListener {

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

    DisconnectListener() {
        // Constructor intentionally empty
    }

    public void setListener(final OnDisconnected onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public CallbackVoid getCallback() {
        return callback;
    }

}
