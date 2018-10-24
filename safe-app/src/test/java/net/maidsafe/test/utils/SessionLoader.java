package net.maidsafe.test.utils;

import net.maidsafe.api.Client;

public class SessionLoader{

    private static SessionLoader instance;

    private SessionLoader() {
        Client.load();
    }

    public static void load() {
        if (instance == null) {
            instance = new SessionLoader();
        }
    }
}
