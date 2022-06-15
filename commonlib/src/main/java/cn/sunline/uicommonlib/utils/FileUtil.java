package cn.sunline.uicommonlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 文件操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FileUtil {
    /**
     * 递归创建文件夹
     *
     * @param dirPath
     */
    public static void mkDir(String dirPath) {
        String[] dirArray = dirPath.split("/");
        String pathTemp = "";
        for (int i = 1; i < dirArray.length; i++) {
            pathTemp = pathTemp + "/" + dirArray[i];
            File newF = new File(dirArray[0] + pathTemp);
            if (!newF.exists()) {
                newF.mkdir();
            }
        }
    }

    /**
     *
     * 如果指定文件对象对应的目录不存在，会创建对应目录；
     * 如果指定文件对象对应的文件不存在，会创建相应文件
     *
     * @param file 指定的文件对象
     * @return
     */
    public static boolean createFileAndDirIfNotExist(File file){
        if(null == file || file.isDirectory()){
            Log.e("FileUtil", "file null or file is direcotry");
            return false;
        }

        File dir = file.getParentFile();
        if(null == dir || !dir.mkdirs()){
            Log.e("FileUtil", "dir null or create dir : "  + dir + "failed ");
            return false;
        }

        boolean exist;
        try {
            if (!file.exists()) {
                exist = file.createNewFile();
            } else {
                exist = true;
            }
        }catch (IOException e){
            exist = false;
        }

        if(!exist){
            Log.e("FileUtil", "create file : "  + file + "failed ");
            return false;
        }

        return true;
    }

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     * @param fileName
     * @param content
     */
    public static void write(Context context, String fileName, String content) {
        if (content == null)
            content = "";

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(content.getBytes());

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.e("FileTest", e.getMessage());
        }
        return null;
    }

    public static String readTxtFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName + fileName);
    }

    /**
     * 向手机写图片
     *
     * @param buffer
     * @param folder
     * @param fileName
     * @return
     */
    public static boolean writeFile(byte[] buffer, String folder,
                                    String fileName) {
        boolean writeSucc = false;

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory()
                    + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writeSucc;
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameNoFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
                point);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param size 字节
     * @return
     */
    public static String getFileSizeDesc(long size) {
        if (size <= 0)
            return "0";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件个数
     *
     * @param dir
     * @return
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }

    /**
     * 检查文件是否存在
     *
     * @param name
     * @return
     */
    public static boolean checkSDCardFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }
        return status;
    }

    /**
     * 检查路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean checkFilePathExists(String path) {
        return new File(path).exists();
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    public static long getFreeDiskSpace() {
        String status = Environment.getExternalStorageState();
        long freeSpace = 0;
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                freeSpace = availableBlocks * blockSize / 1024;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return -1;
        }
        return (freeSpace);
    }

    /**
     * 新建目录
     *
     * @param directoryName
     * @return
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 检查是否安装外置的SD卡
     *
     * @return
     */
    public static boolean checkExternalSDExists() {

        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param fileName
     * @return
     */
    public static boolean deleteDirectory(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            status = deleteDirectory(newPath);
        } else
            status = false;
        return status;
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param newPath
     * @return
     */
    public static boolean deleteDirectory(File newPath) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (null != newPath) {

            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除空目录
     * <p>
     * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
     *
     * @return
     */
    public static int deleteBlankPath(String path) {
        File f = new File(path);
        if (!f.canWrite()) {
            return 1;
        }
        if (f.list() != null && f.list().length > 0) {
            return 2;
        }
        if (f.delete()) {
            return 0;
        }
        return 3;
    }

    /**
     * 重命名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        }
        return false;
    }

    /**
     * 清空一个文件夹
     *
     * @param filePath
     */
    public static void clearFileWithPath(String filePath) {
        List<File> files = FileUtil.listPathFiles(filePath);
        if (files.isEmpty()) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                clearFileWithPath(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static String getSDRoot() {

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取手机外置SD卡的根目录
     *
     * @return
     */
    public static String getExternalSDRoot() {

        Map<String, String> evn = System.getenv();

        return evn.get("SECONDARY_STORAGE");
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param root
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    /**
     * 获取一个文件夹下的所有文件
     *
     * @param root
     * @return
     */
    public static List<File> listPathFiles(String root) {
        List<File> allDir = new ArrayList<File>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isFile())
                allDir.add(f);
            else
                listPath(f.getAbsolutePath());
        }
        return allDir;
    }

    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }

    /**
     * 创建目录
     *
     * @param newPath
     */
    public static PathStatus createPath(String newPath) {
        File path = new File(newPath);
        if (path.exists()) {
            return PathStatus.EXITS;
        }
        if (path.mkdir()) {
            return PathStatus.SUCCESS;
        } else {
            return PathStatus.ERROR;
        }
    }

    /**
     * 截取路径名
     *
     * @return
     */
    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }

    /**
     * 获取应用程序缓存文件夹下的指定目录
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getAppCache(Context context, String dir) {
        String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
        File savedir = new File(savePath);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        savedir = null;
        return savePath;
    }

    /**
     * 加载Properties配置文件
     *
     * @param assetsFileName assests目录下的文件名
     * @return
     */
    public static Properties loadProperties(Context context, String assetsFileName) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open(assetsFileName);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getRelativePath(File file1, File file2){
        URI uri1 = file1.toURI();
        URI uri2 =file2.toURI();
        URI relativeURI  = uri2.relativize(uri1);
        return relativeURI.toString();
    }

    public static String getResolvedPath(String srcPath, String relativeSrcPath) throws URISyntaxException {
        return new URI(srcPath).resolve(relativeSrcPath).getPath();
    }

    /**
     * 拷贝指定的asset文件到目标文件目录。
     * @param context   应用上下文
     * @param originalAssetFilePath 指定的asset文件所在路径，相对于assets根目录
     * @param copyDestDir   目标文件目录的绝对路径，可以是任何可写的文件目录，但是目录文件夹必须存在
     * @return
     * @throws IOException
     */
    public static String copyAssetFile(Context context, String originalAssetFilePath, String copyDestDir) throws IOException{
        String fileName = new File(originalAssetFilePath).getName();
        String absCopyDestPath =  new File(copyDestDir, fileName).getAbsolutePath();
        assetsDataToDest(context, originalAssetFilePath, absCopyDestPath);
        return absCopyDestPath;
    }

    /**
     * 读取assets下文件的流，写入目标file对象对应的输出流中
     *
     * @param context
     * @param originalAssetFilePath
     * @param destFilePath
     * @throws IOException
     */
    public static void assetsDataToDest(Context context, String originalAssetFilePath, String destFilePath) throws IOException {
        InputStream input = context.getAssets().open(originalAssetFilePath);
        writeToDestFile(input, destFilePath);
    }

    /**
     * 拷贝指定的raw文件到目标文件目录。
     * @param context   应用上下文
     * @param originalRawFileName 指定的raw文件名
     * @param copyDestDir   目标文件目录的绝对路径，可以是任何可写的文件目录，但是目录文件夹必须存在
     * @return
     * @throws IOException
     */
    public static String copyRawFile(Context context, String originalRawFileName, String copyDestDir) throws IOException{
        String absCopyDestPath =  new File(copyDestDir, originalRawFileName).getAbsolutePath();
        int originalRawResId = context.getResources().getIdentifier(originalRawFileName, "raw", context.getPackageName());
        rawDataToDest(context, originalRawResId, absCopyDestPath);
        return absCopyDestPath;
    }

    /**
     * 读取指定raw文件，写入目标file对象对应的输出流中
     *
     * @param context
     * @param originalRawFileResId 指定raw文件的资源id
     * @param destFilePath  目标file对象绝对路径
     * @throws IOException
     */
    public static void rawDataToDest(Context context, int originalRawFileResId, String destFilePath) throws IOException {
        InputStream input = context.getResources().openRawResource(originalRawFileResId);
        writeToDestFile(input, destFilePath);
    }

    /**
     * 读取输入流，写入指定目标file对象对应的输出流中
     * @param inputStream   待读取输入流
     * @param destFilePath  目标file对象绝对路径
     * @throws IOException
     */
    public static void writeToDestFile(InputStream inputStream, String destFilePath)throws IOException {
        OutputStream output = new FileOutputStream(destFilePath);
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer);
        while (length > 0) {
            output.write(buffer, 0, length);
            length = inputStream.read(buffer);
        }
        output.flush();
        inputStream.close();
        output.close();
    }

    /**
     * 读取指定raw文件的内容，作为文本形式读出
     * @param context
     * @param rawResourceId  指定raw文件的资源id
     * @return
     */
    public static String getRawContent(Context context, int rawResourceId) {
        String fileStr = "";
        InputStream inputStream = null;
        ByteArrayOutputStream result = null;

        try {
            inputStream = context.getResources().openRawResource(rawResourceId);
            result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int length;
            while((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            fileStr = result.toString();
        } catch (IOException var18) {
            Log.e("getJson", var18.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var17) {
                    Log.e("getJson", var17.getMessage());
                }
            }

            if (result != null) {
                try {
                    result.close();
                } catch (IOException var16) {
                    Log.e("getJson", var16.getMessage());
                }
            }

        }

        return fileStr;
    }


    public static String getMimeType(Context context, Uri uri, String filePath){
        String mediaType = null;
//        try{
//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            mmr.setDataSource(filePath);
//            mediaType = 	mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
//        }catch (Exception e){
//            //
//        }
        if(TextUtils.isEmpty(mediaType) || "".equals(mediaType.trim())){
            String ext =  MimeTypeMap.getFileExtensionFromUrl(filePath);
            mediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        if(TextUtils.isEmpty(mediaType) || "".equals(mediaType.trim())){
            ContentResolver resolver = context.getContentResolver();
            mediaType = resolver.getType(uri);
        }
        return mediaType;
    }

    public static byte[] getBytesFromFile(File file)  {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024 * 4]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = fis.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in_b = swapStream.toByteArray();

            return in_b;
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

        return null;
    }
}