package net.maidsafe.api;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.AuthResponse;
import net.maidsafe.api.model.DecodeResult;
import net.maidsafe.api.model.Request;
import net.maidsafe.models.IpcRequest;
import net.maidsafe.safe_app.AppExchangeInfo;
import net.maidsafe.safe_app.AuthReq;
import net.maidsafe.safe_app.ContainerPermissions;
import net.maidsafe.safe_app.PermissionSet;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;

import java.util.concurrent.CompletableFuture;


public class TestHelper {

    public static final String APP_ID = "net.maidsafe.api.test";
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
        String auth_response = "AQAAAH17bewAAAAAAAAAACAAAAAAAAAAsuxxqWooR3bXg_5KyFntrqoKcFXL-eYGh8T1FBhvB1kg" +
                "AAAAAAAAAI-aKQohstpP6VF5HKJtJTgLX9fXPtKKWs7QDt1wbcVUIAAAAAAAAADizut2wsxg_SiM02Y4zv8JNdRthUf" +
                "vuWExUTecHPpetkAAAAAAAAAACrO0JSvvP7nTY9fa-eWwe8vrKcjlH64Y11SFVGzzHtrizut2wsxg_SiM02Y4zv8JNd" +
                "RthUfvuWExUTecHPpetiAAAAAAAAAA917xqGf-CJNTYh7jDdefZR_OYZ9wL-KsfrljVEWiIB8gAAAAAAAAAM9jQH_sX" +
                "uvzkeU4PIWoCi9pqBRku5U23Iavx7a-JGJlAAAAAAAAAAAAAAAAAAAAAKp5JVAFuuDUuvdTrLvz7lc-o5LDmWdKKP_l" +
                "iyGCSJldmDoAAAAAAAAYAAAAAAAAAJL6C_iymFWDJrazTisxDk98mlhT-qEEhgIAAAAAAAAABwAAAAAAAABfcHVibGl" +
                "j_5N--AYV7KXcxsEWcdfOKlBM-F3V49dM9kdF3zOt93GYOgAAAAAAAAAABQAAAAAAAAAAAAAAAQAAAAIAAAADAAAABA" +
                "AAABsAAAAAAAAAYXBwcy9uZXQubWFpZHNhZmUudGVzdC5qYXZhWUZuQLgK-4szfCMIL9doHYaX_E-Jkfy-YjnpK4ZDi" +
                "meYOgAAAAAAAAEgAAAAAAAAAFZXOBoNxu1y_1ELn7NEwdFdlbU4j6t25gcHu4aFcIdxGAAAAAAAAACD8L4cbIw6q8ai" +
                "OsPn3lMwF69zRkOmAzAABQAAAAAAAAAAAAAAAQAAAAIAAAADAAAABAAAAA";
        System.out.println(locator + " " + secret + " " + authReq.getApp().getId());
        Authenticator authenticator = Authenticator.createAccount(locator, secret,
                TestHelper.randomAlphaNumeric(5)).get();
        System.out.println("Created Account");
        Request request = Session.encodeAuthReq(authReq).get();
        System.out.println("Encoded AuthReq");
//        IpcRequest ipcRequest = authenticator.decodeIpcMessage(request.getUri()).get();
//        System.out.println("Decoded request");
//        Assert.assertThat(ipcRequest, IsInstanceOf.instanceOf(IpcRequest.AuthIpcRequest.class));
//        IpcRequest.AuthIpcRequest authIpcRequest = (IpcRequest.AuthIpcRequest) ipcRequest;
//        String response = authenticator.encodeAuthResponse(authIpcRequest.authReq, request,
//                true).get();
//        System.out.println("Encoded Response");
        DecodeResult decodeResult = Session.decodeIpcMessage(auth_response).get();
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
