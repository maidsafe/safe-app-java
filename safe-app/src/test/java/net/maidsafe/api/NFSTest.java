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

import net.maidsafe.api.model.NFSFileMetadata;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.File;
import net.maidsafe.safe_app.MDataInfo;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;

public class NFSTest {
    static {
        SessionLoader.load();
    }
    static final int TYPE_TAG = 16000;
    static final int LENGTH = 20;


    private MDataInfo getPublicMData(final Client client) throws Exception {
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setRead(true);
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setDelete(true);
        MDataInfo mDataInfo = client.mData.getRandomPublicMData(TYPE_TAG).get();
        NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
        client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(),
                permissionSet).get();
        client.mData.put(mDataInfo, permissionHandle, client.mDataEntries.newEntriesHandle().get())
                .get();
        return mDataInfo;
    }

    @Test
    public void fileCRUDTest() throws Exception {
        Client client = (Client) TestHelper.createSession().get();
        MDataInfo mDataInfo = getPublicMData(client);
        File file = new File();
        NativeHandle fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.OVER_WRITE).get();
        byte[] fileContent = Helper.randomAlphaNumeric(LENGTH).getBytes();
        client.nfs.fileWrite(fileHandle, fileContent).get();
        file = client.nfs.fileClose(fileHandle).get();
        client.nfs.insertFile(mDataInfo, "sample.txt", file);
        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
        byte[] readData = client.nfs.fileRead(fileHandle, 0, 0).get();
        Assert.assertEquals(new String(fileContent), new String(readData));
        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.APPEND).get();
        byte[] appendedContent = Helper.randomAlphaNumeric(LENGTH).getBytes();
        client.nfs.fileWrite(fileHandle, appendedContent).get();
        file = client.nfs.fileClose(fileHandle).get();
        NFSFileMetadata fileMetadata = client.nfs.getFileMetadata(mDataInfo,
                "sample.txt").get();
        client.nfs.updateFile(mDataInfo, "sample.txt", file,
                fileMetadata.getVersion() + 1).get();
        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
        long fileSize = client.nfs.getSize(fileHandle).get();
        Assert.assertEquals(fileContent.length + appendedContent.length, fileSize);
        readData = client.nfs.fileRead(fileHandle, 0, 0).get();
        String newContent = new String(fileContent).concat(new String(appendedContent));
        Assert.assertEquals(newContent, new String(readData));
        fileMetadata = client.nfs.getFileMetadata(mDataInfo, "sample.txt").get();
        client.nfs.deleteFile(mDataInfo, "sample.txt",
                fileMetadata.getVersion() + 1).get();
    }
}
