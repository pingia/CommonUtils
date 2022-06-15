package cn.sunline.uicommonlib.utils;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.WorkerThread;

import java.io.File;

/**
 * 数据删除工具类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月27日 上午10:18:22
 * 
 */
public class DataCleanManager {

	/**
	 * 清除本应用内部缓存
	 * (/data/data/com.xxx.xxx/cache)
	 * * (/data/data/com.xxx.xxx/files)
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清楚本应用所有数据库
	 * (/data/data/com.xxx.xxx/databases)
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}
	
	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * @param file
	 */
	public static void cleanCustomCache(File file) {
		deleteFilesByDirectory(file);
	}

	/**
	 * 获取context对应的应用缓存大小
	 */
	@WorkerThread
	public static long getCacheSize(Context context) {
		long fileSize = 0;

		File databaseDir = new File("/data/data/"
				+ context.getPackageName() + "/databases");
		File filesDir = context.getFilesDir();
		File cacheDir = context.getCacheDir();

		fileSize += FileUtil.getDirSize(databaseDir);
		fileSize += FileUtil.getDirSize(filesDir);
		fileSize += FileUtil.getDirSize(cacheDir);

		if (MethodsCompat.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = MethodsCompat.getExternalCacheDir(context);
			fileSize += FileUtil.getDirSize(externalCacheDir);
		}

		if (fileSize > 0) {
			return fileSize;
		}

		return 0;
	}

	@WorkerThread
	public static void clearAllCache(Context context){
		DataCleanManager.cleanDatabases(context);
		// 清除数据缓存
		DataCleanManager.cleanInternalCache(context);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (MethodsCompat.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			DataCleanManager.cleanCustomCache(MethodsCompat
					.getExternalCacheDir(context));
		}
	}

	/**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 
	 * @param directory
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File child : directory.listFiles()) {
				if (child.isDirectory()) {
					deleteFilesByDirectory(child);
				} 
				child.delete();
			}
		}
	}
}
