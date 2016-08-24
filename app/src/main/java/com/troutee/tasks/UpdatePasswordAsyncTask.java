package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.troutee.R;
import com.troutee.activities.TrouteeActivity;
import com.troutee.activities.UpdateProfileActivity;
import com.troutee.api.UserService;
import com.troutee.dto.request.XUpdatePassword;
import com.troutee.dto.request.XUpdateUser;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.Constants;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by vicente on 12/03/16.
 */
public class UpdatePasswordAsyncTask extends AsyncTask<String, String, String> {

    private UpdateProfileActivity updateProfileActivity;
    private ProgressDialog progressBar;
    private XUpdatePassword xUpdatePassword;
    private Integer errormsg;
    private TrouteeDBHelper dbHelper;
    private Boolean updateImage;

    public UpdatePasswordAsyncTask(UpdateProfileActivity updateProfileActivity, XUpdatePassword xUpdatePassword) {
        this.updateProfileActivity = updateProfileActivity;
        this.xUpdatePassword=xUpdatePassword;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(updateProfileActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage(updateProfileActivity.getString(R.string.msg_updating));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(updateProfileActivity, R.drawable.progress));
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }


    @Override
    protected String doInBackground(String... params) {
            UserService userService = new UserService();
            Response response= userService.updatePassword(xUpdatePassword);
            if(response==null || response.getStatusCode()== HttpStatus.SC_INTERNAL_SERVER_ERROR){
                return "unknow_error";
            }else if(response.getStatusCode()== HttpStatus.SC_UNAUTHORIZED){
                RError rError= (RError) response.getEntity();
                ErrorMapper errorMapper = new ErrorMapper();
                errormsg=errorMapper.errors.get(rError.getCode());
                if(errormsg==null){//no error message to map
                    errormsg=R.string.error_updating_user;
                }
                return "error";
            }else if(response.getStatusCode()== HttpStatus.SC_BAD_REQUEST){
                RError rError= (RError) response.getEntity();
                ErrorMapper errorMapper = new ErrorMapper();
                errormsg=errorMapper.errors.get(rError.getCode());
                if(errormsg==null){//no error message to map
                    errormsg=R.string.error_updating_user;
                }
                return "error";
            }else if(response.getStatusCode()== HttpStatus.SC_OK){
                //insert user info in local database
                dbHelper=new TrouteeDBHelper(updateProfileActivity);
                RUser user= (RUser) response.getEntity();
                dbHelper.updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage(), user.getToken(), true, user.getCreatedAt(), user.getLastLogin(), user.getTotalDevices());
                return "success";
            }
        return null;
    }


    protected void onPostExecute(String result) {
        progressBar.dismiss();
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(updateProfileActivity.getApplicationContext(),updateProfileActivity.getString(R.string.error_updating_user), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("error")==0){
            Utils.displayMessage(updateProfileActivity.getApplicationContext(),updateProfileActivity.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(updateProfileActivity.getApplicationContext(),updateProfileActivity.getString(R.string.msg_user_update_success),MessageType.SUCCESS,PositionType.CENTER);
            Intent intent = new Intent(updateProfileActivity, TrouteeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            updateProfileActivity.startActivity(intent);
            updateProfileActivity.finish();
        }
    }

}
