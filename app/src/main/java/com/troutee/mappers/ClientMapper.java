package com.troutee.mappers;

import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;

import com.troutee.dto.response.ClientStatus;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RUser;
import com.troutee.providers.TrouteeContract;
import com.troutee.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vicente on 30/03/16.
 */
public class ClientMapper {

    private static final String TAG = ClientMapper.class.getSimpleName();

    public static List<RClient> getClientsFromCursor(Cursor cursor){
        try{
            if(cursor!=null && !cursor.isClosed() &&cursor.getCount()>0){
                List<RClient> clients = new ArrayList<>();
                int id_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_ID);
                int name_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_NAME);
                int code_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_CODE);
                int phone_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_PHONE);
                int status_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_STATUS);
                int lat_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LAT);
                int lon_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LON);
                int version_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_VERSION);
                while(cursor.moveToNext()){
                    int id= cursor.getInt(id_index);
                    String name = cursor.getString(name_index);
                    String code = cursor.getString(code_index);
                    String phone = cursor.getString(phone_index);
                    String status = cursor.getString(status_index);
                    String latitude = cursor.getString(lat_index);
                    String longitude = cursor.getString(lon_index);
                    int version= cursor.getInt(version_index);
                    clients.add(new RClient(id,code,name,phone, ClientStatus.fromValue(status),Double.parseDouble(latitude),Double.parseDouble(longitude),version));
                }
                return clients;
            }

        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            cursor.close();
        }
        return null;
    }

    public static RClient getClientFromCursor(Cursor cursor){
        try{
            if(cursor!=null && !cursor.isClosed() &&cursor.getCount()>0){
                int id_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_ID);
                int name_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_NAME);
                int code_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_CODE);
                int phone_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_PHONE);
                int status_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_STATUS);
                int lat_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LAT);
                int lon_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LON);
                int version_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_VERSION);
                boolean valid= cursor.moveToFirst();
                if(valid){
                    int id= cursor.getInt(id_index);
                    String name = cursor.getString(name_index);
                    String code = cursor.getString(code_index);
                    String phone = cursor.getString(phone_index);
                    String status = cursor.getString(status_index);
                    String latitude = cursor.getString(lat_index);
                    String longitude = cursor.getString(lon_index);
                    int version= cursor.getInt(version_index);
                    Double lat=null;
                    Double lon=null;
                    if(latitude!=null){
                         lat= Double.parseDouble(latitude);
                    }

                    if(longitude!=null){
                         lon=Double.parseDouble(longitude);
                    }
                    return  new RClient(id,code,name,phone,ClientStatus.fromValue(status),lat,lon,version);
                }else{
                    return null;
                }
            }

        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            cursor.close();
        }
        return null;
    }

    public static List<String> getClientSuggestions(Cursor cursor){
        try{
            if(cursor!=null && !cursor.isClosed() &&cursor.getCount()>0){
                List<String> suggestions = new ArrayList<String>();
                int id_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_ID);
                int name_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_NAME);
                while(cursor.moveToNext()){
                    int id= cursor.getInt(id_index);
                    String name = cursor.getString(name_index);
                    suggestions.add(name);
                }
                cursor.close();
                return suggestions;
            }

        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            cursor.close();
        }
        return null;
    }

    public static HashMap<Integer,RClient> getClientMapFromCursor(Cursor cursor){
        try{
            if(cursor!=null && !cursor.isClosed() &&cursor.getCount()>0){
                HashMap<Integer,RClient> clients = new HashMap<Integer,RClient>();
                int id_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_ID);
                int name_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_NAME);
                int code_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_CODE);
                int phone_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_PHONE);
                int status_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_STATUS);
                int lat_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LAT);
                int lon_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_LON);
                int version_index= cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_VERSION);
                while(cursor.moveToNext()){
                    int id= cursor.getInt(id_index);
                    String name = cursor.getString(name_index);
                    String code = cursor.getString(code_index);
                    String phone = cursor.getString(phone_index);
                    String status = cursor.getString(status_index);
                    String latitude;
                    if(cursor.isNull(lat_index)){
                        latitude = null;
                    }else{
                        latitude = cursor.getString(lat_index);
                    }

                    String longitude;
                    if(cursor.isNull(lon_index)){
                        longitude=null;
                    }else{
                        longitude = cursor.getString(lon_index);
                    }

                    int version= cursor.getInt(version_index);
                    clients.put(id, new RClient(id, code, name, phone, ClientStatus.fromValue(status),latitude != null && !latitude.isEmpty() ?  Double.parseDouble(latitude): null, longitude!= null && !longitude.isEmpty() ? Double.parseDouble(longitude) : null,version));
                }
                return clients;
            }

        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            cursor.close();
        }
        return null;
    }
}
