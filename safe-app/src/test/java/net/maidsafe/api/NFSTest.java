//package net.maidsafe.api;
//
//import net.maidsafe.api.model.NFSFileMetadata;
//import net.maidsafe.api.model.NativeHandle;
//import net.maidsafe.safe_app.File;
//import net.maidsafe.safe_app.MDataInfo;
//import net.maidsafe.safe_app.PermissionSet;
//import net.maidsafe.test.utils.Helper;
//import net.maidsafe.test.utils.SessionLoader;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//public class NFSTest {
//
//    static {
//        SessionLoader.load();
//    }
//
//    private MDataInfo getPublicMData() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
//        MDataInfo mDataInfo = client.mData.getRandomPublicMData(16000).get();
//        NativeHandle permissionHandle = client.mDataPermission.newPermissionHandle().get();
//        PermissionSet permissionSet = new PermissionSet();
//        permissionSet.setRead(true);
//        permissionSet.setInsert(true);
//        permissionSet.setUpdate(true);
//        permissionSet.setDelete(true);
//        client.mDataPermission.insert(permissionHandle, client.crypto.getAppPublicSignKey().get(), permissionSet).get();
//        client.mData.put(mDataInfo, permissionHandle, client.mDataEntries.newEntriesHandle().get()).get();
//        return mDataInfo;
//    }
//
//    @Test
//    public void fileCRUDTest() throws Exception {
//        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
//        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
//        MDataInfo mDataInfo = getPublicMData();
//        File file = new File();
//        NativeHandle fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.OVER_WRITE).get();
//        byte[] fileContent = Helper.randomAlphaNumeric(20).getBytes();
//        client.nfs.fileWrite(fileHandle, fileContent).get();
//        file = client.nfs.fileClose(fileHandle).get();
//        client.nfs.insertFile(mDataInfo, "sample.txt", file);
//        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
//        byte[] readData = client.nfs.fileRead(fileHandle, 0, 0).get();
//        Assert.assertEquals(fileContent, readData);
//        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.APPEND).get();
//        byte[] appendedContent = Helper.randomAlphaNumeric(10).getBytes();
//        client.nfs.fileWrite(fileHandle, Helper.randomAlphaNumeric(10).getBytes()).get();
//        file = client.nfs.fileClose(fileHandle).get();
//        NFSFileMetadata fileMetadata = client.nfs.getFileMetadata(mDataInfo, "sample.txt").get();
//        client.nfs.updateFile(mDataInfo, "sample.txt", file, fileMetadata.getVersion() + 1).get();
//        fileHandle = client.nfs.fileOpen(mDataInfo, file, NFS.OpenMode.READ).get();
//        long fileSize = client.nfs.getSize(fileHandle).get();
//        Assert.assertEquals(fileContent.length + appendedContent.length, fileSize);
//        readData = client.nfs.fileRead(fileHandle, 0, 0).get();
//        String newContent = new StringBuilder().append(fileContent).append(appendedContent).toString();
//        Assert.assertEquals(newContent, readData);
//        fileMetadata = client.nfs.getFileMetadata(mDataInfo, "sample.txt").get();
//        client.nfs.deleteFile(mDataInfo, "sample.txt", file, fileMetadata.getVersion() + 1).get();
//    }
//}
