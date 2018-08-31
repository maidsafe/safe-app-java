package net.maidsafe.api;

import net.maidsafe.utils.OSInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class Client extends Session {

    public static void load() {
        clientTypeFactory = ClientTypeFactory.load(Client.class);
        try {
            String baseLibName = "libsafe_app";
            String libName = "libsafe_app_jni";
            String baseAuthLibName = "libsafe_authenticator";
            String authLibName = "libsafe_authenticator_jni";
            String extension = ".so";
            switch (OSInfo.getOs()) {
                case WINDOWS:
                    libName = "safe_app_jni";
                    baseLibName = "safe_app";
                    authLibName = "safe_authenticator_jni";
                    baseAuthLibName = "safe_authenticator";
                    extension = ".dll";
                    break;
                case MAC:
                    extension = ".dylib";
                    break;
                default:
                    break;
            }
            String tempDir = System.getProperty("java.io.tmpdir");
            File generatedDir = new File(tempDir, "safe_app_java" + System.nanoTime());
            if (!generatedDir.mkdir()) {
                throw new IOException("Failed to create temp directory " + generatedDir.getName());
            }
            generatedDir.deleteOnExit();
            System.setProperty("java.library.path", generatedDir.getAbsolutePath() + File.pathSeparator
                    + System.getProperty("java.library.path"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);

            File file = new File(generatedDir, baseLibName.concat(extension));
            file.deleteOnExit();
            InputStream inputStream = Client.class.getResourceAsStream("/native/"
                    .concat(baseLibName).concat(extension));
            Files.copy(inputStream, file.toPath());

            file = new File(generatedDir, libName.concat(extension));
            file.deleteOnExit();
            inputStream = Client.class.getResourceAsStream("/native/"
                    .concat(libName).concat(extension));
            Files.copy(inputStream, file.toPath());
            System.loadLibrary("safe_app_jni");

            if(Session.isMock()) {

                file = new File(generatedDir, authLibName.concat(extension));
                file.deleteOnExit();
                inputStream = Client.class.getResourceAsStream("/native/"
                        .concat(authLibName).concat(extension));
                Files.copy(inputStream, file.toPath());

                file = new File(generatedDir, baseAuthLibName.concat(extension));
                file.deleteOnExit();
                inputStream = Client.class.getResourceAsStream("/native/"
                        .concat(baseAuthLibName).concat(extension));
                Files.copy(inputStream, file.toPath());
                System.loadLibrary("safe_authenticator_jni");
            }
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    protected Client(AppHandle appHandle, DisconnectListener disconnectListener) {
        super(appHandle, disconnectListener);
    }
}
