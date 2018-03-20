//package net.maidsafe.api;
//
//import net.maidsafe.api.model.EncryptKeyPair;
//import net.maidsafe.api.model.MDataValue;
//import net.maidsafe.api.model.NativeHandle;
//import net.maidsafe.safe_app.MDataInfo;
//import net.maidsafe.safe_app.PermissionSet;
//import net.maidsafe.test.utils.Helper;
//import net.maidsafe.test.utils.SessionLoader;
//import net.maidsafe.utils.Constants;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.List;
//
//public class MDataTest {
//    static {
//        SessionLoader.load();
//    }
//
//    private void publicMDataCrud(MDataInfo mDataInfo) throws Exception {
//        NativeHandle permissionHandle = MDataPermission.newPermissionHandle().get();
//        PermissionSet permissionSet = new PermissionSet();
//        permissionSet.setInsert(true);
//        permissionSet.setUpdate(true);
//        permissionSet.setRead(true);
//        permissionSet.setDelete(true);
//        MDataPermission.insert(permissionHandle, Crypto.getAppPublicSignKey().get(), permissionSet).get();
//        NativeHandle entriesHandle = MDataEntries.newEntriesHandle().get();
//        MDataEntries.insert(entriesHandle, "someKey".getBytes(), "someValue".getBytes()).get();
//        MData.put(mDataInfo, permissionHandle, entriesHandle).get();
//
//        NativeHandle actionHandle = MDataEntryAction.newEntryAction().get();
//        MDataEntryAction.insert(actionHandle, "someKey2".getBytes(), "someValue2".getBytes()).get();
//        MData.mutateEntries(mDataInfo, actionHandle).get();
//
//        entriesHandle = MData.getEntriesHandle(mDataInfo).get();
//        List<MDataEntry> entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(2, entries.size());
//
//        // Update
//        actionHandle = MDataEntryAction.newEntryAction().get();
//        MDataEntry entry = entries.get(0);
//        byte[] updatedValue = Helper.randomAlphaNumeric(10).getBytes();
//        MDataEntryAction.update(actionHandle, entry.getKey(), updatedValue, entry.getVersion() + 1).get();
//        MData.mutateEntries(mDataInfo, actionHandle).get();
//        MDataValue value = MData.getValue(mDataInfo, entry.getKey()).get();
//        Assert.assertEquals(new String(updatedValue), new String(value.getValue()));
//        // Delete
//        actionHandle = MDataEntryAction.newEntryAction().get();
//        MDataEntryAction.delete(actionHandle, entry.getKey(), value.getVersion() + 1).get();
//
//        entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(1, entries.size());
//
//        long serialisedSize = MData.getSerialisedSize(mDataInfo).get();
//        byte[] serialisedMData = MData.serialise(mDataInfo).get();
//        Assert.assertEquals(serialisedSize, serialisedMData.length);
//        mDataInfo = MData.deserialise(serialisedMData).get();
//        entriesHandle = MData.getEntriesHandle(mDataInfo).get();
//        entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(1, entries.size());
//        long version = MData.getVersion(mDataInfo).get();
//        Assert.assertEquals(3, version);
//        List<byte[]> keys = MData.getKeys(mDataInfo).get();
//        List<MDataValue> values = MData.getValues(mDataInfo).get();
//        Assert.assertEquals(keys.size(), values.size());
//        Assert.assertEquals(entries.get(0).getKey(), keys.get(0));
//        Assert.assertEquals(entries.get(0).getValue(), values.get(0).getValue());
//
//        PermissionSet permissions = MData.getPermissionForUser(Crypto.getAppPublicSignKey().get(), mDataInfo).get();
//        Assert.assertTrue(permissions.getInsert());
//        Assert.assertTrue(permissions.getUpdate());
//        Assert.assertTrue(permissions.getRead());
//        Assert.assertTrue(permissions.getDelete());
//        Assert.assertFalse(permissions.getManagePermission());
//    }
//
//    private void privateMDataCrud(MDataInfo mDataInfo) throws Exception {
//        NativeHandle permissionHandle = MDataPermission.newPermissionHandle().get();
//        PermissionSet permissionSet = new PermissionSet();
//        permissionSet.setInsert(true);
//        permissionSet.setUpdate(true);
//        permissionSet.setRead(true);
//        MDataPermission.insert(permissionHandle, Crypto.getAppPublicSignKey().get(), permissionSet).get();
//        NativeHandle entriesHandle = MDataEntries.newEntriesHandle().get();
//        byte[] key = MData.encryptEntryKey(mDataInfo, "someKey".getBytes()).get();
//        byte[] value = MData.encryptEntryValue(mDataInfo, "someValue".getBytes()).get();
//        MDataEntries.insert(entriesHandle, key, value).get();
//        MData.put(mDataInfo, permissionHandle, entriesHandle).get();
//
//        NativeHandle actionHandle = MDataEntryAction.newEntryAction().get();
//        key = MData.encryptEntryKey(mDataInfo, "someKey2".getBytes()).get();
//        value = MData.encryptEntryValue(mDataInfo, "someValue2".getBytes()).get();
//        MDataEntryAction.insert(actionHandle, key, value).get();
//        MData.mutateEntries(mDataInfo, actionHandle).get();
//
//        entriesHandle = MData.getEntriesHandle(mDataInfo).get();
//        List<MDataEntry> entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(2, entries.size());
//
//        // Update
//        actionHandle = MDataEntryAction.newEntryAction().get();
//        MDataEntry entry = entries.get(0);
//        byte[] updatedValue = Helper.randomAlphaNumeric(10).getBytes();
//        MDataEntryAction.update(actionHandle, entry.getKey(), updatedValue, entry.getVersion() + 1).get();
//        MData.mutateEntries(mDataInfo, actionHandle).get();
//        MDataValue entryValue = MData.getValue(mDataInfo, entry.getKey()).get();
//        byte[] decryptedValue = MData.decrypt(mDataInfo, entryValue.getValue()).get();
//        Assert.assertEquals(new String(updatedValue), new String(decryptedValue));
//        // Delete
//        actionHandle = MDataEntryAction.newEntryAction().get();
//        MDataEntryAction.delete(actionHandle, entry.getKey(), entryValue.getVersion() + 1).get();
//
//        entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(1, entries.size());
//
//        long serialisedSize = MData.getSerialisedSize(mDataInfo).get();
//        byte[] serialisedMData = MData.serialise(mDataInfo).get();
//        Assert.assertEquals(serialisedSize, serialisedMData.length);
//        mDataInfo = MData.deserialise(serialisedMData).get();
//        entriesHandle = MData.getEntriesHandle(mDataInfo).get();
//        entries = MDataEntries.listEntries(entriesHandle).get();
//        Assert.assertEquals(1, entries.size());
//    }
//
//    @Test
//    public void randomPublicMDataCRUDTest() throws Exception {
//        Client.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        long tagType = 15001;
//        MDataInfo mDataInfo = MData.getRandomPublicMData(tagType).get();
//        publicMDataCrud(mDataInfo);
//    }
//
//    @Test
//    public void publicMDataCRUDTest() throws Exception {
//        Client.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        long tagType = 15001;
//        MDataInfo mDataInfo = new MDataInfo();
//        mDataInfo.setName(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes());
//        mDataInfo.setTypeTag(tagType);
//        publicMDataCrud(mDataInfo);
//    }
//
//    @Test
//    public void randomPrivateMDataCRUDTest() throws Exception {
//        Client.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        long tagType = 15001;
//        MDataInfo mDataInfo = MData.getRandomPrivateMData(tagType).get();
//        privateMDataCrud(mDataInfo);
//    }
//
//    @Test
//    public void privateMDataCRUDTest() throws Exception {
//        Client.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        long tagType = 15001;
//        EncryptKeyPair encryptKeyPair = Crypto.generateEncryptKeyPair().get();
//        byte[] nonce = Crypto.generateNonce().get();
//        byte[] secretKey = Crypto.getRawSecretEncryptKey(encryptKeyPair.getSecretEncryptKey()).get();
//        MDataInfo mDataInfo = MData.getPrivateMData(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes(), tagType, secretKey, nonce).get();
//        privateMDataCrud(mDataInfo);
//    }
//
//}
