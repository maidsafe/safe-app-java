package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;

public class IDataTest {
    static {
        SessionLoader.load();
    }

    @Test
    public void plainIDataTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle, client.cipherOpt.getPlainCipherOpt().get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void symmetricIDataTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle, client.cipherOpt.getSymmetricCipherOpt().get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void asymmetricIDataTest() throws Exception {
        NativeHandle nativeHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        Client client = new Client(new AppHandle(nativeHandle.toLong()), new DisconnectListener());
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle publicEncryptKey = client.crypto.getAppPublicEncryptKey().get();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle, client.cipherOpt.getAsymmetricCipherOpt(publicEncryptKey).get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }


}
