package com.lesports.stadium.cache;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
    
    /**
     * @Description  根据传入的字符串，生成md5值
     * 
     * @return String
     *            返回生成的md5值
     */
    public static String md5(String s) {
        String result = null;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
     
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
     
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

}
