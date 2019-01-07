package aiyzp.com.medialib;


import android.view.View;
import android.view.View.MeasureSpec;

import java.lang.ref.WeakReference;

public final class MeasureHelper {
    private WeakReference<View> mWeakView;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoSarNum;
    private int mVideoSarDen;
    private int mVideoRotationDegree;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mCurrentAspectRatio = 0;

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference(view);
    }

    public View getView() {
        return this.mWeakView == null ? null : (View)this.mWeakView.get();
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        this.mVideoSarNum = videoSarNum;
        this.mVideoSarDen = videoSarDen;
    }

    public void setVideoRotation(int videoRotationDegree) {
        this.mVideoRotationDegree = videoRotationDegree;
    }

    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
            width = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = width;
        }

        width = View.getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = View.getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mCurrentAspectRatio == 3) {
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        } else if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthSpecMode != -2147483648 && heightSpecMode != -2147483648) {
                if (widthSpecMode == 1073741824 && heightSpecMode == 1073741824) {
                    width = widthSpecSize;
                    height = heightSpecSize;
                    if (this.mVideoWidth * heightSpecSize < widthSpecSize * this.mVideoHeight) {
                        width = heightSpecSize * this.mVideoWidth / this.mVideoHeight;
                    } else if (this.mVideoWidth * heightSpecSize > widthSpecSize * this.mVideoHeight) {
                        height = widthSpecSize * this.mVideoHeight / this.mVideoWidth;
                    }
                } else if (widthSpecMode == 1073741824) {
                    width = widthSpecSize;
                    height = widthSpecSize * this.mVideoHeight / this.mVideoWidth;
                    if (heightSpecMode == -2147483648 && height > heightSpecSize) {
                        height = heightSpecSize;
                    }
                } else if (heightSpecMode == 1073741824) {
                    height = heightSpecSize;
                    width = heightSpecSize * this.mVideoWidth / this.mVideoHeight;
                    if (widthSpecMode == -2147483648 && width > widthSpecSize) {
                        width = widthSpecSize;
                    }
                } else {
                    width = this.mVideoWidth;
                    height = this.mVideoHeight;
                    if (heightSpecMode == -2147483648 && height > heightSpecSize) {
                        height = heightSpecSize;
                        width = heightSpecSize * this.mVideoWidth / this.mVideoHeight;
                    }

                    if (widthSpecMode == -2147483648 && width > widthSpecSize) {
                        width = widthSpecSize;
                        height = widthSpecSize * this.mVideoHeight / this.mVideoWidth;
                    }
                }
            } else {
                float specAspectRatio = (float)widthSpecSize / (float)heightSpecSize;
                float displayAspectRatio;
                switch(this.mCurrentAspectRatio) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    default:
                        displayAspectRatio = (float)this.mVideoWidth / (float)this.mVideoHeight;
                        if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                            displayAspectRatio = displayAspectRatio * (float)this.mVideoSarNum / (float)this.mVideoSarDen;
                        }
                        break;
                    case 4:
                        displayAspectRatio = 1.7777778F;
                        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                            displayAspectRatio = 1.0F / displayAspectRatio;
                        }
                        break;
                    case 5:
                        displayAspectRatio = 1.3333334F;
                        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                            displayAspectRatio = 1.0F / displayAspectRatio;
                        }
                }

                boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                switch(this.mCurrentAspectRatio) {
                    case 0:
                    case 4:
                    case 5:
                        if (shouldBeWider) {
                            width = widthSpecSize;
                            height = (int)((float)widthSpecSize / displayAspectRatio);
                        } else {
                            height = heightSpecSize;
                            width = (int)((float)heightSpecSize * displayAspectRatio);
                        }
                        break;
                    case 1:
                        if (shouldBeWider) {
                            height = heightSpecSize;
                            width = (int)((float)heightSpecSize * displayAspectRatio);
                        } else {
                            width = widthSpecSize;
                            height = (int)((float)widthSpecSize / displayAspectRatio);
                        }
                        break;
                    case 2:
                    case 3:
                    default:
                        if (shouldBeWider) {
                            width = Math.min(this.mVideoWidth, widthSpecSize);
                            height = (int)((float)width / displayAspectRatio);
                        } else {
                            height = Math.min(this.mVideoHeight, heightSpecSize);
                            width = (int)((float)height * displayAspectRatio);
                        }
                }
            }
        }

        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setAspectRatio(int aspectRatio) {
        this.mCurrentAspectRatio = aspectRatio;
    }
}
