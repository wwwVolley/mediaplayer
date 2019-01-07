package aiyzp.com.medialib;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringUtils {
    private StringUtils() {
        throw new AssertionError();
    }

    public static String generateTime(long time) {
        int totalSeconds = (int)(time / 1000L);
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        return minutes > 99 ? String.format("%d:%02d", minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public static String getFormatSize(int size) {
        long fileSize = (long)size;
        String showSize = "";
        if (fileSize >= 0L && fileSize < 1024L) {
            showSize = fileSize + "Kb/s";
        } else if (fileSize >= 1024L && fileSize < 1048576L) {
            showSize = Long.toString(fileSize / 1024L) + "KB/s";
        } else if (fileSize >= 1048576L && fileSize < 1073741824L) {
            showSize = Long.toString(fileSize / 1048576L) + "MB/s";
        }

        return showSize;
    }

    public static String getCurFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}