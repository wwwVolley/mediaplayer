package aiyzp.com.medialib;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;

public final class AnimHelper {
    private AnimHelper() {
        throw new AssertionError();
    }

    public static void doSlideRightIn(View view, int startX, int endX, int duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", new float[]{(float)startX, (float)endX});
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0F, 1.0F});
        AnimatorSet set = new AnimatorSet();
        set.setDuration((long)duration);
        set.playTogether(new Animator[]{translationX, alpha});
        set.start();
    }

    public static void doClipViewWidth(final View view, int srcWidth, int endWidth, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{srcWidth, endWidth}).setDuration((long)duration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (Integer)valueAnimator.getAnimatedValue();
                LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = width;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    public static void doClipViewHeight(final View view, int srcHeight, int endHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{srcHeight, endHeight}).setDuration((long)duration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (Integer)valueAnimator.getAnimatedValue();
                LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = width;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }
}