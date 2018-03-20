package net.maidsafe.api;

public class Client extends Session {

    BaseSession(AppHandle app, DisconnectListener disconnectListener) {
        super(app, disconnectListener);
    }

}
