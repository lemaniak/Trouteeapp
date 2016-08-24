package com.troutee.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.troutee.R;
import com.troutee.dto.request.XSignup;
import com.troutee.tasks.SignupAsyncTask;
import com.troutee.utils.Constants;
import com.troutee.utils.Dimension;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends Activity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText repassword;

    public CircleImageView userImageRound;
    public ImageView userImage;

    private static final int SELECT_IMAGE_CODE = 0;
    private static final int TAKE_PICTURE_CODE = 1;
    private static final int CROP_IMAGE_CODE = 2;
    private static final boolean SCALE_IMAGE=true;


    private static final String TAG = SignupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        firstname = (EditText) this.findViewById(R.id.signup_tf_firstname);
        lastname = (EditText) this.findViewById(R.id.signup_tf_lastname);
        email = (EditText) this.findViewById(R.id.signup_tf_email);
        password = (EditText) this.findViewById(R.id.signup_tf_password);
        repassword = (EditText) this.findViewById(R.id.signup_tf_repassword);


    }

    protected void onDestroy() {
        super.onDestroy();
       ImageUtils.deleteUserImage(this);
    }


    public void RedirectToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void Signup(View view) {
        if (firstname.getText().toString().isEmpty()) {
            Utils.shakeAndToast(this,firstname, R.string.error_firstname_required);
        } else if (!Utils.isValidLength(2, 50, firstname.getText().toString())) {
            Utils.shakeAndToast(this,firstname, R.string.error_firstname_invalid_length);
        } else if (lastname.getText().toString().isEmpty()) {
            Utils.shakeAndToast(this,lastname, R.string.error_lastname_required);
        } else if (!Utils.isValidLength(2, 50, lastname.getText().toString())) {
            Utils.shakeAndToast(this,lastname, R.string.error_lastname_invalid_length);
        } else if (email.getText().toString().isEmpty()) {
            Utils.shakeAndToast(this,email, R.string.error_email_required);
        } else if (!Utils.isValidEmail(email.getText().toString())) {
            Utils.shakeAndToast(this,email, R.string.error_email_invalid);
        } else if (password.getText().toString().isEmpty()) {
            Utils.shakeAndToast(this,password, R.string.error_password_required);
        } else if (!Utils.isValidLength(2, 50, password.getText().toString())) {
            Utils.shakeAndToast(this,password, R.string.error_password_invalid_length);
        } else if (repassword.getText().toString().isEmpty()) {
            Utils.shakeAndToast(this,repassword, R.string.error_repassword_required);
        } else if (!password.getText().toString().equals(repassword.getText().toString())) {
            Utils.shakeAndToast(this,repassword, R.string.error_repassword_mismatch);
        } else if(!Utils.isOnline(this.getApplicationContext())){
                Utils.displayMessage(this.getApplicationContext(),getString(R.string.error_no_internet),MessageType.ERROR,PositionType.CENTER);
        }else {
            XSignup xSignup = new XSignup();
            xSignup.setFirstName(firstname.getText().toString());
            xSignup.setLastName(lastname.getText().toString());
            xSignup.setEmail(email.getText().toString());
            xSignup.setPassword(password.getText().toString());


//            xSignup.setFirstName("vicente");
//            xSignup.setLastName("san silvestre");
//            xSignup.setEmail("lema017@gmail.com");
//            xSignup.setPassword("clarodeluna");

            new SignupAsyncTask(this,xSignup).execute();
        }
    }


    public void takePicture(View view) {
       Utils.takePicture(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            Utils.displayMessage(this,getString(R.string.error_taking_picture), MessageType.ERROR, PositionType.CENTER);
        }else if(resultCode == RESULT_OK){
            switch(requestCode) {
                case Constants.SELECT_IMAGE_CODE:
                    if(data==null){
                        Utils.displayMessage(this,getString(R.string.error_taking_picture),MessageType.ERROR,PositionType.CENTER);
                    }else{
                        Utils.cropImage(this, data.getData());
                    }
                    break;
                case Constants.TAKE_PICTURE_CODE:
                    if(data==null){
                        Utils.displayMessage(this,getString(R.string.error_taking_picture),MessageType.ERROR,PositionType.CENTER);
                    }else{
                        Utils.cropImage(this, data.getData());
                    }
                    break;
                case Constants.CROP_IMAGE_CODE:
                    Bitmap rotatedBitmap=ImageUtils.getRezizedRotatedBitmap(this,data);
                    userImage = (ImageView) findViewById(R.id.signup_img_user);
                    userImage.setVisibility(View.GONE);
                    userImageRound = (CircleImageView) findViewById(R.id.signup_img_user_round);
                    userImageRound.setVisibility(View.VISIBLE);
                    userImageRound.setImageBitmap(rotatedBitmap);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

    }



}
