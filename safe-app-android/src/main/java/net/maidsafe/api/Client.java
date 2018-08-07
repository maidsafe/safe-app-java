package net.maidsafe.api;

public class Client extends Session {

    Client(AppHandle app, DisconnectListener disconnectListener) {
        super(app, disconnectListener);
    }

    public static void load() {
        try {
            clientTypeFactory = ClientTypeFactory.load(Client.class);
            System.loadLibrary("safe_app_jni");
            if(Session.isMock()) {
                System.loadLibrary("safe_authenticator_jni");
            }
        }
        catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
