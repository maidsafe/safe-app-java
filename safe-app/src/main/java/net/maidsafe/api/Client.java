package net.maidsafe.api;

import net.maidsafe.utils.OSInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Client extends Session {

    public static void load() {
        try {
            String baseLibName = "libsafe_app";
            String libName = "libsafe_app_jni";
            String extension = ".so";
            switch (OSInfo.getOs()) {
                case WINDOWS:
                    libName = "safe_app_jni";
                    baseLibName = "safe_app";
                    extension = ".dll";
                    break;
                case MAC:
                    extension = ".dylib";
                    break;
                default:
                    break;
            }
            String tempDir = System.getProperty("java.temp.io");
            File generatedDir = new File(tempDir, "safe_app_java" + System.nanoTime());
            if (!generatedDir.mkdir()) {
                throw new IOException("Failed to create temp directory " + generatedDir.getName());
            }
            File file = new File(generatedDir, baseLibName.concat(extension));
            file.deleteOnExit();
            InputStream inputStream = Client.class.getResourceAsStream("/native/".concat(baseLibName).concat(extension));
            Files.copy(inputStream, file.toPath());
            System.load(file.getAbsolutePath());
            file = new File(generatedDir, libName.concat(extension));
            file.deleteOnExit();
            inputStream = Client.class.getResourceAsStream("/native/".concat(libName).concat(extension));
            Files.copy(inputStream, file.toPath());
            System.load(file.getAbsolutePath());
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
// TODO: Make private
    public Client(AppHandle appHandle, DisconnectListener disconnectListener) {
        super(appHandle, disconnectListener);
    }
}
