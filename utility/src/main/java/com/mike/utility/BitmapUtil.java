package com.mike.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class BitmapUtil {
    public static Bitmap loadBitMapFromUri(Uri uri, int reqWidth, int reqHeight) {
        File file = new File(uri.getPath());

        return loadBitMapFromFile(file, reqWidth, reqHeight);
    }

    public static BitmapFactory.Options calculateOptions(InputStream inputStream, int reqWidth, int reqHeight) throws IOException {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(inputStream, null, options);

        if (reqHeight == -1 || reqWidth == -1) {
            reqHeight = options.outHeight;
            reqWidth = options.outWidth;
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return options;

    }

    public static Bitmap loadBitMapFromFile(File file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        if (reqHeight == -1 || reqWidth == -1) {
            reqHeight = options.outHeight;
            reqWidth = options.outWidth;
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // A little cheat here, when height equal to 0 then we will calculate depend on reqWidth
        if (height == 0 && width > reqWidth) {
            final int halfWidth = width / 2;
            inSampleSize *= 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        } else if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            inSampleSize *= 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }


        return inSampleSize;
    }
}
