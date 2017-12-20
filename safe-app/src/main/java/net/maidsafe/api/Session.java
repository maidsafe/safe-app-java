package net.maidsafe.api;

import net.maidsafe.utils.OSInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Session extends BaseSession {

    public static void load() {
        try {
            String libName = "libsafe_app";
            String extension = ".so";
            switch (OSInfo.getOs()) {
                case WINDOWS:
                    libName = "safe_app";
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
            File file = new File(generatedDir, libName.concat(extension));
            file.deleteOnExit();
            InputStream inputStream = Session.class.getResourceAsStream("/native/".concat(libName).concat(extension));
            Files.copy(inputStream, file.toPath());
            System.load(file.getAbsolutePath());
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
