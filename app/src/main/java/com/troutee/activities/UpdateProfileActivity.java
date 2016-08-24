package com.troutee.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.troutee.R;
import com.troutee.dto.request.XUpdatePassword;
import com.troutee.dto.request.XUpdateUser;
import com.troutee.dto.response.RUser;
import com.troutee.tasks.UpdatePasswordAsyncTask;
import com.troutee.tasks.UpdateUserAsyncTask;
import com.troutee.utils.Constants;
import com.troutee.utils.Dimension;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    public CircleImageView userImageRound;
    private EditText email;
    private EditText firstname;
    private EditText lastname;
    private EditText password;
    private EditText repassword;


    private static final int SELECT_IMAGE_CODE = 0;
    private static final int TAKE_PICTURE_CODE = 1;
    private static final int CROP_IMAGE_CODE = 2;
    private static final boolean SCALE_IMAGE=true;
    private static final int CAMERA_PERMISSION_OK = 3;

    private boolean pictureUpdated;
    private RUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.update_profile));

        init();
    }

    private void init(){
        email= (EditText) findViewById(R.id.update_profile_email);
        firstname= (EditText) findViewById(R.id.update_profile_firstname);
        lastname= (EditText) findViewById(R.id.update_profile_lastname);
        userImageRound= (CircleImageView) findViewById(R.id.update_profile_img_user_round);
        user= (RUser) getIntent().getSerializableExtra("user");
        if(user!=null){
            email.setText(user.getEmail());
            firstname.setText(user.getFirstName());
            lastname.setText(user.getLastName());
        }

        File imgFile = ImageUtils.getUserImageFile(this);
        if(imgFile.exists()){
            Bitmap userImage=ImageUtils.getBitmapFromFile(imgFile);
            userImageRound.setImageBitmap(userImage);
        }
    }

    public void showUpdatePasswordDialog(View view){
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.change_password);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
         password = (EditText) dialog.findViewById(R.id.change_password_password);
         repassword = (EditText) dialog.findViewById(R.id.change_password_confirmation);

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.change_password_cancel);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        Button updateButton = (Button) dialog.findViewById(R.id.change_password_save);
        // if decline button is clicked, close the custom dialog
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.isOnline(UpdateProfileActivity.this.getApplicationContext())){
                    Utils.displayMessage(UpdateProfileActivity.this.getApplicationContext(), getString(R.string.error_no_internet), MessageType.ERROR, PositionType.CENTER);
                }else {
                    if (password.getText().toString().isEmpty()) {
                        Utils.shakeAndToast(UpdateProfileActivity.this,password, R.string.error_password_required);
                    } else if (!Utils.isValidLength(2, 50, password.getText().toString())) {
                        Utils.shakeAndToast(UpdateProfileActivity.this,password, R.string.error_password_invalid_length);
                    } else if (repassword.getText().toString().isEmpty()) {
                        Utils.shakeAndToast(UpdateProfileActivity.this,repassword, R.string.error_repassword_required);
                    } else if (!password.getText().toString().equals(repassword.getText().toString())) {
                        Utils.shakeAndToast(UpdateProfileActivity.this,repassword, R.string.error_repassword_mismatch);
                    }else{
                        XUpdatePassword xUpdatePassword = new XUpdatePassword();
                        xUpdatePassword.setId(user.getId());
                        xUpdatePassword.setToken(user.getToken());
                        xUpdatePassword.setPassword(password.getText().toString());

                        UpdatePasswordAsyncTask updatePasswordAsyncTask = new UpdatePasswordAsyncTask(UpdateProfileActivity.this,xUpdatePassword);
                        updatePasswordAsyncTask.execute();
                        dialog.dismiss();
                    }
                }

            }
        });

    }


    public void updateUser(View view){
        XUpdateUser xUpdateUser = new XUpdateUser();
        if(!Utils.isOnline(this.getApplicationContext())){
            Utils.displayMessage(this.getApplicationContext(),getString(R.string.error_no_internet),MessageType.ERROR,PositionType.CENTER);
        }else {
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
            }else{
                xUpdateUser.setEmail(email.getText().toString());
                xUpdateUser.setFirstName(firstname.getText().toString());
                xUpdateUser.setLastName(lastname.getText().toString());
                xUpdateUser.setId(user.getId());
                xUpdateUser.setToken(user.getToken());
                new UpdateUserAsyncTask(this,xUpdateUser,pictureUpdated).execute();
            }
        }
    }




    public void takePictureProfile(View view) {
            Utils.takePicture(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            Utils.displayMessage(this, getString(R.string.error_taking_picture), MessageType.ERROR, PositionType.CENTER);
        }else{
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
                    Bitmap rotatedBitmap=ImageUtils.getRezizedRotatedBitmap(this, data);
                    userImageRound = (CircleImageView) findViewById(R.id.update_profile_img_user_round);
                    if(userImageRound!=null && rotatedBitmap!=null){
                        userImageRound.setImageBitmap(rotatedBitmap);
                        ImageUtils.BitmapToFile(this, rotatedBitmap);
                    }
                    pictureUpdated=true;
                    break;
                case Constants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA:
                    Utils.takePicture(this);
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

    }
}
