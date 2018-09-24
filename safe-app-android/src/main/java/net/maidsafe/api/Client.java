package net.maidsafe.api;

import android.system.Os;

public class Client extends Session {

    Client(AppHandle app, DisconnectListener disconnectListener) {
        super(app, disconnectListener);
    }

    public static void load() {
        clientTypeFactory = ClientTypeFactory.load(Client.class);
        System.loadLibrary("safe_app_jni");
        if(Session.isMock()) {
            System.loadLibrary("safe_authenticator_jni");
        }
        try {
            Os.setenv("SAFE_MOCK_VAULT_PATH", "/data/data/net.maidsafe.api.test/cache", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
