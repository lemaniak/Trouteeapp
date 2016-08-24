package com.troutee.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.troutee.R;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RUser;
import com.troutee.mappers.UserMapper;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.tasks.LoginAsyncTask;
import com.troutee.tasks.SignupAsyncTask;
import com.troutee.tasks.ValidateTokenAsyncTask;
import com.troutee.utils.Constants;
import com.troutee.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText email;
    private EditText password;

    private TrouteeDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        email= (EditText) findViewById(R.id.login_email);
        password= (EditText) findViewById(R.id.login_password);
        processAutoLogging();
    }


    public void login(View view){
//        if(email.getText().toString().isEmpty()){
//            Utils.shakeAndToast(this,email,R.string.error_email_required);
//        }else if(!Utils.isValidEmail(email.getText().toString())){
//            Utils.shakeAndToast(this,email, R.string.error_email_invalid);
//        }else if (password.getText().toString().isEmpty()) {
//            Utils.shakeAndToast(this,password, R.string.error_password_required);
//        } else if (!Utils.isValidLength(2, 50, password.getText().toString())) {
//            Utils.shakeAndToast(this,password, R.string.error_password_invalid_length);
//        }else{
//            XLogin xLogin = new XLogin(email.getText().toString(),password.getText().toString());
            XLogin xLogin = new XLogin("lema017@gmail.com","qwerty");
            new LoginAsyncTask(this,xLogin).execute();
//        }

    }

    public void RedirectToSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void processAutoLogging(){
        if(Utils.getBooleanPreference(this, Constants.AUTO_LOGGING)){
            dbHelper=new TrouteeDBHelper(this);
            Cursor cursor = dbHelper.getLoggedUser();
            RUser user= null;
            if(cursor.getCount()>=1){
                user= UserMapper.getUserFromCursor(cursor);
                cursor.close();
                //VALIDATE TOKEN
                XToken xToken = new XToken(user.getToken());
                new ValidateTokenAsyncTask(this,xToken).execute();
            }
        }

    }


}
