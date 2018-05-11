package net.maidsafe.safe_app;

/// Containers request
public class ContainersReq {
	private AppExchangeInfo app;
	private ContainerPermissions[] containers;
	private long containersLen;
	private long containersCap;

	public ContainersReq() {
		this.app = new AppExchangeInfo();
		this.containers = new ContainerPermissions[] {};
	}
	public ContainersReq(AppExchangeInfo app, ContainerPermissions[] containers, long containersLen, long containersCap) {
		this.app = app;
		this.containers = containers;
		this.containersLen = containersLen;
		this.containersCap = containersCap;
	}
	public AppExchangeInfo getApp() {
		return app;
	}

	public void setApp(final AppExchangeInfo val) {
		this.app = val;
	}

	public ContainerPermissions[] getContainer() {
		return containers;
	}

	public void setContainer(final ContainerPermissions[] val) {
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

