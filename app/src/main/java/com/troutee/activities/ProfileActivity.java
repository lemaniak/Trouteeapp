package com.troutee.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.troutee.R;
import com.troutee.dto.response.RUser;
import com.troutee.utils.Constants;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    public CircleImageView userImageRound;
    private RUser user;
    private TextView profileEmail;
    private TextView memberSince;
    private TextView lastLogin;
    private TextView registeredDevices;
    private Switch autoLogin;
    private Switch locationMocking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.profile));
        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
        userImageRound= (CircleImageView) findViewById(R.id.update_profile_img_user_round);
        File imgFile = ImageUtils.getUserImageFile(this);
        if(imgFile.exists()){
            Bitmap userImage=ImageUtils.getBitmapFromFile(imgFile);
            userImageRound.setImageBitmap(userImage);
        }
    }

    private void init(){
        userImageRound= (CircleImageView) findViewById(R.id.update_profile_img_user_round);
        profileEmail= (TextView) findViewById(R.id.profile_email);
        memberSince =(TextView) findViewById(R.id.profile_lbl_member_since_value);
        lastLogin = (TextView) findViewById(R.id.profile_lbl_last_login_value);
        registeredDevices = (TextView) findViewById(R.id.profile_lbl_total_devices_value);

        autoLogin = (Switch) findViewById(R.id.profile_switch_auto_log_in);
        locationMocking = (Switch) findViewById(R.id.profile_switch_location_mock);

        File imgFile = ImageUtils.getUserImageFile(this);
        if(imgFile.exists()){
            Bitmap userImage=ImageUtils.getBitmapFromFile(imgFile);
            userImageRound.setImageBitmap(userImage);
        }

        user= (RUser) getIntent().getSerializableExtra("user");
        if(user!=null){
            profileEmail.setText(user.getEmail());
            memberSince.setText(user.getCreatedAt());
            lastLogin.setText(user.getLastLogin());
            registeredDevices.setText(user.getTotalDevices() + "");
        }

        if(Utils.getBooleanPreference(this,Constants.AUTO_LOGGING)){
            autoLogin.setChecked(true);
        }

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.setBooleanPreference(ProfileActivity.this, Constants.AUTO_LOGGING, true);
                } else {
                    Utils.setBooleanPreference(ProfileActivity.this, Constants.AUTO_LOGGING, false);
                }
            }
        });

        if(Utils.getBooleanPreference(this,Constants.LOCATION_MOCKING)){
            locationMocking.setChecked(true);
        }

        locationMocking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.setBooleanPreference(ProfileActivity.this, Constants.LOCATION_MOCKING, true);
                } else {
                    Utils.setBooleanPreference(ProfileActivity.this, Constants.LOCATION_MOCKING, false);
                }
            }
        });
    }


    public void updateProfile(View view){
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

}
