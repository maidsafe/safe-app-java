package net.maidsafe.safe_app;

/// Access container entry for a single app.
public class AccessContainerEntry {
	public AccessContainerEntry() { }
	private ContainerInfo[] containers;

	public ContainerInfo[] getContainer() {
		return containers;
	}

	public void setContainer(final ContainerInfo[] val) {
		containers = val;
	}

	private long containersLen;

	public long getContainersLen() {
		return containersLen;
	}

	public void setContainersLen(final long val) {
		containersLen = val;
	}

	private long containersCap;

	public long getContainersCap() {
		return containersCap;
	}

	public void setContainersCap(final long val) {
		containersCap = val;
	}

	public AccessContainerEntry(ContainerInfo[] containers, long containersLen, long containersCap) {
		this.containers = containers;
		this.containersLen = containersLen;
		this.containersCap = containersCap;
}
}

