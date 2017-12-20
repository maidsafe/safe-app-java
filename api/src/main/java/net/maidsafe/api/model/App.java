package net.maidsafe.api.model;

public class App {
    private String id;
    private String name;
    private String vendor;
    private String version;

    public App(String id, String name, String vendor, String version) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public String getVersion() {
        return version;
    }
}
