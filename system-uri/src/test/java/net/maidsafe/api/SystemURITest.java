package net.maidsafe.api;

import jnr.ffi.LibraryLoader;
import org.junit.Test;

import java.io.File;

public class SystemURITest{

  @Test
  public void registerTest() {
    SystemURI.LibSysURI sysURI =  SystemURI.load();
    sysURI.install("me.lionelfaber.example", "Lionel Faber", "Notepad",
        new String[]{"mspaint.exe", "newFile"}, 2, "", "lionelTest", null, () -> {
      System.out.println("This works");
        });


  }

}