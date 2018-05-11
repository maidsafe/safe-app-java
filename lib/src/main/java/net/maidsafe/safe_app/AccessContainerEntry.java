package net.maidsafe.safe_app;

/// Access container entry for a single app.
public class AccessContainerEntry {
	private ContainerInfo[] containers;
	private long containersLen;
	private long containersCap;

	public AccessContainerEntry() {
		this.containers = new ContainerInfo[] {};
	}
	public AccessContainerEntry(ContainerInfo[] containers, long containersLen, long containersCap) {
		this.containers = containers;
		this.containersLen = containersLen;
		this.containersCap = containersCap;
	}
	public ContainerInfo[] getContainer() {
		return containers;
	}

	public void setContainer(final ContainerInfo[] val) {
		this.containers = val;
	}

	public long getContainersLen() {
		return containersLen;
	}

	public void setContainersLen(final long val) {
		this.containersLen = val;
	}

	public long getContainersCap() {
		return containersCap;
	}

	public void setContainersCap(final long val) {
		this.containersCap = val;
	}

}

