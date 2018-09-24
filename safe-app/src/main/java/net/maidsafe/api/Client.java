package net.maidsafe.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class Client extends Session {

    public static void load() {
        clientTypeFactory = ClientTypeFactory.load(Client.class);
        try {
            String baseLibName = System.mapLibraryName("safe_app");
            String libName = System.mapLibraryName("safe_app_jni");
            String baseAuthLibName = System.mapLibraryName("safe_authenticator");
            String authLibName = System.mapLibraryName("safe_authenticator_jni");
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

            File file = new File(generatedDir, baseLibName);
            file.deleteOnExit();
            InputStream inputStream = Client.class.getResourceAsStream("/native/"
                    .concat(baseLibName));
            Files.copy(inputStream, file.toPath());

            file = new File(generatedDir, libName);
            file.deleteOnExit();
            inputStream = Client.class.getResourceAsStream("/native/"
                    .concat(libName));
            Files.copy(inputStream, file.toPath());
            System.loadLibrary("safe_app_jni");

            if(Session.isMock()) {

                file = new File(generatedDir, authLibName);
                file.deleteOnExit();
                inputStream = Client.class.getResourceAsStream("/native/"
                        .concat(authLibName));
                Files.copy(inputStream, file.toPath());

                file = new File(generatedDir, baseAuthLibName);
                file.deleteOnExit();
                inputStream = Client.class.getResourceAsStream("/native/"
                        .concat(baseAuthLibName));
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
