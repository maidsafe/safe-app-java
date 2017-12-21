package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Test;

public class MDataTest {
    static {
        SessionLoader.load();
    }

    @Test
    public void publicMDataTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        long tagType = 15001;
        MDataInfo mDataInfo = MData.getRandomPublicMData(tagType).get();
        NativeHandle permissionHandle = MDataPermission.newPermissionHandle().get();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setInsert(true);
        MDataPermission.insert(permissionHandle, Crypto.getAppPublicSignKey().get(), permissionSet).get();
        MData.put(mDataInfo, permissionHandle, MDataEntries.newEntriesHandle().get());
    }
}
