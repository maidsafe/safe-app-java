package net.maidsafe.utils;

import net.maidsafe.api.model.NativeHandle;

public class Constants {
    public static final NativeHandle ANYONE_HANDLE = new NativeHandle(0, (h) -> {});
    public static final int XOR_NAME_LENGTH = 32;
    public static final long PUBLIC_SIGN_KEY_SIZE = 32;
    public static final long SECRET_SIGN_KEY_SIZE = 64;
    public static final long PUBLIC_ENC_KEY_SIZE = 32;
    public static final long SECRET_ENC_KEY_SIZE = 32;
    Constants() {
        // Constructor intentionally empty
    }

}
