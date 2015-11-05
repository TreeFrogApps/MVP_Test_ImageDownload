package com.treefrogapps.mvp_test_imagedownload.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.presenter.ImagePresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class to check downloaded bitmap file type and decode bitmap
 * reads the first 11 bytes and compares to common file types
 * (not done by extension as this is not guaranteed to work)
 */
public class ImageUtils {

    public static boolean isValidImage(InputStream inputStream) {

        byte[] header = new byte[11];

        try {
            inputStream.read(header, 0, header.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // int array to hold value of byte header - use bitmask 0xFF

        int[] headerValues = new int[header.length];
        headerValues[0] = header[0] & 0xFF;
        headerValues[1] = header[1] & 0xFF;
        headerValues[2] = header[2] & 0xFF;
        headerValues[3] = header[3] & 0xFF;
        headerValues[4] = header[4] & 0xFF;
        headerValues[5] = header[5] & 0xFF;
        headerValues[6] = header[6] & 0xFF;
        headerValues[7] = header[7] & 0xFF;
        headerValues[8] = header[8] & 0xFF;
        headerValues[9] = header[9] & 0xFF;
        headerValues[10] = header[10] & 0xFF;

        if (headerValues[0] == 0xFF && headerValues[1] == 0xD8 && headerValues[2] == 0xFF) {
            // image is a jpg (can be various types of jpg - denoted from 4th byte
            Log.i("Image Type Valid", "jpg");
            return true;

        } else if (headerValues[0] == 0x89 && headerValues[1] == 0x50 && headerValues[2] == 0x4E && headerValues[3] == 0x47) {
            // image is a PNG file
            Log.i("Image Type Valid", "png");
            return true;

        } else if (headerValues[0] == 0x47 && headerValues[1] == 0x49 && headerValues[2] == 0x46 && headerValues[3] == 0x38) {
            // images is a GIF file
            Log.i("Image Type Not Supported", "gif");
            return false;

        } else if ((headerValues[0] == 0x49 || headerValues[0] == 0x4D) &&
                (headerValues[1] == 0x49 || headerValues[1] == 0x20 || headerValues[0] == 0x4D) &&
                (headerValues[2] == 0x49 || headerValues[2] == 0x2A || headerValues[2] == 0x00)) {
            // 99% sure this will be a TIF file
            Log.i("Image Type Valid", "tif");
            return true;

        } else if (headerValues[0] == 0x42 && headerValues[1] == 0x4D) {
            // images is a BMP file
            Log.i("Image Type Valid", "bmp");
            return true;

        } else {
            // not a valid image format
            return false;
        }
    }

    public static Bitmap scaleFilteredBitmap(InputStream inputStream) {

        Bitmap downloadedBitmap = BitmapFactory.decodeStream(inputStream);
        Bitmap filteredBitmap = downloadedBitmap.copy(downloadedBitmap.getConfig(), true);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadedBitmap.recycle();

        int width = filteredBitmap.getWidth();
        int height = filteredBitmap.getHeight();

        for (int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {

                int p = filteredBitmap.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int a = Color.alpha(p);

                filteredBitmap.setPixel(i, j,
                        Color.argb(a, Math.min(((r + g + b) / 3), 255), Math.min(((r + g + b) / 3), 255), Math.min(((r + g + b) / 3), 255)));
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss_SSS", Locale.getDefault());
        String filename = sdf.format(new Date());

        File imageFile = new File(ImagePresenter.FOLDER_LOCATION, "Image_downloaded_" + filename + ".jpg");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            filteredBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int imageWidth = bmOptions.outWidth;
        int imageHeight = bmOptions.outHeight;

        int scaleFactor = 1;
        int maxPixelCount = 120000;

        while ((imageWidth * imageHeight) > maxPixelCount) {
            imageWidth /= 2;
            imageHeight /= 2;
            scaleFactor++;
        }

        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }

    public static String checkURL(String url) {

        String startUrl = "http://";
        String startUrlSecure = "https://";

        if (startUrl.equals(url.substring(0, 7)) || startUrlSecure.equals(url.substring(0, 8))) {
            return url;
        } else {
            return startUrl + url;
        }


    }

    public static Bitmap imageLoader(File imageFile) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int imageWidth = bmOptions.outWidth;
        int imageHeight = bmOptions.outHeight;

        int scaleFactor = 1;
        int maxPixelCount = 120000;

        while ((imageWidth * imageHeight) > maxPixelCount) {
            imageWidth /= 2;
            imageHeight /= 2;
            scaleFactor++;
        }

        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }


}
