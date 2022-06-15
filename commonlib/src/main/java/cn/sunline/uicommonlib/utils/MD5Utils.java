package cn.sunline.uicommonlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MD5Utils {
    private static final String LOG_TAG = MD5Utils.class.getSimpleName();
    
    protected static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    public static boolean checkFile(File file, String oriMd5) {
        if (file.exists()) {
            String md5 = null;
            try {
                md5 = getFileMD5(file);
            } catch (Exception e) {
                Logger.e(LOG_TAG, e.getMessage());
                file.delete();
                return false;
            }
            
            if (md5 == null) {
                file.delete();
                return false;
            }
            
            if (md5.equals(oriMd5.toLowerCase()))
                return true;
            else {
                file.delete();
                return false;
            }
        }
        
        return false;
    }
    
    public static String getFileMD5(String fileName) throws IOException {
        File f = new File(fileName);
        return getFileMD5(f);
    }
    
    public static String getFileMD5(File file) {
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        
        MessageDigest md5 = null;
        
        try {
            in = new FileInputStream(file);
            md5 = MessageDigest.getInstance("MD5");
            if (in == null || md5 == null)
                return null;
            
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            
            return bufferToHex(md5.digest()).toLowerCase();
        } catch (Exception e) {
            Logger.e(LOG_TAG, e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (Exception e2) {
                Logger.e(LOG_TAG, e2.getMessage());
            }
        }
        
        return null;
    }
    
    public static String getMD5(byte[] buffer) {
        MessageDigest md5 = null;
        
        try {
            md5 = MessageDigest.getInstance("MD5");
            if (md5 == null)
                return null;
            
            md5.update(buffer);
            
            return bufferToHex(md5.digest()).toLowerCase();
        } catch (Exception e) {
            Logger.e(LOG_TAG, e.getMessage());
        }
        
        return null;
    }
    
    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }
    
    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        
        return stringbuffer.toString();
    }
    
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >>> 4];
        char c1 = hexDigits[bt & 0x0f];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
