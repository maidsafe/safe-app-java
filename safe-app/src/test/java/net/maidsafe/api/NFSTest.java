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

    private MDataInfo getPublicMData() throws Exception {
        MDataInfo mDataInfo = MData.getRandomPublicMData(16000).get();
        NativeHandle permissionHandle = MDataPermission.newPermissionHandle().get();
        PermissionSet permissionSet = new PermissionSet();
        permissionSet.setRead(true);
        permissionSet.setInsert(true);
        permissionSet.setUpdate(true);
        permissionSet.setDelete(true);
        MDataPermission.insert(permissionHandle, Crypto.getAppPublicSignKey().get(), permissionSet).get();
        MData.put(mDataInfo, permissionHandle, MDataEntries.newEntriesHandle().get()).get();
        return mDataInfo;
    }

    @Test
    public void fileCRUDTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        MDataInfo mDataInfo = getPublicMData();
        File file = new File();
        NativeHandle fileHandle = NFS.fileOpen(mDataInfo, file, NFS.OpenMode.OVER_WRITE).get();
        byte[] fileContent = Helper.randomAlphaNumeric(20).getBytes();
        NFS.fileWrite(fileHandle, fileContent).get();
        file = NFS.fileClose(fileHandle).get();
        NFS.insertFile(mDataInfo, "sample.txt", file);
        fileHandle = NFS.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
        byte[] readData = NFS.fileRead(fileHandle, 0, 0).get();
        Assert.assertEquals(fileContent, readData);
        fileHandle = NFS.fileOpen(mDataInfo, file, NFS.OpenMode.APPEND).get();
        byte[] appendedContent = Helper.randomAlphaNumeric(10).getBytes();
        NFS.fileWrite(fileHandle, Helper.randomAlphaNumeric(10).getBytes()).get();
        file = NFS.fileClose(fileHandle).get();
        NFSFileMetadata fileMetadata = NFS.getFileMetadata(mDataInfo, "sample.txt").get();
        NFS.updateFile(mDataInfo, "sample.txt", file, fileMetadata.getVersion() + 1).get();
        fileHandle = NFS.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
        long fileSize = NFS.getSize(fileHandle).get();
        Assert.assertEquals(fileContent.length + appendedContent.length, fileSize);
        readData = NFS.fileRead(fileHandle, 0, 0).get();
        String newContent = new StringBuilder().append(fileContent).append(appendedContent).toString();
        Assert.assertEquals(newContent, readData);
        fileMetadata = NFS.getFileMetadata(mDataInfo, "sample.txt").get();
        NFS.deleteFile(mDataInfo, "sample.txt", file, fileMetadata.getVersion() + 1).get();
    }
}
