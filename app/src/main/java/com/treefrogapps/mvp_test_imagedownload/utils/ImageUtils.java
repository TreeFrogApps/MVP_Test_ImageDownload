package com.treefrogapps.mvp_test_imagedownload.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to check downloaded bitmap file type and decode bitmap
 * reads the first 11 bytes and compares to common file types
 * (not done by extension as this is not guaranteed to work)
 */
public class ImageUtils {

    public static boolean isValidImage(InputStream inputStream){

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

        if (headerValues[0] == 0xFF && headerValues[1] == 0xD8 && headerValues[2] == 0xFF){
            // image is a jpg (can be various types of jpg - denoted from 4th byte
            return true;

        } else if (headerValues[0] == 0x89 && headerValues[1] == 0x50 && headerValues[2] == 0x4E && headerValues[3] == 0x47
                && headerValues[4] == 0x0D && headerValues[5] == 0x0A && headerValues[6] == 0x1A && headerValues[7] == 0xA0){
            // image is a PNG file
            return true;

        } else if (headerValues[0] == 0x47 && headerValues[1] == 0x49 && headerValues[2] == 0x46 && headerValues[3] == 0x38){
            // images is a GIF file
            return true;

        } else if ((headerValues[0] == 0x49 || headerValues[0] == 0x4D) &&
                (headerValues[1] == 0x49 || headerValues[1] == 0x20 || headerValues[0] == 0x4D) &&
                (headerValues[2] == 0x49 || headerValues[2] == 0x2A || headerValues[2] == 0x00)){
            // 99% sure this will be a TIF file
            return true;

        } else if (headerValues[0] == 0x42 && headerValues[1] == 0x4D) {
            // images is a BMP file
            return true;

        } else {
            // not a valid image format
            return false;
        }

    }


}
