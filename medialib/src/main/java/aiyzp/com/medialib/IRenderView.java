package aiyzp.com.medialib;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IRenderView {
    int AR_ASPECT_FIT_PARENT = 0;
    int AR_ASPECT_FILL_PARENT = 1;
    int AR_ASPECT_WRAP_CONTENT = 2;
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4;
    int AR_4_3_FIT_PARENT = 5;

    View getView();

    boolean shouldWaitForResize();

    void setVideoSize(int var1, int var2);

    void setVideoSampleAspectRatio(int var1, int var2);

    void setVideoRotation(int var1);

    void setAspectRatio(int var1);

    void addRenderCallback(@NonNull IRenderCallback var1);

    void removeRenderCallback(@NonNull IRenderCallback var1);

    void setTransform(Matrix var1);

    Matrix getTransform();

    Bitmap getVideoScreenshot();

    public interface IRenderCallback {
        void onSurfaceCreated(@NonNull ISurfaceHolder var1, int var2, int var3);

        void onSurfaceChanged(@NonNull ISurfaceHolder var1, int var2, int var3, int var4);

        void onSurfaceDestroyed(@NonNull ISurfaceHolder var1);
    }

    public interface ISurfaceHolder {
        void bindToMediaPlayer(IMediaPlayer var1);

        @NonNull
        IRenderView getRenderView();

        @Nullable
        SurfaceHolder getSurfaceHolder();

        @Nullable
        Surface openSurface();

        @Nullable
        SurfaceTexture getSurfaceTexture();
    }
}
