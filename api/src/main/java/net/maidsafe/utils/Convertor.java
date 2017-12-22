package net.maidsafe.utils;

import net.maidsafe.api.model.MDataValue;

import java.util.ArrayList;
import java.util.List;

public class Convertor {

    public static List<MDataValue> toMDataValue(net.maidsafe.safe_app.MDataValue[] ffiMDataValues) {
        List<MDataValue> mDataValues = new ArrayList<>();
        for (net.maidsafe.safe_app.MDataValue value : ffiMDataValues) {
            mDataValues.add(new MDataValue(value.getContentPtr(), value.getEntryVersion()));
        }
        return mDataValues;
    }

    public static List<byte[]> toKeys(net.maidsafe.safe_app.MDataKey[] ffiMDataKeys) {
        List<byte[]> keys = new ArrayList<>();
        for (net.maidsafe.safe_app.MDataKey ffiKeys : ffiMDataKeys) {
            keys.add(ffiKeys.getValPtr());
        }
        return keys;
    }
}
