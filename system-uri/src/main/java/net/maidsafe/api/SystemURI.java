package net.maidsafe.api;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.annotations.Delegate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class SystemURI {


  public static interface LibSysURI {

    public interface CallbackResult { @Delegate public void call(); }

    void install(String bundle, String vendor, String name, String[] args, long argsLen,
                 String icon, String schemes, Pointer userData, CallbackResult result);

  }

  public static LibSysURI load() {
    try {

      String libraryName = "system_uri";

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

      File file = new File(generatedDir, System.mapLibraryName("system_uri"));
      file.deleteOnExit();
      InputStream inputStream = SystemURI.class.getResourceAsStream("/native/".concat(System.mapLibraryName("system_uri")));
      Files.copy(inputStream, file.toPath());

      return  LibraryLoader.create(LibSysURI.class).load("system_uri");
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}