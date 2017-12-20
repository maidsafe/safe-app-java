package net.maidsafe.safe_app;

/// Containers request
public class ContainersReq {
	public ContainersReq() { }

	private AppExchangeInfo app;

	public AppExchangeInfo getApp() {
		return app;
	}

	public void setApp(final AppExchangeInfo val) {
		app = val;
	}

	private ContainerPermissions[] containers;

	public ContainerPermissions[] getContainer() {
		return containers;
	}

	public void setContainer(final ContainerPermissions[] val) {
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

	public ContainersReq(AppExchangeInfo app, ContainerPermissions[] containers, long containersLen, long containersCap) { }
}

