package net.maidsafe.api;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest {
    @Test
    public void addTest() {
        SafeApi api = new SafeApi();
        Assert.assertEquals(10, api.add(5,5));
    }
}
