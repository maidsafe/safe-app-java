package net.maidsafe.api;

import java.awt.color.CMMException;
import java.util.concurrent.CompletableFuture;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.DecodeResult;
import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.api.model.Request;
import net.maidsafe.models.IpcRequest;
import net.maidsafe.safe_app.AppExchangeInfo;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.safe_app.ContainerPermissions;
import net.maidsafe.safe_app.NativeBindings;
import net.maidsafe.safe_app.PermissionSet;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;


public class TestHelper {

    public static final String APP_ID = "net.maidsafe.java.test";
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static CompletableFuture<Object> createSession() throws Exception {
        return createSession(APP_ID);
    }

    public static CompletableFuture<Object> createSession(String appId) throws Exception {
        ContainerPermissions[] permissions = new ContainerPermissions[1];
        permissions[0] = new ContainerPermissions("_public", new PermissionSet(true,
                true, false, false, false));
        AuthReq authReq = new AuthReq(new AppExchangeInfo(appId, "",
                TestHelper.randomAlphaNumeric(5), TestHelper.randomAlphaNumeric(5)),
                true, permissions, 1, 0);
        String locator = TestHelper.randomAlphaNumeric(10);
        String secret = TestHelper.randomAlphaNumeric(10);
        return createSession(locator, secret, authReq);
    }

    public static CompletableFuture<Object> createSession(AuthReq authReq) throws Exception {
        String locator = TestHelper.randomAlphaNumeric(10);
        String secret = TestHelper.randomAlphaNumeric(10);
        return createSession(locator, secret, authReq);
    }

    public static CompletableFuture<Object> createSession(String locator, String secret, AuthReq authReq)
            throws Exception {
        System.out.println(locator + " " + secret + " " + authReq.getApp().getId());
        Authenticator authenticator = Authenticator.createAccount(locator, secret,
                TestHelper.randomAlphaNumeric(5)).get();
        System.out.println("Created Account");
        Request request = Session.encodeAuthReq(authReq).get();
        System.out.println("Encoded AuthReq");
        IpcRequest ipcRequest = authenticator.decodeIpcMessage(request.getUri()).get();
        System.out.println("Decoded request");
        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(IpcRequest.AuthIpcRequest.class));
        IpcRequest.AuthIpcRequest authIpcRequest = (IpcRequest.AuthIpcRequest) ipcRequest;
        String response = authenticator.encodeAuthResponse(authIpcRequest.authReq, request,
                true).get();
        System.out.println("Encoded Response");
        DecodeResult decodeResult = Session.decodeIpcMessage(response).get();
        System.out.println("Decoded Response");
        Assert.assertThat(decodeResult, CoreMatchers.instanceOf(AuthResponse.class));
        AuthResponse authResponse = (AuthResponse) decodeResult;
        System.out.println("Decode result as AuthResponse");
        Assert.assertNotNull(authResponse);
        System.out.println("Connecting");
        return Session.connect(new App(authReq.getApp().getId(), authReq.getApp().getScope(),
                authReq.getApp().getName(), authReq.getApp().getVendor()), authResponse.getAuthGranted());
    }
}
