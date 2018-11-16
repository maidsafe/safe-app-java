// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api;

import java.util.List;

import net.maidsafe.api.model.EncryptKeyPair;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.MDataEntry;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.MDataKey;
import net.maidsafe.safe_app.MDataValue;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;
import net.maidsafe.utils.Constants;

import org.junit.Assert;
import org.junit.Test;


public class MDataTest {
    static {
        SessionLoader.load();
    }

    static final int LENGTH = 10;
    static final int TYPE_TAG = 15001;


    private void publicMDataCrud(final MDataInfo mDataInfo) throws Exception {
        Session session = TestHelper.createSession();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setRead(true);
        permissionSet.setDelete(true);
        permissionSet.setManagePermission(true);
        NativeHandle permissionHandle = session.mDataPermission.newPermissionHandle().get();
        session.mDataPermission.insert(permissionHandle, session.crypto.getAppPublicSignKey().get(),
                permissionSet).get();
        NativeHandle entriesHandle = session.mDataEntries.newEntriesHandle().get();
        session.mDataEntries.insert(entriesHandle, "someKey".getBytes(), "someValue".getBytes()).get();
        session.mData.put(mDataInfo, permissionHandle, entriesHandle).get();


        NativeHandle actionHandle = session.mDataEntryAction.newEntryAction().get();
        session.mDataEntryAction.insert(actionHandle, "someKey2".getBytes(),
                "someValue2".getBytes()).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        List<MDataEntry> entries = session.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(2, entries.size());

        // Update
        actionHandle = session.mDataEntryAction.newEntryAction().get();
        MDataEntry entry = entries.get(0);
        byte[] updatedValue = Helper.randomAlphaNumeric(LENGTH).getBytes();
        session.mDataEntryAction.update(actionHandle, entry.getKey().getKey(), updatedValue,
                entry.getValue().getEntryVersion() + 1).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();
        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        entries = session.mDataEntries.listEntries(entriesHandle).get();

        entry = entries.get(0);
        MDataValue value = session.mData.getValue(mDataInfo, entry.getKey().getKey()).get();
        Assert.assertEquals(new String(updatedValue), new String(value.getContent()));
        // Delete
        actionHandle = session.mDataEntryAction.newEntryAction().get();
        session.mDataEntryAction.delete(actionHandle, entry.getKey().getKey(),
                entry.getValue().getEntryVersion() + 1).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        entries = session.mDataEntries.listEntries(entriesHandle).get();


        Assert.assertEquals(entries.get(0).getValue().getContentLen(), 0);

        byte[] serialisedMData = session.mData.serialise(mDataInfo).get();
        final MDataInfo mdInfo = session.mData.deserialise(serialisedMData).get();

        List<MDataKey> keys = session.mData.getKeys(mdInfo).get();
        List<MDataValue> values = session.mData.getValues(mdInfo).get();
        Assert.assertEquals(keys.size(), values.size());
        Assert.assertEquals(new String(entries.get(0).getKey().getKey()),
                new String(keys.get(0).getKey()));
        Assert.assertEquals(new String(entries.get(0).getValue().getContent()),
                new String(values.get(0).getContent()));

        PermissionSet permissions = session.mData.getPermissionForUser(
                session.crypto.getAppPublicSignKey().get(), mdInfo).get();
        Assert.assertTrue(permissions.getInsert());
        Assert.assertTrue(permissions.getUpdate());
        Assert.assertTrue(permissions.getRead());
        Assert.assertTrue(permissions.getDelete());
        Assert.assertTrue(permissions.getManagePermission());

        permissionSet.setInsert(false);
        permissionSet.setUpdate(false);
        permissionSet.setRead(false);
        permissionSet.setDelete(false);
        permissionHandle = session.mDataPermission.newPermissionHandle().get();
        session.mDataPermission.insert(permissionHandle, session.crypto.getAppPublicSignKey().get(),
                permissionSet).get();
        session.mData.setUserPermission(session.crypto.getAppPublicSignKey().get(), mdInfo,
                permissionSet, session.mData.getVersion(mdInfo).get() + 1).get();
        long version = session.mData.getVersion(mdInfo).get();
        Assert.assertEquals(1, version);
    }

    private void privateMDataCrud(final MDataInfo mDataInfo) throws Exception {
        Session session = TestHelper.createSession();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setRead(true);
        permissionSet.setDelete(true);
        NativeHandle permissionHandle = session.mDataPermission.newPermissionHandle().get();
        session.mDataPermission.insert(permissionHandle, session.crypto.getAppPublicSignKey().get(),
                permissionSet).get();
        NativeHandle entriesHandle = session.mDataEntries.newEntriesHandle().get();
        byte[] key = session.mData.encryptEntryKey(mDataInfo, "someKey".getBytes()).get();
        byte[] value = session.mData.encryptEntryValue(mDataInfo, "someValue".getBytes()).get();
        session.mDataEntries.insert(entriesHandle, key, value).get();
        session.mData.put(mDataInfo, permissionHandle, entriesHandle).get();

        NativeHandle actionHandle = session.mDataEntryAction.newEntryAction().get();
        key = session.mData.encryptEntryKey(mDataInfo, "someKey2".getBytes()).get();
        value = session.mData.encryptEntryValue(mDataInfo, "someValue2".getBytes()).get();
        session.mDataEntryAction.insert(actionHandle, key, value).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        List<MDataEntry> entries = session.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(2, entries.size());
        // Update
        actionHandle = session.mDataEntryAction.newEntryAction().get();
        MDataEntry entry = entries.get(0);
        byte[] updatedValue = Helper.randomAlphaNumeric(LENGTH).getBytes();
        byte[] encryptedUpdatedValue = session.mData.encryptEntryValue(mDataInfo, updatedValue).get();
        session.mDataEntryAction.update(actionHandle, entry.getKey().getKey(), encryptedUpdatedValue,
                entry.getValue().getEntryVersion() + 1).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();

        MDataValue entryValue = session.mData.getValue(mDataInfo, entry.getKey().getKey()).get();
        byte[] decryptedValue = session.mData.decrypt(mDataInfo, entryValue.getContent()).get();
        Assert.assertEquals(new String(updatedValue), new String(decryptedValue));
        // Delete
        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        entries = session.mDataEntries.listEntries(entriesHandle).get();

        entry = entries.get(0);
        actionHandle = session.mDataEntryAction.newEntryAction().get();
        session.mDataEntryAction.delete(actionHandle, entry.getKey().getKey(),
                entry.getValue().getEntryVersion() + 1).get();
        session.mData.mutateEntries(mDataInfo, actionHandle).get();

        entriesHandle = session.mData.getEntriesHandle(mDataInfo).get();
        entries = session.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(0, entries.get(0).getValue().getContentLen());

        byte[] serialisedMData = session.mData.serialise(mDataInfo).get();
        final MDataInfo mdInfo = session.mData.deserialise(serialisedMData).get();
        entriesHandle = session.mData.getEntriesHandle(mdInfo).get();
        entries = session.mDataEntries.listEntries(entriesHandle).get();
        Assert.assertEquals(2, entries.size());
    }

    @Test
    public void randomPublicMDataCRUDTest() throws Exception {
        Session session = TestHelper.createSession();
        long tagType = TYPE_TAG;
        MDataInfo mDataInfo = session.mData.getRandomPublicMData(tagType).get();
        publicMDataCrud(mDataInfo);
    }

    @Test
    public void publicMDataCRUDTest() throws Exception {
        long tagType = TYPE_TAG;
        MDataInfo mDataInfo = new MDataInfo();
        mDataInfo.setName(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes());
        mDataInfo.setTypeTag(tagType);
        publicMDataCrud(mDataInfo);
    }

    @Test
    public void randomPrivateMDataCRUDTest() throws Exception {
        Session session = TestHelper.createSession();
        long tagType = TYPE_TAG;
        MDataInfo mDataInfo = session.mData.getRandomPrivateMData(tagType).get();
        privateMDataCrud(mDataInfo);
    }

    @Test
    public void privateMDataCRUDTest() throws Exception {
        Session session = TestHelper.createSession();
        long tagType = TYPE_TAG;
        EncryptKeyPair encryptKeyPair = session.crypto.generateEncryptKeyPair().get();
        byte[] nonce = session.crypto.generateNonce().get();
        byte[] secretKey = session.crypto.getRawSecretEncryptKey(encryptKeyPair.getSecretEncryptKey())
                .get();
        MDataInfo mDataInfo = session.mData.getPrivateMData(Helper.randomAlphaNumeric(
                Constants.XOR_NAME_LENGTH).getBytes(), tagType, secretKey, nonce).get();
        privateMDataCrud(mDataInfo);
    }
}
