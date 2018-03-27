package net.maidsafe.api;

public class Client extends Session {

    Client(AppHandle app, DisconnectListener disconnectListener) {
        super(app, disconnectListener);
    }

}
