package aiyzp.com.medialib;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public final class WindowUtils {
    private WindowUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static int getDisplayRotation(Activity activity) {
        switch(activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }

    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static void dimBackground(float from, float to, Activity context) {
        final Window window = context.getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{from, to});
        valueAnimator.setDuration(500L);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams params = window.getAttributes();
                params.alpha = (Float)animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }

    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        byte orientation;
        if ((rotation == 0 || rotation == 2) && height > width || (rotation == 1 || rotation == 3) && width > height) {
            switch(rotation) {
                case 0:
                    orientation = 1;
                    break;
                case 1:
                    orientation = 0;
                    break;
                case 2:
                    orientation = 9;
                    break;
                case 3:
                    orientation = 8;
                    break;
                default:
                    orientation = 1;
            }
        } else {
            switch(rotation) {
                case 0:
                    orientation = 0;
                    break;
                case 1:
                    orientation = 1;
                    break;
                case 2:
                    orientation = 8;
                    break;
                case 3:
                    orientation = 9;
                    break;
                default:
                    orientation = 0;
            }
        }

        return orientation;
    }
}