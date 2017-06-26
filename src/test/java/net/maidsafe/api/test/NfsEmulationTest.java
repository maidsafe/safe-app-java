package net.maidsafe.api.test;

import junit.framework.TestCase;
import net.maidsafe.api.model.MutableData;

/**
 * Created by expertonetechnologies on 21/06/17.
 */
public class NfsEmulationTest extends TestCase {

    public void testNfsUpdate() throws Exception {
        String key = "key";
        String value = "value";

        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getBytes(), value.getBytes(), true);

    }
}
