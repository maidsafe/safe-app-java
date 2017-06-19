package net.maidsafe.api.test;

import java.util.Arrays;
import java.util.List;

import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.ContainerPermission;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.api.model.Permission;
import junit.framework.TestCase;

public class AccessContainerTest extends TestCase {

	public void testGetContainers() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess();
		List<String> containers = client.container().getContainers().get();
		assertEquals(false, containers.isEmpty());
		assertEquals(1, containers.size());
	}

	public void testGetContainerswithPermissions() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess(Arrays
				.asList(new ContainerPermission("_public", Arrays
						.asList(Permission.Read))));
		List<String> containers = client.container().getContainers().get();
		assertEquals(false, containers.isEmpty());
		assertEquals(2, containers.size());
	}

	public void testGetContainer() throws Exception {
		SafeClient client = Utils.getTestAppWithAccess(Arrays
				.asList(new ContainerPermission("_public", Arrays.asList(
						Permission.Read, Permission.Delete))));

		assertEquals(
				new Boolean(true),
				client.container()
						.hasAccess(
								"_public",
								Arrays.asList(Permission.Read,
										Permission.Delete)).get());
		MutableData md = client.container().getContainer("_public").get();
		assertEquals(false, md == null);
	}

	// public void testGetAppContainer() throws Exception {
	// SafeClient client = Utils.getTestAppWithAccess(Arrays
	// .asList(new ContainerPermission("_public", Arrays
	// .asList(Permission.Read))));
	// MutableDataTest md = client.container().getAppContainer().get();
	// assertEquals(false, md == null);
	// }

}
