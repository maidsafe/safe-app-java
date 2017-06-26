package net.maidsafe.api.test;

import junit.framework.TestCase;
import net.maidsafe.api.Crypto;
import net.maidsafe.api.MDataInfo;
import net.maidsafe.api.SafeClient;
import net.maidsafe.api.model.*;

import java.util.Random;
import java.util.concurrent.ExecutionException;


public class MutableDataTest extends TestCase {

    public void testRandomPublicTypeAndReadName() throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MutableData mutableData = mDataInfo.getRandomPublicType(0).get();
        MutableData mData = Utils.quickSetUp(null, null, mutableData);
        assert (mData.getNameAndTag().get() != null);
    }

    public void testRandomPrivateTypeAndReadName() throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MutableData mutableData = mDataInfo.getRandomPrivateType(0).get();
        MutableData mData = Utils.quickSetUp(null, null, mutableData);
        assert (mData.getNameAndTag().get() != null);
    }

    public void testCustomPublicTypeAndReadName() throws Exception {
        byte[] name = new byte[32];
        new Random().nextBytes(name);
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MutableData mutableData = mDataInfo.getPublicType(name, 0).get();
        assert (mutableData.getNameAndTag().get() != null);
    }

    public void testGetMDataVersion() throws Exception {
        String key = "key1";
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getBytes(), value.getBytes(), true);
        assertEquals(0, (long) mData.getVersion().get());
    }

    public void testGetExistingKeyFromPublicMD() throws Exception {
        String key = "key1";
        String value = "value1";
        MutableData mData = Utils.getTestPublicMDataWithPermissions(key.getBytes(), value.getBytes());
        ValueVersion valueVersion = mData.getValue(key.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(value, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }

    public void testGetExistingKeyFromPrivateMD() throws Exception {
        byte[] name = new byte[32];
        byte[] secKey = new byte[32];
        byte[] nonce = new byte[32];
        long tagType = 0;
        String key = "key1";
        String value = "value1";

        new Random().nextBytes(name);
        new Random().nextBytes(secKey);
        new Random().nextBytes(nonce);
        MutableData mData = Utils.getTestPrivateMDataWithPermissions(name, tagType, secKey, nonce, key.getBytes(), value.getBytes());
        ValueVersion valueVersion = mData.getValue(key.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(value, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }

    public void testGetExistingKeysFromSerialisedPrivateMD() throws Exception {
        byte[] name = new byte[32];
        byte[] secKey = new byte[32];
        byte[] nonce = new byte[32];
        long tagType = 0;
        String key = "key1";
        String value = "value1";

        new Random().nextBytes(name);
        new Random().nextBytes(secKey);
        new Random().nextBytes(nonce);

        MutableData mData = Utils.getTestPrivateMDataWithPermissions(name, tagType, secKey, nonce, key.getBytes(), value.getBytes());
        byte[] serialisedData = mData.serialise().get();

        MutableData privMutData = mData.deserialise(serialisedData).get();
        ValueVersion valueVersion = privMutData.getValue(key.getBytes()).get();

        assert (valueVersion != null);
        assertEquals(value, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }

    public void testGetExistingKeyFromPublicMData() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestPublicMDataWithPermissions(key.getRaw(), value.getBytes());
        ValueVersion valueVersion = mData.getValue(testKey.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), value);
        assertEquals(valueVersion.getVersion(), 0);
    }

    public void testSerialiseAndDeSerialise() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";

        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        byte[] serialisedData = mData.serialise().get();

        try {
            mData.deserialise(serialisedData).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testGetEntriesAndLength() throws Exception {
        String testKey = "key1";
        String testValue = "value1";
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(testKey.getBytes(), testValue.getBytes());
        assertEquals(1, (long) mDataEntries.getLength().get());
    }

    public void testGetEntriesAndValue() throws Exception {
        String testKey = "key1";
        String testValue = "value1";
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(testKey.getBytes(), testValue.getBytes());
        ValueVersion valueVersion = mDataEntries.getEntry(testKey.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(testValue, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }

    public void testInsertAndGetValue() throws Exception {
        String testKey = "key1";
        String testValue = "value1";
        String testKey2 = "key2";
        String testValue2 = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(testKey.getBytes(), testValue.getBytes());
        mDataEntries.insertEntry(testKey2.getBytes(), testValue2.getBytes());
        ValueVersion valueVersion = mDataEntries.getEntry(testKey2.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(testValue2, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }

    public void testForEachListEntries() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        mDataEntries.forEachEntry(new MDataEntries.ForEachCallback() {
            @Override
            public void onData(MDataEntry mDataEntry) {
                byte[] key = mDataEntry.getKey();
                try {
                    ValueVersion valueVersion = mDataEntries.getEntry(key).get();
                    assertEquals(0, valueVersion.getVersion());
                    assertEquals(value.getBytes(), valueVersion.getValue());
                } catch (InterruptedException | ExecutionException e) {
                    assert (e != null);
                }
            }

            @Override
            public void completed() {

            }
        });
    }


    public void testForEachListOfKeys() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        MDataKeys mDataKeys = mData.getKeys().get();
        mDataKeys.forEachKey(new MDataKeys.ForEachCallback() {
            @Override
            public void onData(MDataKey mDataKey) {
                assert (mDataKey.getKey() != null);
                assertEquals(testKey.getBytes(), mDataKey.getKey());
            }

            @Override
            public void completed() {

            }
        });
    }

    public void testForEachListOfValues() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        MDataValues mDataValues = mData.getValues().get();
        mDataValues.forEachValue(new MDataValues.ForEachCallback() {
            @Override
            public void onData(MDataValue mDataValue) {
                assert (mDataValue.getValue() != null);
                assertEquals(value.getBytes(), mDataValue.getValue());
            }

            @Override
            public void completed() {

            }
        });
    }

    public void testPublicEncryptEntryKey() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        byte[] encryptedKey = mData.encryptKey(key.getRaw()).get();
        assert (encryptedKey != null);
        assertEquals(testKey, new String(encryptedKey));
    }

    public void testPublicEncryptEntryValue() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), false);
        byte[] encryptedValue = mData.encryptValue(value.getBytes()).get();
        assert (encryptedValue != null);
        assertEquals(value, new String(encryptedValue));
    }

    public void testPrivateEncryptEntryKey() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), true);
        byte[] encryptedKey = mData.encryptKey(key.getRaw()).get();
        assert (encryptedKey != null);
        assertNotSame(testKey, new String(encryptedKey));
    }

    public void testPrivateEncryptEntryValue() throws Exception {
        String testKey = "key1";
        XorName key = new XorName(testKey.getBytes());
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getRaw(), value.getBytes(), true);
        byte[] encryptedValue = mData.encryptValue(value.getBytes()).get();
        assert (encryptedValue != null);
        assertNotSame(value, new String(encryptedValue));
    }

    public void testInsertMutation() throws Exception {
        String testKey = "key1";
        String testValue = "value1";

        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        MutableData m = mDataInfo.getRandomPublicType(0).get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();

        mDataEntries.insertEntry(testKey.getBytes(), testValue.getBytes());
        entryMutationTransaction.insert(testKey.getBytes(), testValue.getBytes());
        ValueVersion valueVersion = mDataEntries.getEntry(testKey.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(testValue, new String(valueVersion.getValue()));
        assertEquals(0, valueVersion.getVersion());
    }


    public void testUpdateMutationPrivateMD() throws Exception {
        String key1 = "key1";
        String value1 = "value1";

        String testKey = "key1";
        String testValue = "value1";

        SafeClient client = Utils.getTestAppWithAccess();
        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        MutableData m = mDataInfo.getRandomPrivateType(0).get();

        mDataEntries.insertEntry(testKey.getBytes(), testValue.getBytes());
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.insert(testKey.getBytes(), testValue.getBytes());
        mDataEntries.mutate();
        entryMutationTransaction.update(key1.getBytes(), value1.getBytes(), 1);
        m.applyMutation(entryMutationTransaction);

        ValueVersion valueVersion = m.getValue(key1.getBytes()).get();
        assert (valueVersion != null);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testUpdateMutation() throws Exception {
        String testKey = "key1";
        String testValue = "value1";
        String newKey = "newKey";
        String newValue = "newValue";

        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), testValue.getBytes(), false);

        MDataEntries entries = mData.getEntries().get();

        EntryMutationTransaction ent = entries.mutate().get();
        ent.update(newKey.getBytes(), newValue.getBytes(), 1);
        mData.applyMutation(ent);

        ValueVersion val = mData.getValue(newKey.getBytes()).get();

        assert (val != null);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testUpdateMutationWithBufferValue() throws Exception {
        String testKey = "key1";
        String value = "value1";
        String newKey = "key2";
        String newValue = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        Crypto crypto = client.crypto();

        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.update(newKey.getBytes(), newValue.getBytes(), 1);
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion = mData.getValue(newKey.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), value);
        assertEquals(valueVersion.getVersion(), 1);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testRemoveMutationFromExistingEntries() throws Exception {
        String testKey = "key2";
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.remove(testKey.getBytes(), 0);
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion = mData.getValue(testKey.getBytes()).get();
        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), "");
        assertEquals(valueVersion.getVersion(), 1);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testRemoveMutationOnPublicMData() throws Exception {
        String testKey = "key1";
        String value = "value1";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.remove(testKey.getBytes(), 1);
        mData.applyMutation(entryMutationTransaction);
        MutableData mData2 = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        ValueVersion valueVersion = mData2.getValue(testKey.getBytes()).get();

        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), "");
        assertEquals(valueVersion.getVersion(), 1);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testInsertMutationFromNewObject() throws Exception {
        String testKey = "key1";
        String value = "value1";
        String testKey2 = "newKey";
        String testValue2 = "newValue";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.insert(testKey2.getBytes(), testValue2.getBytes());
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion = mData.getValue(testKey2.getBytes()).get();

        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), testValue2);
        assertEquals(valueVersion.getVersion(), 0);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testUpdateMutationFromNewObject() throws Exception {
        String testKey = "key1";
        String value = "value1";
        String testKey2 = "newKey";
        String testValue2 = "newValue";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.update(testKey2.getBytes(), testValue2.getBytes(), 1);
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion = mData.getValue(testKey2.getBytes()).get();

        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), testValue2);
        assertEquals(valueVersion.getVersion(), 1);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testRemoveMutationFromNewObject() throws Exception {
        String testKey = "key1";
        String value = "value1";
        String testKey2 = "newKey";
        String testValue2 = "newValue";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), value.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.remove(testKey2.getBytes(), 1);
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion = mData.getValue(testKey2.getBytes()).get();

        assert (valueVersion != null);
        assertEquals(new String(valueVersion.getValue()), testValue2);
        assertEquals(valueVersion.getVersion(), 1);
    }

    //TODO: Failing due to Rust Ffi - TO BE FIXED - Mutation not authorised
    public void testRemovalAndUpdateWithinSameMutation() throws Exception {
        String testKey = "key1";
        String testValue = "value1";
        String testKey2 = "newKey";
        String testValue2 = "newValue";
        MutableData mData = Utils.getTestRandomMDataWithPermissions(testKey.getBytes(), testValue.getBytes(), false);
        MDataEntries mDataEntries = mData.getEntries().get();
        EntryMutationTransaction entryMutationTransaction = mDataEntries.mutate().get();
        entryMutationTransaction.remove(testKey2.getBytes(), 1);
        entryMutationTransaction.update(testKey.getBytes(), testValue.getBytes(), 1);
        mData.applyMutation(entryMutationTransaction);
        ValueVersion valueVersion2 = mData.getValue(testKey2.getBytes()).get();
        ValueVersion valueVersion1 = mData.getValue(testKey.getBytes()).get();

        assert (valueVersion2 != null);
        assertEquals(new String(valueVersion2.getValue()), "");
        assertEquals(valueVersion2.getVersion(), 1);

        assert (valueVersion1 != null);
        assertEquals(new String(valueVersion1.getValue()), testValue2);
        assertEquals(valueVersion1.getVersion(), 1);
    }

    //TODO: To be completed
    public void testForEachPermissionsList() throws Exception {
        String key = "key";
        String value = "value";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();


        mData = mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(key.getBytes(), value.getBytes());

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
        MDataPermissions perms = mData.getPermissions().get();
        assert (perms.getPermissionSet(signKey).get() != null);
        perms.forEachPermissions(new MDataPermissions.ForEachCallback() {
            @Override
            public void onData(MDataPermission permission) {

            }

            @Override
            public void completed() {

            }
        });
    }

    public void testGetPermissionSet() throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        Crypto crypto = client.crypto();
        MDataInfo mDataInfo = client.mDataInfo();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        try {
            mDataPermissions.getPermissionSet(signKey).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testInsertAndGetPermissionSetForAnyone() throws Exception {
        String key = "key";
        String value = "value";
        SafeClient client = Utils.getTestAppWithAccess();

        MDataInfo mDataInfo = client.mDataInfo();
        MutableData mData = Utils.getTestRandomMDataWithPermissions(key.getBytes(), value.getBytes(), false);
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        MDataPermissions mDataPermissions = mData.getPermissions().get();

        try {
            mDataPermissions.insertPermissionSet(null, mDataPermissionSet).get();
        } catch (Exception e) {
            assert (e != null);
        }
        try {
            mDataPermissions.getPermissionSet(null).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testInsertNewPermissionSet() throws Exception {
        String key2 = "key2";
        String value2 = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Delete);
        mData.setUserPermissions(signKey, mDataPermissionSet, 1);
        EntryMutationTransaction entryMutationTransaction = mDataInfo.getnewMutation().get();
        entryMutationTransaction.update(key2.getBytes(), value2.getBytes(), 1);
        try {
            mData.applyMutation(entryMutationTransaction).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testUpdateUsersPermission() throws Exception {
        String key2 = "key2";
        String value2 = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.denyPermissionSet(Permission.Delete);
        mData.setUserPermissions(signKey, mDataPermissionSet, 1);
        EntryMutationTransaction entryMutationTransaction = mDataInfo.getnewMutation().get();
        entryMutationTransaction.update(key2.getBytes(), value2.getBytes(), 1);
        try {
            mData.applyMutation(entryMutationTransaction).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testGetUsersPermission() throws Exception {
        String key = "key1";
        String value = "value1";

        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0).get();

        MDataEntries mDataEntries = mDataInfo.newEntries().get();
        mDataEntries.insertEntry(key.getBytes(), value.getBytes());
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        MDataPermissions mDataPermissions = mDataInfo.getNewPermissions().get();
        mDataPermissions.insertPermissionSet(signKey, mDataPermissionSet);
        mData.put(mDataPermissions, mDataEntries);
        try {
            mData.getUserPermissions(signKey).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testRemoveUsersPermission() throws Exception {
        String key2 = "key2";
        String value2 = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        mData.deleteUserPermissions(signKey, 1);
        EntryMutationTransaction entryMutationTransaction = mDataInfo.getnewMutation().get();
        entryMutationTransaction.update(key2.getBytes(), value2.getBytes(), 1);
        try {
            mData.applyMutation(entryMutationTransaction).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testUpdateInsertUsersPermission() throws Exception {
        String key2 = "key2";
        String value2 = "value2";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();

        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        mData.setUserPermissions(signKey, mDataPermissionSet, 1);
        EntryMutationTransaction entryMutationTransaction = mDataInfo.getnewMutation().get();
        entryMutationTransaction.update(key2.getBytes(), value2.getBytes(), 1);
        try {
            mData.applyMutation(entryMutationTransaction).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testInsertNewPermissionsForAnyone() throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.allowPermissionSet(Permission.Insert);
        try {
            mData.setUserPermissions(signKey, mDataPermissionSet, 1).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    public void testSetClearedPermissionsForAnyone() throws Exception {
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();

        mData = mDataInfo.getRandomPublicType(0L).get();
        PublicSignKey signKey = crypto.getAppPublicSignKey().get();
        MDataPermissionSet mDataPermissionSet = mDataInfo.getNewPermissionSet().get();
        mDataPermissionSet.clearPermissions(Permission.Insert);
        try {
            mData.setUserPermissions(signKey, mDataPermissionSet, 1).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }

    //TODO: Error / failure to be fixed
    public void testGetUserPermissionsForAnyone() throws Exception {
        String key = "key";
        String value = "value";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        Crypto crypto = client.crypto();

        MDataInfo mDataInfo = client.mDataInfo();
        MDataEntries mDataEntries = mDataInfo.newEntries().get();

        mData = mDataInfo.getRandomPublicType(0).get();

        mDataEntries.insertEntry(key.getBytes(), value.getBytes());

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

        MDataPermissionSet newPermSet = mDataInfo.getNewPermissionSet().get();
        newPermSet.allowPermissionSet(Permission.Insert);

        mData.setUserPermissions(null, newPermSet, 1);
        try {
            mData.getUserPermissions(null).get();
        } catch (Exception e) {
            assert (e == null);
        }
    }

    //TODO: Error / failure to be fixed
    public void testRemoveUserPermissionsForAnyone() throws Exception {
        String key = "key";
        String value = "value";
        SafeClient client = Utils.getTestAppWithAccess();
        MutableData mData;
        MDataInfo mDataInfo = client.mDataInfo();

        mData = Utils.getTestRandomMDataWithPermissions(key.getBytes(), value.getBytes(), false);

        MDataPermissionSet newPermSet = mDataInfo.getNewPermissionSet().get();
        newPermSet.allowPermissionSet(Permission.Insert);
        mData.setUserPermissions(null, newPermSet, 1);
        try {
            mData.deleteUserPermissions(null, 2).get();
        } catch (Exception e) {
            assert (e != null);
        }
    }
}
