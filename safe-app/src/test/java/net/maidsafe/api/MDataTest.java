package net.maidsafe.api;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.safe_app.*;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;
import net.maidsafe.utils.Constants;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MDataTest {
    static {
        SessionLoader.load();
    }

    private void publicMDataCrud(MDataInfo mDataInfo) throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setRead(true);
        permissionSet.setDelete(true);
        client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(), permissionSet).get();
        NativeHandle entriesHandle = client.mDataEntries.newEntriesHandle().get();
        client.mDataEntries.insert(entriesHandle, "someKey".getBytes(), "someValue".getBytes()).get();
        client.mData.put(mDataInfo, permissionHandle, entriesHandle).get();

        NativeHandle actionHandle = client.mDataEntryAction.newEntryAction().get();
        client.mDataEntryAction.insert(actionHandle, "someKey2".getBytes(), "someValue2".getBytes()).get();
        client.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
        List<MDataEntry> entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(2, entries.size());

        // Update
        actionHandle = client.mDataEntryAction.newEntryAction().get();
        MDataEntry entry = entries.get(0);
        byte[] updatedValue = Helper.randomAlphaNumeric(10).getBytes();
        client.mDataEntryAction.update(actionHandle, entry.getKey().getVal(), updatedValue, entry.getValue().getEntryVersion() + 1).get();
        client.mData.mutateEntries(mDataInfo, actionHandle).get();
        MDataValue value = client.mData.getValue(mDataInfo, entry.getKey().getVal()).get();
        Assert.assertEquals(new String(updatedValue), new String(value.getContent()));
        // Delete
        actionHandle = client.mDataEntryAction.newEntryAction().get();
        client.mDataEntryAction.delete(actionHandle, entry.getKey().getVal(), value.getEntryVersion() + 1).get();

        entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(1, entries.size());

        long serialisedSize = client.mData.getSerialisedSize(mDataInfo).get();
        byte[] serialisedMData = client.mData.serialise(mDataInfo).get();
        Assert.assertEquals(serialisedSize, serialisedMData.length);
        mDataInfo = client.mData.deserialise(serialisedMData).get();
        entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
        entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(1, entries.size());
        long version = client.mData.getVersion(mDataInfo).get();
        Assert.assertEquals(3, version);
        List<MDataKey> keys = client.mData.getKeys(mDataInfo).get();
        List<MDataValue> values = client.mData.getValues(mDataInfo).get();
        Assert.assertEquals(keys.size(), values.size());
        Assert.assertEquals(entries.get(0).getKey(), keys.get(0));
        Assert.assertEquals(entries.get(0).getValue(), values.get(0).getContent());

        PermissionSet permissions = client.mData.getPermissionForUser(client.crypto.getAppPublicSignKey().get(), mDataInfo).get();
        Assert.assertTrue(permissions.getInsert());
        Assert.assertTrue(permissions.getUpdate());
        Assert.assertTrue(permissions.getRead());
        Assert.assertTrue(permissions.getDelete());
        Assert.assertFalse(permissions.getManagePermission());
    }

    private void privateMDataCrud(MDataInfo mDataInfo) throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setRead(true);
        client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(), permissionSet).get();
        NativeHandle entriesHandle = client.mDataEntries.newEntriesHandle().get();
        byte[] key = client.mData.encryptEntryKey(mDataInfo, "someKey".getBytes()).get();
        byte[] value = client.mData.encryptEntryValue(mDataInfo, "someValue".getBytes()).get();
        client.mDataEntries.insert(entriesHandle, key, value).get();
        client.mData.put(mDataInfo, permissionHandle, entriesHandle).get();

        NativeHandle actionHandle = client.mDataEntryAction.newEntryAction().get();
        key = client.mData.encryptEntryKey(mDataInfo, "someKey2".getBytes()).get();
        value = client.mData.encryptEntryValue(mDataInfo, "someValue2".getBytes()).get();
        client.mDataEntryAction.insert(actionHandle, key, value).get();
        client.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
        List<MDataEntry> entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(2, entries.size());

        // Update
        actionHandle = client.mDataEntryAction.newEntryAction().get();
        MDataEntry entry = entries.get(0);
        byte[] updatedValue = Helper.randomAlphaNumeric(10).getBytes();
        client.mDataEntryAction.update(actionHandle, entry.getKey().getVal(), updatedValue, entry.getValue().getEntryVersion() + 1).get();
        client.mData.mutateEntries(mDataInfo, actionHandle).get();
        MDataValue entryValue = client.mData.getValue(mDataInfo, entry.getKey().getVal()).get();
        byte[] decryptedValue = client.mData.decrypt(mDataInfo, entryValue.getContent()).get();
        Assert.assertEquals(new String(updatedValue), new String(decryptedValue));
        // Delete
        actionHandle = client.mDataEntryAction.newEntryAction().get();
        client.mDataEntryAction.delete(actionHandle, entry.getKey().getVal(), entryValue.getEntryVersion() + 1).get();

        entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(1, entries.size());

        long serialisedSize = client.mData.getSerialisedSize(mDataInfo).get();
        byte[] serialisedMData = client.mData.serialise(mDataInfo).get();
        Assert.assertEquals(serialisedSize, serialisedMData.length);
        mDataInfo = client.mData.deserialise(serialisedMData).get();
        entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
        entries = client.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(1, entries.size());
    }

//    @Test
//    public void randomPublicMDataCRUDTest() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
//        long tagType = 15001;
//        MDataInfo mDataInfo = client.mData.getRandomPublicMData(tagType).get();
//        publicMDataCrud(mDataInfo);
//    }

    @Test
    public void publicMDataCRUDTest() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        long tagType = 15001;
        MDataInfo mDataInfo = new MDataInfo();
        mDataInfo.setName(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes());
        mDataInfo.setTypeTag(tagType);
        publicMDataCrud(mDataInfo);
    }

//    @Test
//    public void randomPrivateMDataCRUDTest() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
//        long tagType = 15001;
//        MDataInfo mDataInfo = client.mData.getRandomPrivateMData(tagType).get();
//        privateMDataCrud(mDataInfo);
//    }

//    @Test
//    public void privateMDataCRUDTest() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
//        long tagType = 15001;
//        EncryptKeyPair encryptKeyPair = client.crypto.generateEncryptKeyPair().get();
//        byte[] nonce = client.crypto.generateNonce().get();
//        byte[] secretKey = client.crypto.getRawSecretEncryptKey(encryptKeyPair.getSecretEncryptKey()).get();
//        MDataInfo mDataInfo = client.mData.getPrivateMData(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes(), tagType, secretKey, nonce).get();
//        privateMDataCrud(mDataInfo);
//    }

}
