package net.maidsafe.api;

import net.maidsafe.utils.OSInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;

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
            String tempDir = System.getProperty("java.io.tmpdir");
            File generatedDir = new File(tempDir, "safe_app_java" + System.nanoTime());
            if (!generatedDir.mkdir()) {
                throw new IOException("Failed to create temp directory " + generatedDir.getName());
            }
            generatedDir.deleteOnExit();

            System.setProperty("java.library.path", generatedDir.getAbsolutePath());

            Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
            fieldSysPath.setAccessible( true );
            fieldSysPath.set( null, null );

            ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "SET");
            Map<String, String> env = pb.environment();
            env.put("Path", System.getenv("PATH") + File.pathSeparator + generatedDir.getAbsolutePath());
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            char[] buf = new char[1024];
            while (!isr.ready()) {
                ;
            }
            while (isr.read(buf) != -1) {
                System.out.println(buf);
            }

            File file = new File(generatedDir, baseLibName.concat(extension));
            file.deleteOnExit();
            InputStream inputStream = Client.class.getResourceAsStream("/native/".concat(baseLibName).concat(extension));
            Files.copy(inputStream, file.toPath());

            file = new File(generatedDir, libName.concat(extension));
            file.deleteOnExit();
            inputStream = Client.class.getResourceAsStream("/native/".concat(libName).concat(extension));
            Files.copy(inputStream, file.toPath());

            System.loadLibrary("safe_app");
            System.loadLibrary("safe_app_jni");
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
// TODO: Make private
    public Client(AppHandle appHandle, DisconnectListener disconnectListener) {
        super(appHandle, disconnectListener);
    }
}
