package com.troutee.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.troutee.utils.WeekDays;

import java.util.List;

/**
 * Created by vicente on 11/03/16.
 */
public class TrouteeDBHelper extends SQLiteOpenHelper{
    private static final String TAG = TrouteeDBHelper.class.getSimpleName();

    public TrouteeDBHelper(Context context) {
        super(context, TrouteeContract.DB_NAME, null, TrouteeContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TrouteeContract.generateUserTable());
            db.execSQL(TrouteeContract.generateDeviceTable());
            db.execSQL(TrouteeContract.generateClientTable());
        }catch (Exception e){
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TrouteeContract.User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrouteeContract.Device.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrouteeContract.Client.TABLE_NAME);
        onCreate(db);
    }


    //USER
    public boolean insertUser  (Integer id, String firstName, String lastName, String email,String image, String token,Boolean logged, String registrationDate,String lastLogin,int totalDevices)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrouteeContract.User.COLUMN_ID, id);
            contentValues.put(TrouteeContract.User.COLUMN_FIRSTNAME, firstName);
            contentValues.put(TrouteeContract.User.COLUMN_LASTNAME, lastName);
            contentValues.put(TrouteeContract.User.COLUMN_EMAIL, email);
            contentValues.put(TrouteeContract.User.COLUMN_IMAGE, image);
            contentValues.put(TrouteeContract.User.COLUMN_TOKEN, token);
            contentValues.put(TrouteeContract.User.COLUMN_LOGGED, getStatus(logged));
            contentValues.put(TrouteeContract.User.COLUMN_REGISTRATION_DATE,registrationDate);
            contentValues.put(TrouteeContract.User.COLUMN_LASTLOGIN,lastLogin);
            contentValues.put(TrouteeContract.User.COLUMN_TOTAL_DEVICES,totalDevices);
            db.insertOrThrow(TrouteeContract.User.TABLE_NAME, null, contentValues);
            db.close();
            disableUsersTokens(id);
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return true;
    }

    //USER
    public boolean insertDevice  (Integer id,String firstname,String lastname, String phone,String registration_status,Integer start_hour,Integer start_minute,Integer end_hour,Integer end_minute,Integer monitor_interval,String image,List<WeekDays> weekdays,Integer userid)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrouteeContract.Device.COLUMN_ID, id);
            contentValues.put(TrouteeContract.Device.COLUMN_FIRSTNAME, firstname);
            contentValues.put(TrouteeContract.Device.COLUMN_LASTNAME, lastname);
            contentValues.put(TrouteeContract.Device.COLUMN_PHONE, phone);
            contentValues.put(TrouteeContract.Device.COLUMN_REGISTRATION_STATUS, registration_status);
            contentValues.put(TrouteeContract.Device.COLUMN_START_MONITOR_HOUR, start_hour);
            contentValues.put(TrouteeContract.Device.COLUMN_START_MONITOR_MINUTE, start_minute);
            contentValues.put(TrouteeContract.Device.COLUMN_START_MONITOR_MINUTE, start_minute);
            contentValues.put(TrouteeContract.Device.COLUMN_END_MONITOR_HOUR, end_hour);
            contentValues.put(TrouteeContract.Device.COLUMN_END_MONITOR_MINUTE, end_minute);
            contentValues.put(TrouteeContract.Device.COLUMN_MONITOR_INTERVAL, monitor_interval);
            contentValues.put(TrouteeContract.Device.COLUMN_IMAGE, image);
            contentValues.put(TrouteeContract.Device.COLUMN_USER_ID, userid);
            if(weekdays!=null){
                if(weekdays.contains(WeekDays.SUNDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_SUNDAY, 1);
                }
                if(weekdays.contains(WeekDays.MONDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_MONDAY, 1);
                }
                if(weekdays.contains(WeekDays.TUESDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_TUESDAY, 1);
                }
                if(weekdays.contains(WeekDays.WEDNESDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_WEDNESDAY, 1);
                }
                if(weekdays.contains(WeekDays.THURSDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_THURSDAY, 1);
                }
                if(weekdays.contains(WeekDays.FRIDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_FRIDAY, 1);
                }
                if(weekdays.contains(WeekDays.SATURDAY)){
                    contentValues.put(TrouteeContract.Device.COLUMN_SATURDAY, 1);
                }
            }
            db.insertOrThrow(TrouteeContract.Device.TABLE_NAME, null, contentValues);
            db.close();
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return true;
    }

    public boolean upsertUser (Integer id, String firstName, String lastName, String email, String image,String token,Boolean logged,String registrationDate,String lastLogin,int totalDevices)
    {
        if(!existUserById(id)){
            return insertUser(id, firstName, lastName, email, image, token, logged, registrationDate, lastLogin, totalDevices);
        }else{
            return updateUser(id, firstName, lastName, email, image, token, logged, registrationDate, lastLogin, totalDevices);
        }
    }

    public boolean updateUser (Integer id, String firstName, String lastName, String email, String image,String token,Boolean logged,String registrationDate,String lastLogin,int totalDevices)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrouteeContract.User.COLUMN_FIRSTNAME, firstName);
        contentValues.put(TrouteeContract.User.COLUMN_LASTNAME, lastName);
        contentValues.put(TrouteeContract.User.COLUMN_EMAIL, email);
        contentValues.put(TrouteeContract.User.COLUMN_IMAGE, image);
        contentValues.put(TrouteeContract.User.COLUMN_TOKEN, token);
        contentValues.put(TrouteeContract.User.COLUMN_LOGGED, getStatus(logged));
        contentValues.put(TrouteeContract.User.COLUMN_REGISTRATION_DATE,registrationDate);
        contentValues.put(TrouteeContract.User.COLUMN_LASTLOGIN,lastLogin);
        contentValues.put(TrouteeContract.User.COLUMN_TOTAL_DEVICES,totalDevices);
        db.update(TrouteeContract.User.TABLE_NAME, contentValues, TrouteeContract.User.COLUMN_ID + " =  " + id, null);
        db.close();
        return true;
    }



    public boolean updateUserStatus (Integer id, Boolean logged)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrouteeContract.User.COLUMN_LOGGED, getStatus(logged) );
        db.update(TrouteeContract.User.TABLE_NAME, contentValues, TrouteeContract.User.COLUMN_ID + " =  " + id, null);
        db.close();
        return true;
    }

    public boolean updateUserToken (Integer id, String token)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrouteeContract.User.COLUMN_TOKEN, token);
        db.update(TrouteeContract.User.TABLE_NAME, contentValues, TrouteeContract.User.COLUMN_ID+" =  "+id, null);
        db.close();
        return true;
    }

    public void disableUsersTokens (Integer id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrouteeContract.User.COLUMN_LOGGED,getStatus(false));
        db.update(TrouteeContract.User.TABLE_NAME,values,TrouteeContract.User.COLUMN_ID+" !="+id,null);
        db.close();
    }

    public Cursor getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TrouteeContract.User.TABLE_NAME +" where "+ TrouteeContract.User.COLUMN_ID +"="+id+"", null );
        return res;
    }

    public boolean existUserById(int id){
        Cursor cursor = getUser(id);
        if(cursor.getCount()==1){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public Cursor getLoggedUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TrouteeContract.User.TABLE_NAME + " WHERE " + TrouteeContract.User.COLUMN_LOGGED + "='" + getStatus(true)+"'", null);
        return res;
    }

    public static String getStatus(Boolean logged){
        String status= null;
        if(logged){
            status="logged";
        }else{
            status="not_logged";
        }
        return status;
    }

    public Integer deleteUser (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int resp =db.delete(TrouteeContract.User.TABLE_NAME,TrouteeContract.User.COLUMN_ID+" = ? "+id,null);
        db.close();
        return resp;
    }


    //CLIENT
    public boolean insertClient (Integer id, String code, String name,String phone, String status, String lat, String lon,Integer version)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrouteeContract.Client.COLUMN_ID, id);
            contentValues.put(TrouteeContract.Client.COLUMN_CODE, code);
            contentValues.put(TrouteeContract.Client.COLUMN_NAME, name);
            contentValues.put(TrouteeContract.Client.COLUMN_PHONE, phone);
            contentValues.put(TrouteeContract.Client.COLUMN_STATUS, status);
            contentValues.put(TrouteeContract.Client.COLUMN_LAT, lat);
            contentValues.put(TrouteeContract.Client.COLUMN_LON, lon);
            contentValues.put(TrouteeContract.Client.COLUMN_VERSION, version);
            db.insertWithOnConflict(TrouteeContract.Client.TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return true;
    }

    public boolean updateClient (Integer id, String code, String name,String phone, String status, String lat, String lon,Integer version)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrouteeContract.Client.COLUMN_CODE, code);
            contentValues.put(TrouteeContract.Client.COLUMN_NAME, name);
            contentValues.put(TrouteeContract.Client.COLUMN_PHONE, phone);
            contentValues.put(TrouteeContract.Client.COLUMN_STATUS, status);
            contentValues.put(TrouteeContract.Client.COLUMN_LAT, lat);
            contentValues.put(TrouteeContract.Client.COLUMN_LON, lon);
            contentValues.put(TrouteeContract.Client.COLUMN_VERSION, version);
            db.update(TrouteeContract.Client.TABLE_NAME, contentValues, TrouteeContract.Client.COLUMN_ID + " =  " + id, null);
            db.close();
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return true;
    }

    public Integer deleteClient (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int resp =db.delete(TrouteeContract.Client.TABLE_NAME,TrouteeContract.Client.COLUMN_ID+" = "+id,null);
        db.close();
        return resp;
    }

    public void deleteAllClient ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrouteeContract.Client.TABLE_NAME, null, null);
        db.close();
    }

    public Cursor getAllClientsFiltered (String filter)
    {
        try{
            SQLiteDatabase db = getReadableDatabase();
            return db.query(TrouteeContract.Client.TABLE_NAME,null,TrouteeContract.Client.COLUMN_CODE+" LIKE '%"+filter+"%' OR "+TrouteeContract.Client.COLUMN_NAME+" LIKE '%"+filter+"%'",null,null,null,null);
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return null;
    }

    public Cursor getAllClientsByIds (String ids)
    {
        try{
            SQLiteDatabase db = getReadableDatabase();
            return db.query(TrouteeContract.Client.TABLE_NAME,null,TrouteeContract.Client.COLUMN_ID+" IN ("+ids+")",null,null,null,null);
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return null;
    }

    public Cursor getClientByName (String name)
    {
        try{
            SQLiteDatabase db = getReadableDatabase();
            return db.query(TrouteeContract.Client.TABLE_NAME,null,TrouteeContract.Client.COLUMN_NAME+" = '"+name+"'",null,null,null,null,"1");
        }catch(Exception ex){
            Log.e(TAG,ex.toString());
        }
        return null;
    }

    public Cursor getAllClients() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            return db.query(TrouteeContract.Client.TABLE_NAME,null,null,null,null,null,null);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    public Cursor getAllClientNamesAndCodes() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            String from[] = {TrouteeContract.Client.COLUMN_ID,TrouteeContract.Client.COLUMN_NAME};
            return db.query(TrouteeContract.Client.TABLE_NAME,from,null,null,null,null,null);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

}
