package aiyzp.com.medialib;

import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SDCardUtils {
    public SDCardUtils() {
    }

    public static String getState() {
        return Environment.getExternalStorageState();
    }

    public static boolean isAvailable() {
        return getState().equals("mounted");
    }

    public static File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }

    public static String getRootPath() {
        File rootDirectory = getRootDirectory();
        return rootDirectory != null ? rootDirectory.getPath() : null;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }

        return sdDir.toString();
    }

    public static long getFreeSpaceBytes(String path) {
        StatFs statFs = new StatFs(path);
        long freeSpaceBytes;
        if (VERSION.SDK_INT >= 18) {
            freeSpaceBytes = statFs.getAvailableBytes();
        } else {
            freeSpaceBytes = (long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize();
        }

        return freeSpaceBytes;
    }
}
