package com.windsoft.oneday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
/**
 * Created by ironFactory on 2015-08-11.
 */
public class ImageBase64 {

    public static String encodeTobase64(Bitmap image) {

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,80, baos);
        byte [] b=baos.toByteArray();
        String temp=null;
        try{
            System.gc();
            temp= Base64.encodeToString(b, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }catch(OutOfMemoryError e) {
            baos=new  ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }


    public static Bitmap decodeBase64(String input,Context context) {
        byte[] decodedByte = Base64.decode(input, 0);


        Boolean isSDPresent = android.os.Environment
                .getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);

        File sdCardDirectory;
        if (isSDPresent) {
            // yes SD-card is present
            sdCardDirectory = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "myFile");

            if (!sdCardDirectory.exists()) {
                if (!sdCardDirectory.mkdirs()) {
                    Log.d("MySnaps", "failed to create directory");

                }
            }
        } else {
            // Sorry
            sdCardDirectory = new File(context.getCacheDir(),"");
        }



        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((1000 - 0) + 1) + 0;

        String nw = "IMG_" + timeStamp + randomNum+".jpg";// also tried .txt as file extension
        File image = new File(sdCardDirectory, nw);



        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {


            outStream = new FileOutputStream(image);
            outStream.write(input.getBytes());

            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("Compress bitmap path", image.getPath());

        return decodeFile(image); // BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private static Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale>=REQUIRED_SIZE && o.outHeight/scale>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
}
