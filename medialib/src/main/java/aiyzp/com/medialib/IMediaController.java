package aiyzp.com.medialib;

import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public interface IMediaController {
    void hide();

    boolean isShowing();

    void setAnchorView(View var1);

    void setEnabled(boolean var1);

    void setMediaPlayer(MediaPlayerControl var1);

    void show(int var1);

    void show();

    void showOnce(View var1);
}
