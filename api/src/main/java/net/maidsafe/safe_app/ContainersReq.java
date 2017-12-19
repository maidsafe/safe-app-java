package net.maidsafe.safe_app;

/// Containers request
public class ContainersReq {
    private AppExchangeInfo app;
    private ContainerPermission containers;
    private long containersLen;
    private long containersCap;

    public AppExchangeInfo getApp() {
        return app;
    }

    public void setApp(final AppExchangeInfo val) {
        app = val;
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

