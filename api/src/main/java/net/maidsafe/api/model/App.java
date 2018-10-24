package net.maidsafe.api.model;

public class App {
    final private String id;
    final private String name;
    final private String vendor;
    final private String version;

    public App(final String id, final String name, final String vendor, final String version) {
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
