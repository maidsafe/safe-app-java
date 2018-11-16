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

import net.maidsafe.api.model.AuthIpcRequest;
import net.maidsafe.api.model.DecodeResult;
import net.maidsafe.api.model.IpcRequest;
import net.maidsafe.api.model.Request;
import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.UnregisteredIpcRequest;
import net.maidsafe.api.model.UnregisteredClientResponse;
import net.maidsafe.safe_app.AppExchangeInfo;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.safe_app.ContainerPermissions;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.test.utils.Helper;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;


public final class TestHelper {

    private TestHelper() {
        // Constructor intentionally empty
    }
    public static final String APP_ID = "net.maidsafe.java.test";
    public static final int LENGTH = 10;

    public static Session createSession() throws Exception {
        return createSession(APP_ID);
    }

    public static Session createSession(final String appId) throws Exception {
        ContainerPermissions[] permissions = new ContainerPermissions[1];
        permissions[0] = new ContainerPermissions("_public", new PermissionSet(true,
                true, false, false, false));
        AuthReq authReq = new AuthReq(new AppExchangeInfo(appId, "",
                Helper.randomAlphaNumeric(LENGTH), Helper.randomAlphaNumeric(LENGTH)),
                true, permissions, 1, 0);
        String locator = Helper.randomAlphaNumeric(LENGTH);
        String secret = Helper.randomAlphaNumeric(LENGTH);
        return createSession(locator, secret, authReq);
    }

    public static Session createSession(final AuthReq authReq) throws Exception {
        String locator = Helper.randomAlphaNumeric(LENGTH);
        String secret = Helper.randomAlphaNumeric(LENGTH);
        return createSession(locator, secret, authReq);
    }

    public static Session createSession(final String locator,
                                                          final String secret, final AuthReq authReq)
            throws Exception {
        Authenticator authenticator = Authenticator.createAccount(locator, secret,
                Helper.randomAlphaNumeric(LENGTH)).get();
        Request request = Session.encodeAuthReq(authReq).get();
        IpcRequest ipcRequest = authenticator.decodeIpcMessage(request.getUri()).get();
        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(AuthIpcRequest.class));
        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(AuthIpcRequest.class));
        AuthIpcRequest authIpcRequest = (AuthIpcRequest) ipcRequest;
        String response = authenticator.encodeAuthResponse(authIpcRequest,
                true).get();
        DecodeResult decodeResult = Session.decodeIpcMessage(response).get();
        Assert.assertThat(decodeResult, CoreMatchers.instanceOf(AuthResponse.class));
        AuthResponse authResponse = (AuthResponse) decodeResult;
        Assert.assertNotNull(authResponse);
        return Session.connect(authReq.getApp().getId(), authResponse.getAuthGranted()).get();
    }

    public static Session createUnregisteredSession() throws Exception {
        Request request = Client.getUnregisteredSessionRequest(APP_ID).get();
        Assert.assertTrue(request.getReqId() != 0);
        Assert.assertNotNull(request.getUri());
        IpcRequest unregisteredIpcRequest = Authenticator.unregisteredDecodeIpcMessage(request.getUri()).get();
        Assert.assertThat(unregisteredIpcRequest, IsInstanceOf.instanceOf(UnregisteredIpcRequest.class));
        String response = Authenticator.encodeUnregisteredResponse(unregisteredIpcRequest, true).get();
        DecodeResult result = Session.decodeIpcMessage(response).get();
        Assert.assertThat(result, IsInstanceOf.instanceOf(UnregisteredClientResponse.class));
        return Session.connect((UnregisteredClientResponse) result).get();
    }
}
