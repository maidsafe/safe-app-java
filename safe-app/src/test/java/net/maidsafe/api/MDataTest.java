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

  private void publicMDataCrud(MDataInfo mDataInfo) throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    PermissionSet permissionSet = new PermissionSet();
    permissionSet.setInsert(true);
    permissionSet.setUpdate(true);
    permissionSet.setRead(true);
    permissionSet.setDelete(true);
    permissionSet.setManagePermission(true);
    NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
    client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(),
        permissionSet).get();
    NativeHandle entriesHandle = client.mDataEntries.newEntriesHandle().get();
    client.mDataEntries.insert(entriesHandle, "someKey".getBytes(), "someValue".getBytes()).get();
    client.mData.put(mDataInfo, permissionHandle, entriesHandle).get();


    NativeHandle actionHandle = client.mDataEntryAction.newEntryAction().get();
    client.mDataEntryAction.insert(actionHandle, "someKey2".getBytes(),
        "someValue2".getBytes()).get();
    client.mData.mutateEntries(mDataInfo, actionHandle).get();

    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    List<MDataEntry> entries = client.mDataEntries.listEntries(entriesHandle).get();
    Assert.assertEquals(2, entries.size());

    // Update
    actionHandle = client.mDataEntryAction.newEntryAction().get();
    MDataEntry entry = entries.get(0);
    byte[] updatedValue = Helper.randomAlphaNumeric(10).getBytes();
    client.mDataEntryAction.update(actionHandle, entry.getKey().getVal(), updatedValue,
        entry.getValue().getEntryVersion() + 1).get();
    client.mData.mutateEntries(mDataInfo, actionHandle).get();
    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    entries = client.mDataEntries.listEntries(entriesHandle).get();

    entry = entries.get(0);
    MDataValue value = client.mData.getValue(mDataInfo, entry.getKey().getVal()).get();
    Assert.assertEquals(new String(updatedValue), new String(value.getContent()));
    // Delete
    actionHandle = client.mDataEntryAction.newEntryAction().get();
    client.mDataEntryAction.delete(actionHandle, entry.getKey().getVal(),
        entry.getValue().getEntryVersion() + 1).get();
    client.mData.mutateEntries(mDataInfo, actionHandle).get();

    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    entries = client.mDataEntries.listEntries(entriesHandle).get();


    Assert.assertEquals(entries.get(0).getValue().getContentLen(), 0);

    byte[] serialisedMData = client.mData.serialise(mDataInfo).get();
    mDataInfo = client.mData.deserialise(serialisedMData).get();

    List<MDataKey> keys = client.mData.getKeys(mDataInfo).get();
    List<MDataValue> values = client.mData.getValues(mDataInfo).get();
    Assert.assertEquals(keys.size(), values.size());
    Assert.assertEquals(new String(entries.get(0).getKey().getVal()),
        new String(keys.get(0).getVal()));
    Assert.assertEquals(new String(entries.get(0).getValue().getContent()),
        new String(values.get(0).getContent()));

    PermissionSet permissions = client.mData.getPermissionForUser(
        client.crypto.getAppPublicSignKey().get(), mDataInfo).get();
    Assert.assertTrue(permissions.getInsert());
    Assert.assertTrue(permissions.getUpdate());
    Assert.assertTrue(permissions.getRead());
    Assert.assertTrue(permissions.getDelete());
    Assert.assertTrue(permissions.getManagePermission());

    permissionSet.setInsert(false);
    permissionSet.setUpdate(false);
    permissionSet.setRead(false);
    permissionSet.setDelete(false);
    permissionHandle = client.mDataPermission.newPermissionHandle().get();
    client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(),
        permissionSet).get();
    client.mData.setUserPermission(client.crypto.getAppPublicSignKey().get(), mDataInfo,
        permissionSet, client.mData.getVersion(mDataInfo).get() + 1).get();
    long version = client.mData.getVersion(mDataInfo).get();
    Assert.assertEquals(1, version);
  }

  private void privateMDataCrud(MDataInfo mDataInfo) throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    PermissionSet permissionSet = new PermissionSet();
    permissionSet.setInsert(true);
    permissionSet.setUpdate(true);
    permissionSet.setRead(true);
    permissionSet.setDelete(true);
    NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
    client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(),
        permissionSet).get();
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
    byte[] encryptedUpdatedValue = client.mData.encryptEntryValue(mDataInfo, updatedValue).get();
    client.mDataEntryAction.update(actionHandle, entry.getKey().getVal(), encryptedUpdatedValue,
        entry.getValue().getEntryVersion() + 1).get();
    client.mData.mutateEntries(mDataInfo, actionHandle).get();

    MDataValue entryValue = client.mData.getValue(mDataInfo, entry.getKey().getVal()).get();
    byte[] decryptedValue = client.mData.decrypt(mDataInfo, entryValue.getContent()).get();
    Assert.assertEquals(new String(updatedValue), new String(decryptedValue));
    // Delete
    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    entries = client.mDataEntries.listEntries(entriesHandle).get();

    entry = entries.get(0);
    actionHandle = client.mDataEntryAction.newEntryAction().get();
    client.mDataEntryAction.delete(actionHandle, entry.getKey().getVal(),
        entry.getValue().getEntryVersion() + 1).get();
    client.mData.mutateEntries(mDataInfo, actionHandle).get();

    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    entries = client.mDataEntries.listEntries(entriesHandle).get();
    Assert.assertEquals(0, entries.get(0).getValue().getContentLen());

    byte[] serialisedMData = client.mData.serialise(mDataInfo).get();
    mDataInfo = client.mData.deserialise(serialisedMData).get();
    entriesHandle = client.mData.getEntriesHandle(mDataInfo).get();
    entries = client.mDataEntries.listEntries(entriesHandle).get();
    Assert.assertEquals(2, entries.size());
  }

  @Test
  public void randomPublicMDataCRUDTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    long tagType = 15001;
    MDataInfo mDataInfo = client.mData.getRandomPublicMData(tagType).get();
    publicMDataCrud(mDataInfo);
  }

  @Test
  public void publicMDataCRUDTest() throws Exception {
    long tagType = 15001;
    MDataInfo mDataInfo = new MDataInfo();
    mDataInfo.setName(Helper.randomAlphaNumeric(Constants.XOR_NAME_LENGTH).getBytes());
    mDataInfo.setTypeTag(tagType);
    publicMDataCrud(mDataInfo);
  }

  @Test
  public void randomPrivateMDataCRUDTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    long tagType = 15001;
    MDataInfo mDataInfo = client.mData.getRandomPrivateMData(tagType).get();
    privateMDataCrud(mDataInfo);
  }

  @Test
  public void privateMDataCRUDTest() throws Exception {
    Client client = (Client)TestHelper.createSession().get();
    long tagType = 15001;
    EncryptKeyPair encryptKeyPair = client.crypto.generateEncryptKeyPair().get();
    byte[] nonce = client.crypto.generateNonce().get();
    byte[] secretKey = client.crypto.getRawSecretEncryptKey(encryptKeyPair.getSecretEncryptKey())
        .get();
    MDataInfo mDataInfo = client.mData.getPrivateMData(Helper.randomAlphaNumeric(
        Constants.XOR_NAME_LENGTH).getBytes(), tagType, secretKey, nonce).get();
    privateMDataCrud(mDataInfo);
  }
}
