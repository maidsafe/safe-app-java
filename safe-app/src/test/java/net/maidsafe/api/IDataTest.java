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

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.test.utils.Helper;
import net.maidsafe.test.utils.SessionLoader;

import org.junit.Assert;
import org.junit.Test;

public class IDataTest {

    static {
        SessionLoader.load();
    }
    static final int LENGTH = 200;


    @Test
    public void plainIDataTest() throws Exception {
        Client client = (Client) TestHelper.createSession().get();
        byte[] data = Helper.randomAlphaNumeric(LENGTH).getBytes();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle,
                client.cipherOpt.getPlainCipherOpt().get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void symmetricIDataTest() throws Exception {
        Client client = (Client) TestHelper.createSession().get();
        byte[] data = Helper.randomAlphaNumeric(LENGTH).getBytes();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle,
                client.cipherOpt.getSymmetricCipherOpt().get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }

    @Test
    public void asymmetricIDataTest() throws Exception {
        Client client = (Client) TestHelper.createSession().get();
        byte[] data = Helper.randomAlphaNumeric(LENGTH).getBytes();
        NativeHandle publicEncryptKey = client.crypto.getAppPublicEncryptKey().get();
        NativeHandle writerHandle = client.iData.getWriter().get();
        client.iData.write(writerHandle, data).get();
        byte[] address = client.iData.close(writerHandle,
                client.cipherOpt.getAsymmetricCipherOpt(publicEncryptKey).get()).get();

        NativeHandle readerHandle = client.iData.getReader(address).get();
        long size = client.iData.getSize(readerHandle).get();
        byte[] readData = client.iData.read(readerHandle, 0, size).get();

        Assert.assertEquals(new String(data), new String(readData));
    }
}
