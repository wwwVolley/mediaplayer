package aiyzp.com.medialib;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SurfaceRenderView extends SurfaceView implements IRenderView {
    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    public SurfaceRenderView(Context context) {
        super(context);
        this.initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    @TargetApi(21)
    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        this.mSurfaceCallback = new SurfaceCallback(this);
        this.getHolder().addCallback(this.mSurfaceCallback);
        this.getHolder().setType(0);
    }

    public View getView() {
        return this;
    }

    public boolean shouldWaitForResize() {
        return true;
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            this.getHolder().setFixedSize(videoWidth, videoHeight);
            this.requestLayout();
        }

    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            this.requestLayout();
        }

    }

    public void setVideoRotation(int degree) {
        Log.e("", "SurfaceView doesn't support rotation (" + degree + ")!\n");
    }

    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        this.requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public void addRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.addRenderCallback(callback);
    }

    public void removeRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.removeRenderCallback(callback);
    }

    public void setTransform(Matrix transform) {
    }

    public Matrix getTransform() {
        return null;
    }

    public Bitmap getVideoScreenshot() {
        return null;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(SurfaceRenderView.class.getName());
    }

    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (VERSION.SDK_INT >= 14) {
            info.setClassName(SurfaceRenderView.class.getName());
        }

    }

    private static final class SurfaceCallback implements Callback {
        private SurfaceHolder mSurfaceHolder;
        private boolean mIsFormatChanged;
        private int mFormat;
        private int mWidth;
        private int mHeight;
        private WeakReference<SurfaceRenderView> mWeakSurfaceView;
        private Map<IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();

        public SurfaceCallback(@NonNull SurfaceRenderView surfaceView) {
            this.mWeakSurfaceView = new WeakReference(surfaceView);
        }

        public void addRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.put(callback, callback);
            ISurfaceHolder surfaceHolder = null;
            if (this.mSurfaceHolder != null) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView)this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }

                callback.onSurfaceCreated(surfaceHolder, this.mWidth, this.mHeight);
            }

            if (this.mIsFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView)this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }

                callback.onSurfaceChanged(surfaceHolder, this.mFormat, this.mWidth, this.mHeight);
            }

        }

        public void removeRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.remove(callback);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView)this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            Iterator var3 = this.mRenderCallbackMap.keySet().iterator();

            while(var3.hasNext()) {
                IRenderCallback renderCallback = (IRenderCallback)var3.next();
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }

        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            this.mSurfaceHolder = null;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView)this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            Iterator var3 = this.mRenderCallbackMap.keySet().iterator();

            while(var3.hasNext()) {
                IRenderCallback renderCallback = (IRenderCallback)var3.next();
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = true;
            this.mFormat = format;
            this.mWidth = width;
            this.mHeight = height;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView)this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            Iterator var6 = this.mRenderCallbackMap.keySet().iterator();

            while(var6.hasNext()) {
                IRenderCallback renderCallback = (IRenderCallback)var6.next();
                renderCallback.onSurfaceChanged(surfaceHolder, format, width, height);
            }

        }
    }

    private static final class InternalSurfaceHolder implements ISurfaceHolder {
        private SurfaceRenderView mSurfaceView;
        private SurfaceHolder mSurfaceHolder;

        public InternalSurfaceHolder(@NonNull SurfaceRenderView surfaceView, @Nullable SurfaceHolder surfaceHolder) {
            this.mSurfaceView = surfaceView;
            this.mSurfaceHolder = surfaceHolder;
        }

        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                if (VERSION.SDK_INT >= 16 && mp instanceof ISurfaceTextureHolder) {
                    ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder)mp;
                    textureHolder.setSurfaceTexture((SurfaceTexture)null);
                }

                mp.setDisplay(this.mSurfaceHolder);
            }

        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mSurfaceView;
        }

        @Nullable
        public SurfaceHolder getSurfaceHolder() {
            return this.mSurfaceHolder;
        }

        @Nullable
        public SurfaceTexture getSurfaceTexture() {
            return null;
        }

        @Nullable
        public Surface openSurface() {
            return this.mSurfaceHolder == null ? null : this.mSurfaceHolder.getSurface();
        }
    }
}
