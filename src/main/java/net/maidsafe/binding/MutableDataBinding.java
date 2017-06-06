package net.maidsafe.binding;


import com.sun.jna.Library;
import com.sun.jna.Pointer;
import net.maidsafe.binding.model.FfiCallback;

import java.awt.*;

public interface MutableDataBinding extends Library {

    // MData Info
    void mdata_info_new_public(Pointer appHandle, byte[] name, long typeTag, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_info_new_private(Pointer appHandle, byte[] name, long typeTag, byte[] secretKey, byte[] nonce, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_info_random_public(Pointer appHandle, long typeTag, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_info_random_private(Pointer appHandle, long typeTag, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_info_encrypt_entry_key(Pointer appHandle, long mDataInfoHandle, byte[] key, long keyLen, Pointer userData, FfiCallback.DataCallback cb);

    void mdata_info_encrypt_entry_value(Pointer appHandle, long mDataInfoHandle, byte[] value, long valueLen, Pointer userData, FfiCallback.DataCallback cb);

    void mdata_info_decrypt(Pointer appHandle, long mDataInfoHandle, byte[] input, long inputLen, Pointer userData, FfiCallback.DataCallback cb);

    void mdata_info_extract_name_and_type_tag(Pointer appHandle, long mDataInfoHandle, Pointer userData, FfiCallback.PointerCallback cb);

    void mdata_info_serialise(Pointer appHandle, long mDataInfoHandle, Pointer userData, FfiCallback.DataCallback cb);

    void mdata_info_deserialise(Pointer appHandle, byte[] data, long dataLen, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_info_free(Pointer appHandle, long mDataInfoHandle, Pointer userData, FfiCallback.ResultCallback cb);

    // MData Entries
    void mdata_entries_new(Pointer appHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_entries_insert(Pointer appHandle, long entriesHandle, byte[] key, long keyLen, byte[] value, long valueLen, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_entries_len(Pointer appHandle, long entriesHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_entries_get(Pointer appHandle, long entriesHandle, byte[] key, long keyLen, Pointer userData, FfiCallback.DataWithVersionCallback cb);

    void mdata_entries_for_each(Pointer appHandle, long entriesHandle, FfiCallback.ForEachCallback forEachCb, Pointer userData, FfiCallback.ResultCallback resCb);

    void mdata_entries_free(Pointer appHandle, long entriesHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_keys_len(Pointer appHandle, long keysHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_keys_for_each(Pointer appHandle, long keysHandle, FfiCallback.ForEachKeysCallback keysCb, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_keys_free(Pointer appHandle, long keysHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_values_len(Pointer appHandle, long valuesHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_values_for_each(Pointer appHandle, long valuesHandle, FfiCallback.ForEachValuesCallback valuesCb, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_values_free(Pointer appHandle, long valuesHandle, Pointer userData, FfiCallback.ResultCallback cb);

    // MData entry actions
    void mdata_entry_actions_new(Pointer appHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_entry_actions_insert(Pointer appHandle, long entryActionHandle, byte[] key, long keyLen, byte[] value, long valueLen, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_entry_actions_update(Pointer appHandle, long entryActionHandle, byte[] key, long keyLen, byte[] value, long valueLen, long version, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_entry_actions_delete(Pointer appHandle, long entryActionHandle, byte[] key, long keyLen, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_entry_actions_free(Pointer appHandle, long entryActionHandle, Pointer userData, FfiCallback.ResultCallback cb);

    // MData Permissions
    void mdata_permission_set_new(Pointer appHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_permissions_set_allow(Pointer appHandle, long permissionHandle, int mDataAction, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_permissions_set_deny(Pointer appHandle, long permissionHandle, int mDataAction, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_permissions_set_clear(Pointer appHandle, long permissionHandle, int mDataAction, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_permissions_set_free(Pointer appHandle, long permissionHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_permissions_new(Pointer appHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_permissions_len(Pointer appHandle, long permissionsHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_permissions_get(Pointer appHandle, long permissionHandle, long signKeyHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_permissions_for_each(Pointer appHandle, long permissionsHandle, FfiCallback.ForEachPermissionsCallback forEachCb, Pointer userData, FfiCallback.ResultCallback resCb);

    void mdata_permissions_insert(Pointer appHandle, long permissionHandle, long signKeyHandle, long permissionSetHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_permissions_free(Pointer appHandle, long permissionsHandle, Pointer userData, FfiCallback.ResultCallback cb);

    //MData Mod
    void mdata_put(Pointer appHandle, long infoHandle, long permissionHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_get_version(Pointer appHandle, long infoHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_get_value(Pointer appHandle, long infoHandle, byte[] key, long keyLen, Pointer userData, FfiCallback.DataWithVersionCallback cb);

    void mdata_list_entries(Pointer appHandle, long infoHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_list_keys(Pointer appHandle, long infoHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_list_values(Pointer appHandle, long infoHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_mutate_entries(Pointer appHandle, long infoHandle, long actionHandle, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_list_permissions(Pointer appHandle, long infoHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_list_user_permissions(Pointer appHandle, long infoHandle, long signKeyHandle, Pointer userData, FfiCallback.HandleCallback cb);

    void mdata_set_user_permissions(Pointer appHandle, long infoHandle, long signKeyHandle, long permissionSetHandle, long version, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_del_user_permissions(Pointer appHandle, long infoHandle, long signKeyHandle, long version, Pointer userData, FfiCallback.ResultCallback cb);

    void mdata_change_owner(Pointer appHandle, long infoHandle, long signKeyHandle, long version, Pointer userData, FfiCallback.ResultCallback cb);
}
