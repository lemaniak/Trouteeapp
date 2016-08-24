package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.troutee.R;
import com.troutee.activities.SignupActivity;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.ClientService;
import com.troutee.dto.request.XSignup;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RClientsResponse;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.api.UserService;
import com.troutee.services.UpdateClientsService;
import com.troutee.utils.Constants;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by vicente on 12/03/16.
 */
public class SignupAsyncTask extends AsyncTask<String, String, String> {

    private SignupActivity context;
    private ProgressDialog progressBar;
    private XSignup xSignup;
    private Integer errormsg;
    private TrouteeDBHelper dbHelper;

    public SignupAsyncTask(SignupActivity context, XSignup xSignup) {
        this.context = context;
        this.xSignup=xSignup;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage(context.getString(R.string.msg_user_registration_in_progress));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.progress));
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }


    @Override
    protected String doInBackground(String... params) {
        File imgFile =ImageUtils.getUserImageFile(context);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArray = bytes.toByteArray();
            xSignup.setImage(byteArray);
        }
            UserService userService = new UserService();
            Response response= userService.signup(xSignup);
            if(response==null || response.getStatusCode()== HttpStatus.SC_INTERNAL_SERVER_ERROR){
                return "unknow_error";
            }else if(response.getStatusCode()== HttpStatus.SC_BAD_REQUEST){
                RError rError= (RError) response.getEntity();
                ErrorMapper errorMapper = new ErrorMapper();
                errormsg=errorMapper.errors.get(rError.getCode());
                if(errormsg==null){//no error message to map
                    errormsg=R.string.error_registering_user;
                }
                return "error";
            }else if(response.getStatusCode()== HttpStatus.SC_CREATED){
                if (imgFile.exists()) {
                    imgFile.delete();
                }

                //insert user info in local database
                dbHelper=new TrouteeDBHelper(context);
                RUser user= (RUser) response.getEntity();
                dbHelper.insertUser(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getImage(),user.getToken(),true,user.getCreatedAt(),user.getLastLogin(),user.getTotalDevices());
                updateClientsDB(user);
                return "success";
            }
        return null;
    }

    private void updateClientsDB(RUser rUser){
        Intent updateClientsIntent = new Intent(context, UpdateClientsService.class);
        updateClientsIntent.putExtra("user", rUser);
        context.startService(updateClientsIntent);
    }



    protected void onPostExecute(String result) {
        progressBar.dismiss();
        ImageUtils.deleteUserImage(context);
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(context.getApplicationContext(),context.getString(R.string.error_registering_user), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("error")==0){
            Utils.displayMessage(context.getApplicationContext(),context.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(context.getApplicationContext(), context.getString(R.string.msg_user_registration_success),MessageType.SUCCESS,PositionType.CENTER);
            Intent intent = new Intent(context, TrouteeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            context.finish();

        }
    }

}
