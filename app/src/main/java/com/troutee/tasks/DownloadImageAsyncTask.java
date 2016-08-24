package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.troutee.R;
import com.troutee.activities.LoginActivity;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.UserService;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.Constants;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

import java.io.File;
import java.io.InputStream;

/**
 * Created by vicente on 18/03/16.
 */
public class DownloadImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

    private ImageView bmImage;
    private ProgressDialog progressBar;
    private Context context;


    public DownloadImageAsyncTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage(context.getString(R.string.msg_loading));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.progress));
        progressBar.show();
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String urldisplay = params[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            ImageUtils.BitmapToFile(context,mIcon11);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        progressBar.dismiss();
        bmImage.setImageBitmap(result);
    }
}
