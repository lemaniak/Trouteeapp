package com.troutee.mappers;

import android.database.Cursor;
import android.util.Log;

import com.troutee.dto.response.RUser;
import com.troutee.providers.TrouteeContract;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.Utils;

/**
 * Created by vicente on 30/03/16.
 */
public class UserMapper {

    private static final String TAG = UserMapper.class.getSimpleName();

    public static RUser getUserFromCursor(Cursor cursor){
        try{
            int id_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_ID);
            int firstname_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_FIRSTNAME);
            int lastname_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_LASTNAME);
            int email_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_EMAIL);
            int image_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_IMAGE);
            int token_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_TOKEN);
            int logged_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_LOGGED);
            int created_at_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_REGISTRATION_DATE);
            int last_login_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_LASTLOGIN);
            int total_devices_index= cursor.getColumnIndexOrThrow(TrouteeContract.User.COLUMN_TOTAL_DEVICES);
            cursor.moveToFirst();
            int id=cursor.getInt(id_index);
            String firstname=cursor.getString(firstname_index);
            String lastname=cursor.getString(lastname_index);
            String email=cursor.getString(email_index);
            String image=cursor.getString(image_index);
            String token=cursor.getString(token_index);
            String logged=cursor.getString(logged_index);
            String createdAt=cursor.getString(created_at_index);
            String lastLogin=cursor.getString(last_login_index);
            int total_devices=cursor.getInt(total_devices_index);
            return new RUser(id,firstname, lastname, email, image, token, Utils.getLogged(logged),createdAt,lastLogin,total_devices);

        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
        }
        return null;
    }
}
