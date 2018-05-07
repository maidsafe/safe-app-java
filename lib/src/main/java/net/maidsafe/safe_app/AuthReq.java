package net.maidsafe.safe_app;

/// Represents an authorisation request
public class AuthReq {
	private AppExchangeInfo app;
	private boolean appContainer;
	private ContainerPermissions[] containers;
	private long containersLen;
	private long containersCap;

	public AuthReq() {
		this.app = new AppExchangeInfo();
		this.containers = new ContainerPermissions[] {};
	}
	public AuthReq(AppExchangeInfo app, boolean appContainer, ContainerPermissions[] containers, long containersLen, long containersCap) {
		this.app = app;
		this.appContainer = appContainer;
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

	public boolean getAppContainer() {
		return appContainer;
	}

	public void setAppContainer(final boolean val) {
		this.appContainer = val;
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

