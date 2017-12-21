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
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle writerHandle = IData.getWriter().get();
        IData.write(writerHandle, data).get();
        byte[] address = IData.close(writerHandle, CipherOpt.getPlainCipherOpt().get()).get();

        NativeHandle readerHandle = IData.getReader(address).get();
        long size = IData.getSize(readerHandle).get();
        byte[] readData = IData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void symmetricIDataTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle writerHandle = IData.getWriter().get();
        IData.write(writerHandle, data).get();
        byte[] address = IData.close(writerHandle, CipherOpt.getSymmetricCipherOpt().get()).get();

        NativeHandle readerHandle = IData.getReader(address).get();
        long size = IData.getSize(readerHandle).get();
        byte[] readData = IData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void asymmetricIDataTest() throws Exception {
        Session.appHandle = TestHelper.createTestApp(Helper.APP_ID).get();
        byte[] data = Helper.randomAlphaNumeric(200).getBytes();
        NativeHandle publicEncryptKey = Crypto.getAppPublicEncryptKey().get();
        NativeHandle writerHandle = IData.getWriter().get();
        IData.write(writerHandle, data).get();
        byte[] address = IData.close(writerHandle, CipherOpt.getAsymmetricCipherOpt(publicEncryptKey).get()).get();

        NativeHandle readerHandle = IData.getReader(address).get();
        long size = IData.getSize(readerHandle).get();
        byte[] readData = IData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }


}
