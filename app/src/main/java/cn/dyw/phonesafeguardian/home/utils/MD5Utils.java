package cn.dyw.phonesafeguardian.home.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dyw on 2017/3/18.
 */

public class MD5Utils {

    public static String encode(String text){
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());

            StringBuffer sb = new StringBuffer();
            for(byte b : result){
                int number = b&0xff;
                String hex = Integer.toHexString(number);
                if(hex.length()==1)
                    sb.append("0" + hex);
                else
                    sb.append(hex);
            }
            Log.d("MD5Utils", sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}
