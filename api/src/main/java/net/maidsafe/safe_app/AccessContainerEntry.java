package net.maidsafe.safe_app;

/// Access container entry for a single app.
public class AccessContainerEntry {
    private ContainerInfo containersPtr;
    private long containersLen;
    private long containersCap;

    public ContainerInfo getContainersPtr() {
        return containersPtr;
    }

    public void setContainersPtr(final ContainerInfo val) {
        containersPtr = val;
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

