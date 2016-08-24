package com.troutee.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.support.v4.content.CursorLoader;

/**
 * Created by vicente on 17/03/16.
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private static final int DEFAULT_SCALE_WIDTH = 250;
    private static final int DEFAULT_SCALE_HEIGHT = 250;

    public static File UriToFile(Context context, Uri uri) {
        return new File(getPath(context, uri));
    }

    public static Bitmap getBitmapFromFile(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapFromByteArray(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static Bitmap getRotatedImageFromFile(File file) {
        Bitmap imageBitmap = null;
        //FIND IMAGE ROTATION
        try {
            ExifInterface ei = new ExifInterface(file.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            imageBitmap = getBitmapFromFile(file);
            //ROTATE IMAGE FOR PORTRAIT
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageBitmap = rotateImage(imageBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageBitmap = rotateImage(imageBitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageBitmap = rotateImage(imageBitmap, 270);
                    break;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return imageBitmap;
    }

    public static Bitmap getImageResized(Bitmap originalBitmap, Dimension desiredScaledDimension) {

        if (desiredScaledDimension == null || desiredScaledDimension.height == 0 || desiredScaledDimension.width == 0) {
            //SCALE IMAGE to default dimension
            desiredScaledDimension.width = DEFAULT_SCALE_WIDTH;
            desiredScaledDimension.height = DEFAULT_SCALE_HEIGHT;
        }

        Dimension originalImageDimensions = new Dimension(originalBitmap.getWidth(), originalBitmap.getHeight());
        Dimension resizedImageDimension = getScaledDimension(originalImageDimensions, desiredScaledDimension);
        return Bitmap.createScaledBitmap(originalBitmap, resizedImageDimension.width, resizedImageDimension.height, true);
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public static void BitmapToFile(Context context, Bitmap bitmap) {
        File imageFile;
        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            imageFile = createImageFile(true, context);
        } else {//use internal extorage
            imageFile = createImageFile(false, context);
        }

        if (imageFile != null) {
            writeImageFileContent(bitmap, imageFile);
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File createImageFile(boolean external, Context context) {
        File file;
        if (external) {
            // Get the directory for the user's public pictures directory.
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+Constants.USERIMAGE);
        } else {
            file = new File(context.getFilesDir(), Constants.USERIMAGE);
        }
        return file;
    }


    public static void writeImageFileContent(Bitmap bitmap, File file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            if(!file.exists()){
                file.createNewFile();
            }

            //write the bytes in file
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPath(Context context,Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static Bitmap getRezizedRotatedBitmap(Context context,Intent data){
        File croppedImageFile=ImageUtils.UriToFile(context, data.getData());
        Bitmap rotatedBitmap=ImageUtils.getRotatedImageFromFile(croppedImageFile);
        if(Constants.SCALE_IMAGE){
            Dimension imageDesiredDimension = new Dimension(250,250);
            rotatedBitmap=ImageUtils.getImageResized(rotatedBitmap,imageDesiredDimension);
        }
        ImageUtils.BitmapToFile(context, rotatedBitmap);
        return  rotatedBitmap;
    }

    public static void deleteUserImage(Context context) {
        File imageFile;
        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            imageFile = createImageFile(true,context);
        } else {//use internal extorage
            imageFile = createImageFile(false, context);
        }
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    public static File getUserImageFile(Context context) {
        File imageFile;
        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            imageFile = createImageFile(true,context);
        } else {//use internal extorage
            imageFile = createImageFile(false,context);
        }
        return imageFile;
    }
}
