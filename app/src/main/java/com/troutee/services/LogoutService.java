package com.troutee.services;

import android.app.IntentService;
import android.content.Intent;

import com.troutee.R;
import com.troutee.api.UserService;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by vicente on 01/04/16.
 */
public class LogoutService extends IntentService {

    private final String TAG = LogoutService.class.getSimpleName();
    private TrouteeDBHelper dbHelper;

    public LogoutService() {
        super("LogoutService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Retrieve User From DB
        RUser user = (RUser) intent.getSerializableExtra("user");
        if(user!=null && Utils.isOnline(this)){
            dbHelper=new TrouteeDBHelper(this);
            UserService userService = new UserService();
            XToken xToken = new XToken(user.getToken());
            Response response=userService.logout(xToken);
            if(response==null || response.getStatusCode()== HttpStatus.SC_INTERNAL_SERVER_ERROR){
                Utils.displayMessage(this,this.getString(R.string.error_logging_out_user), MessageType.ERROR, PositionType.CENTER);
            }else if(response.getStatusCode()== HttpStatus.SC_BAD_REQUEST && response.getStatusCode()!=3000){
                RError rError= (RError) response.getEntity();
                ErrorMapper errorMapper = new ErrorMapper();
                Integer errormsg=errorMapper.errors.get(rError.getCode());
                if(errormsg!=null){//no error message to map
                    Utils.displayMessage(this,this.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
                }
            }else if(response.getStatusCode()== HttpStatus.SC_OK){
               Utils.displayMessage(this,this.getString(R.string.msg_user_log_out_success),MessageType.SUCCESS,PositionType.CENTER);
            }
        }
    }
}
