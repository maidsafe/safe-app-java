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

import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.DecodeResult;
import net.maidsafe.api.model.Request;
import net.maidsafe.api.model.*;
import net.maidsafe.safe_app.AppExchangeInfo;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.safe_app.ContainerPermissions;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.test.utils.Helper;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;


public class TestHelper {

    public static final String APP_ID = "net.maidsafe.java.test";

    public static CompletableFuture<Object> createSession() throws Exception {
        return createSession(APP_ID);
    }

    public static CompletableFuture<Object> createSession(String appId) throws Exception {
        ContainerPermissions[] permissions = new ContainerPermissions[1];
        permissions[0] = new ContainerPermissions("_public", new PermissionSet(true,
                true, false, false, false));
        AuthReq authReq = new AuthReq(new AppExchangeInfo(appId, "",
                Helper.randomAlphaNumeric(5), Helper.randomAlphaNumeric(5)),
                true, permissions, 1, 0);
        String locator = Helper.randomAlphaNumeric(10);
        String secret = Helper.randomAlphaNumeric(10);
        return createSession(locator, secret, authReq);
    }

    public static CompletableFuture<Object> createSession(AuthReq authReq) throws Exception {
        String locator = Helper.randomAlphaNumeric(10);
        String secret = Helper.randomAlphaNumeric(10);
        return createSession(locator, secret, authReq);
    }

    public static CompletableFuture<Object> createSession(String locator, String secret, AuthReq authReq)
            throws Exception {
        System.out.println(locator + " " + secret + " " + authReq.getApp().getId());
        Authenticator authenticator = Authenticator.createAccount(locator, secret,
                Helper.randomAlphaNumeric(5)).get();
        System.out.println("Created Account");
        Request request = Session.encodeAuthReq(authReq).get();
        System.out.println("Encoded AuthReq");
        IpcRequest ipcRequest = authenticator.decodeIpcMessage(request.getUri()).get();
        System.out.println("Decoded request");
        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(AuthIpcRequest.class));
        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(AuthIpcRequest.class));
        AuthIpcRequest authIpcRequest = (AuthIpcRequest) ipcRequest;
        String response = authenticator.encodeAuthResponse(authIpcRequest,
                true).get();
        System.out.println("Encoded Response");
        DecodeResult decodeResult = Session.decodeIpcMessage(response).get();
        System.out.println("Decoded Response");
        Assert.assertThat(decodeResult, CoreMatchers.instanceOf(AuthResponse.class));
        AuthResponse authResponse = (AuthResponse) decodeResult;
        System.out.println("Decode result as AuthResponse");
        Assert.assertNotNull(authResponse);
        System.out.println("Connecting");
        return Session.connect(authReq.getApp().getId(), authResponse.getAuthGranted());
    }
}
