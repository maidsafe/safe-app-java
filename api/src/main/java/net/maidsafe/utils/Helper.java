package net.maidsafe.utils;

import net.maidsafe.model.MDataValue;
import net.maidsafe.safe_app.FfiResult;
import net.maidsafe.safe_app.MdataValue;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static Exception ffiResultToException(FfiResult result) {
        return new Exception(result.getDescription() + " : " + result.getErrorCode());
    }

    public static List<MDataValue> convertFromFfiMDataValue(MdataValue[] ffiMDataValues) {
        List<MDataValue> mDataValues = new ArrayList<>();
        for (MdataValue value : ffiMDataValues) {
            mDataValues.add(new MDataValue(new byte[]{value.getContentPtr()}, value.getEntryVersion()));
        }
        return mDataValues;
    }
}
