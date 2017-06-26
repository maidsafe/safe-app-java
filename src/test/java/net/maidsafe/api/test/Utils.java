package net.maidsafe.api.test;

import com.sun.jna.ptr.PointerByReference;
import net.maidsafe.api.Crypto;
import net.maidsafe.api.MDataInfo;
import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.*;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.FfiContainerPermission;

import java.util.List;

public class Utils {

    public static SafeClient getTestApp() {
        App app = new App(null, null, null);
        PointerByReference appPointerRef = new PointerByReference();
        BindingFactory.getInstance().getAuth().test_create_app(appPointerRef);
        app.setAppHandle(appPointerRef.getValue());
        return new SafeClient(app);
    }

    public static SafeClient getTestAppWithAccess() throws Exception {
        App app = new App(new AppInfo("com.maidsafe.test.app", "Test App",
                "MaidSafe"), null, null);
        PointerByReference appPointerRef = new PointerByReference();

        BindingFactory
                .getInstance()
                .getAuth()
                .test_create_app_with_access(new FfiContainerPermission[1], 0,
                        appPointerRef);
        app.setAppHandle(appPointerRef.getValue());
        return new SafeClient(app);
    }

    public static SafeClient getTestAppWithAccess(
            List<ContainerPermission> access) throws Exception {
        App app = new App(new AppInfo("com.maidsafe", "Test App", "Maidsafe"),
                null, null);
        PointerByReference appPointerRef = new PointerByReference();
        final FfiContainerPermission[] accessPermissions = new FfiContainerPermission[access
                .size()];
        int i = 0;
        for (ContainerPermission container : access) {
            accessPermissions[i] = new FfiContainerPermission(container);
            i++;
        }

        BindingFactory
                .getInstance()
                .getAuth()
                .test_create_app_with_access(accessPermissions, access.size(),
                        appPointerRef);
        app.setAppHandle(appPointerRef.getValue());
        return new SafeClient(app);
    }

    public static MutableData getTestRandomMDataWithPermissions(byte[] key, byte[] value, boolean isPrivate) throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        if (isPrivate)
            mData = mDataInfo.getRandomPrivateType(0).get();
        else
            mData = mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(key, value);

        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        mDataPermissionSet.allowPermissionSet(Permission.Read);
        mDataPermissionSet.allowPermissionSet(Permission.Update);
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        mDataPermissionSet.allowPermissionSet(Permission.ManagePermissions);
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        mDataPermissions.insertPermissionSet(signKey, mDataPermissionSet);
        mData.put(mDataPermissions, mDataEntries);
        return mData;
    }

    public static MutableData quickSetUp(byte[] key, byte[] value, MutableData mData) throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        if(key != null && value != null){
            mDataEntries.insertEntry(key, value);
        }

        Crypto crypto = client.crypto();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        mDataPermissionSet.allowPermissionSet(Permission.Read);
        mDataPermissionSet.allowPermissionSet(Permission.Update);
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        mDataPermissionSet.allowPermissionSet(Permission.ManagePermissions);
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        mDataPermissions.insertPermissionSet(signKey, mDataPermissionSet);
        mData.put(mDataPermissions, mDataEntries);


        return mData;
    }


    public static MutableData getTestPublicMDataWithPermissions(byte[] key, byte[] value) throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();
        MDataInfo mDataInfo = client.mDataInfo();
        mData = mDataInfo.getPublicType(key, 0).get();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();
        mDataEntries.insertEntry(key, value);
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        mDataPermissionSet.allowPermissionSet(Permission.Read);
        mDataPermissionSet.allowPermissionSet(Permission.Update);
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        mDataPermissionSet.allowPermissionSet(Permission.ManagePermissions);
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        mDataPermissions.insertPermissionSet(signKey, mDataPermissionSet);
        mData.put(mDataPermissions, mDataEntries);

        return mData;
    }

    public static MutableData getTestPrivateMDataWithPermissions(byte[] name, long tagType, byte[] secKey, byte[] nonce, byte[] key, byte[] value) throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();
        MDataInfo mDataInfo = client.mDataInfo();
        mData = mDataInfo.getPrivateType(new XorName(name), tagType, secKey, nonce).get();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();
        mDataEntries.insertEntry(key, value);
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        mDataPermissionSet.allowPermissionSet(Permission.Read);
        mDataPermissionSet.allowPermissionSet(Permission.Update);
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        mDataPermissionSet.allowPermissionSet(Permission.ManagePermissions);
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        mDataPermissions.insertPermissionSet(signKey, mDataPermissionSet);
        mData.put(mDataPermissions, mDataEntries);

        return mData;
    }


}
