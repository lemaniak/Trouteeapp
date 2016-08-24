package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.troutee.R;
import com.troutee.activities.LoginActivity;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.UserService;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.Response;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by vicente on 18/03/16.
 */
public class ValidateTokenAsyncTask extends AsyncTask<String,String,String>{

    private XToken xToken;
    private LoginActivity loginActivity;
    private ProgressDialog progressBar;
    private Integer errormsg;

    public ValidateTokenAsyncTask(LoginActivity loginActivity, XToken xToken) {
        this.xToken = xToken;
        this.loginActivity = loginActivity;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(loginActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage(loginActivity.getString(R.string.msg_user_log_in_progress));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(loginActivity, R.drawable.progress));
        progressBar.show();
    }


    @Override
    protected String doInBackground(String... params) {
        UserService userService = new UserService();
        Response response= userService.validateToken(xToken);
        if(response==null || response.getStatusCode()== HttpStatus.SC_INTERNAL_SERVER_ERROR){
            return "unknow_error";
        }else if(response.getStatusCode()== HttpStatus.SC_BAD_REQUEST){
            RError rError= (RError) response;
            ErrorMapper errorMapper = new ErrorMapper();
            errormsg=errorMapper.errors.get(rError.getCode());
            if(errormsg==null){//no error message to map
                errormsg=R.string.error_signing_in_user;
            }
            return "error";
        }else if(response.getStatusCode()== HttpStatus.SC_OK){
            return "success";
        }
        return null;
    }

    protected void onPostExecute(String result) {
        progressBar.dismiss();
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(loginActivity.getApplicationContext(),loginActivity.getString(R.string.error_signing_in_user), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("error")==0){
            Utils.displayMessage(loginActivity.getApplicationContext(),loginActivity.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(loginActivity.getApplicationContext(),loginActivity.getString(R.string.msg_user_log_in_success),MessageType.SUCCESS,PositionType.CENTER);
            Intent intent = new Intent(loginActivity, TrouteeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginActivity.startActivity(intent);
            loginActivity.finish();
        }
    }
}
