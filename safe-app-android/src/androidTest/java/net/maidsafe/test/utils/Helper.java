package net.maidsafe.test.utils;


import android.support.test.InstrumentationRegistry;
import net.maidsafe.api.R;
import net.maidsafe.api.Session;

import java.io.*;

public class Helper {

    public static final String APP_ID = "net.maidsafe.test.java";
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static void androidSetup() {
        try {
            // copy safe_core.config
            String exeName = Session.getAppStem().get();
            String appFilesPath = InstrumentationRegistry.getContext().getFilesDir().getPath();
            OutputStream outputStream = new FileOutputStream(new File(InstrumentationRegistry.getContext().getFilesDir() + "/" + exeName + ".safe_core.config"));
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            String content = InstrumentationRegistry.getContext().getResources().getString(R.string.core_config)
                    .replace("applicationFilesPath", appFilesPath);
            streamWriter.write(content);
            streamWriter.close();
            outputStream = new FileOutputStream(new File(InstrumentationRegistry.getContext().getFilesDir() + "/" + "log.toml"));
            streamWriter = new OutputStreamWriter(outputStream);
            content = InstrumentationRegistry.getContext().getResources().getString(R.string.logtoml);
            streamWriter.write(content);
            streamWriter.close();
            Session.setAdditionalSearchPath(appFilesPath).get();
            Session.initLogging("logfile").get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
