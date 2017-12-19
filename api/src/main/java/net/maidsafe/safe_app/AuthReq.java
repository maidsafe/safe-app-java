package net.maidsafe.safe_app;

/// Represents an authorisation request
public class AuthReq {
    private AppExchangeInfo app;
    private boolean appContainer;
    private ContainerPermission containers;
    private long containersLen;
    private long containersCap;

    public AppExchangeInfo getApp() {
        return app;
    }

    public void setApp(final AppExchangeInfo val) {
        app = val;
    }

    public boolean getAppContainer() {
        return appContainer;
    }

    public void setAppContainer(final boolean val) {
        appContainer = val;
    }

    public ContainerPermission getContainer() {
        return containers;
    }

    public void setContainer(final ContainerPermission val) {
        containers = val;
    }

    public long getContainersLen() {
        return containersLen;
    }

    public void setContainersLen(final long val) {
        containersLen = val;
    }

    public long getContainersCap() {
        return containersCap;
    }

    public void setContainersCap(final long val) {
        containersCap = val;
    }

}

