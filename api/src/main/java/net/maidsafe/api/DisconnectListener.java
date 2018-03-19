package net.maidsafe.api;

import net.maidsafe.api.listener.OnDisconnected;
import net.maidsafe.safe_app.CallbackVoid;

class DisconnectListener {

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
