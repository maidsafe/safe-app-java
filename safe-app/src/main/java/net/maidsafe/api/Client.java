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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class Client extends Session {

    private static final String NATIVE_PATH = "/native/";
    private static File generatedDir;

    protected Client(final AppHandle appHandle, final DisconnectListener disconnectListener) {
        super(appHandle, disconnectListener);
    }

    public static void load() {
        try {
            final String tempDir = System.getProperty("java.io.tmpdir");

            generatedDir = new File(tempDir, "safe_app_java" + System.nanoTime());
            if (!generatedDir.mkdir()) {
                throw new IOException("Failed to create temp directory " + generatedDir.getName());
            }
            generatedDir.deleteOnExit();

            System.setProperty("java.library.path", generatedDir.getAbsolutePath() + File.pathSeparator
                    + System.getProperty("java.library.path"));
            final Field syspaths = ClassLoader.class.getDeclaredField("sys_paths");
            syspaths.setAccessible(true);
            syspaths.set(null, null);

            copyLibrary(getLibraryName("safe_app"));
            copyLibrary(getLibraryName("safe_app_jni"));

            System.loadLibrary("safe_app_jni");

            if (Session.isMock()) {
                copyLibrary(getLibraryName("safe_authenticator"));
                copyLibrary(getLibraryName("safe_authenticator_jni"));

                System.loadLibrary("safe_authenticator_jni");
            }
        } catch (NoSuchFieldException | IOException | IllegalAccessException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static String getLibraryName(final String library) {
        return System.mapLibraryName(library);
    }

    private static void copyLibrary(final String library) {
        final File file = new File(generatedDir, library);
        file.deleteOnExit();
        final InputStream inputStream = Client.class.getResourceAsStream(NATIVE_PATH.concat(library));
        try {
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}
