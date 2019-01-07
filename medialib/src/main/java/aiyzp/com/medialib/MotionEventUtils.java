package aiyzp.com.medialib;

import android.graphics.PointF;
import android.view.MotionEvent;

public final class MotionEventUtils {
    public static final int FINGER_FLAG_1 = 601;
    public static final int FINGER_FLAG_2 = 602;
    public static final int FINGER_FLAG_3 = 603;

    private MotionEventUtils() {
        throw new AssertionError();
    }

    public static float calcSpacing(MotionEvent event, int index1, int index2) {
        float x = event.getX(index1) - event.getX(index2);
        float y = event.getY(index1) - event.getY(index2);
        return (float)Math.sqrt((double)(x * x + y * y));
    }

    public static float calcSpacing(MotionEvent event, int fingerFlag) {
        float x;
        float y;
        if (601 == fingerFlag) {
            x = (event.getX(0) + event.getX(1)) / 2.0F - event.getX(2);
            y = (event.getY(0) + event.getY(1)) / 2.0F - event.getY(2);
        } else if (602 == fingerFlag) {
            x = (event.getX(0) + event.getX(2)) / 2.0F - event.getX(1);
            y = (event.getY(0) + event.getY(2)) / 2.0F - event.getY(1);
        } else if (603 == fingerFlag) {
            x = (event.getX(2) + event.getX(1)) / 2.0F - event.getX(0);
            y = (event.getY(2) + event.getY(1)) / 2.0F - event.getY(0);
        } else {
            x = event.getX(0) - event.getX(1);
            y = event.getY(0) - event.getY(1);
        }

        return (float)Math.sqrt((double)(x * x + y * y));
    }

    public static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1) + event.getX(2);
        float y = event.getY(0) + event.getY(1) + event.getY(2);
        point.set(x / 3.0F, y / 3.0F);
    }

    public static int calcFingerFlag(MotionEvent event) {
        float space1 = calcSpacing(event, 0, 1);
        float space2 = calcSpacing(event, 0, 2);
        float space3 = calcSpacing(event, 2, 1);
        float minSpace = Math.min(space1, Math.min(space2, space3));
        if (minSpace == space1) {
            return 601;
        } else if (minSpace == space2) {
            return 602;
        } else {
            return minSpace == space3 ? 603 : -1;
        }
    }

    public static float rotation(MotionEvent event, int fingerFlag) {
        double delta_x;
        double delta_y;
        if (601 == fingerFlag) {
            delta_x = (double)((event.getX(0) + event.getX(1)) / 2.0F - event.getX(2));
            delta_y = (double)((event.getY(0) + event.getY(1)) / 2.0F - event.getY(2));
        } else if (602 == fingerFlag) {
            delta_x = (double)((event.getX(0) + event.getX(2)) / 2.0F - event.getX(1));
            delta_y = (double)((event.getY(0) + event.getY(2)) / 2.0F - event.getY(1));
        } else if (603 == fingerFlag) {
            delta_x = (double)((event.getX(2) + event.getX(1)) / 2.0F - event.getX(0));
            delta_y = (double)((event.getY(2) + event.getY(1)) / 2.0F - event.getY(0));
        } else {
            delta_x = (double)(event.getX(0) - event.getX(1));
            delta_y = (double)(event.getY(0) - event.getY(1));
        }

        double radians = Math.atan2(delta_y, delta_x);
        return (float)Math.toDegrees(radians);
    }
}
