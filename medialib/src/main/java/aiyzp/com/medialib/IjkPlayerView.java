package aiyzp.com.medialib;


import aiyzp.com.medialib.R.id;
import aiyzp.com.medialib.R.layout;
import aiyzp.com.medialib.R.style;
import aiyzp.com.medialib.ShareDialog.OnDialogClickListener;
import aiyzp.com.medialib.ShareDialog.OnDialogDismissListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class IjkPlayerView extends FrameLayout implements OnClickListener {
    private static final int MAX_VIDEO_SEEK = 1000;
    private static final int DEFAULT_HIDE_TIMEOUT = 5000;
    private static final int MSG_UPDATE_SEEK = 10086;
    private static final int MSG_ENABLE_ORIENTATION = 10087;
    private static final int INVALID_VALUE = -1;
    private IjkVideoView mVideoView;
    public ImageView mPlayerThumb;
    private ProgressBar mLoadingView;
    private TextView mTvVolume;
    private TextView mTvBrightness;
    private TextView mTvFastForward;
    private FrameLayout mFlTouchLayout;
    private ImageView mIvBack;
    private MarqueeTextView mTvTitle;
    private LinearLayout mFullscreenTopBar;
    private ImageView mIvBackWindow;
    private FrameLayout mWindowTopBar;
    private ImageView mIvPlay;
    private ImageView mIvPlayCircle;
    private TextView mTvCurTime;
    private SeekBar mPlayerSeek;
    private TextView mTvEndTime;
    private ImageView mIvFullscreen;
    private LinearLayout mLlBottomBar;
    private FrameLayout mFlVideoBox;
    private ImageView mIvPlayerLock;
    private TextView mTvRecoverScreen;
    private TextView mTvSettings;
    private RadioGroup mAspectRatioOptions;
    private AppCompatActivity mAttachActivity;
    private Handler mHandler;
    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;
    private int mMaxVolume;
    private boolean mIsForbidTouch;
    private boolean mIsShowBar;
    private boolean mIsFullscreen;
    private boolean mIsPlayComplete;
    private boolean mIsSeeking;
    private long mTargetPosition;
    private int mCurPosition;
    private int mCurVolume;
    private float mCurBrightness;
    private int mInitHeight;
    private int mWidthPixels;
    private int mScreenUiVisibility;
    private OrientationEventListener mOrientationListener;
    private boolean mIsNeverPlay;
    private OnInfoListener mOutsideInfoListener;
    private boolean mIsForbidOrientation;
    private boolean mIsAlwaysFullScreen;
    private long mExitTime;
    private Matrix mVideoMatrix;
    private Matrix mSaveMatrix;
    private boolean mIsNeedRecoverScreen;
    private int mAspectOptionsHeight;
    private final OnSeekBarChangeListener mSeekListener;
    private Runnable mHideBarRunnable;
    private OnGestureListener mPlayerGestureListener;
    private Runnable mHideTouchViewRunnable;
    private OnTouchListener mPlayerTouchListener;
    private boolean mIsRenderingStart;
    private boolean mIsBufferingStart;
    private OnInfoListener mInfoListener;
    private static final int DEFAULT_QUALITY_TIME = 300;
    public static final int MEDIA_QUALITY_SMOOTH = 0;
    public static final int MEDIA_QUALITY_MEDIUM = 1;
    public static final int MEDIA_QUALITY_HIGH = 2;
    public static final int MEDIA_QUALITY_SUPER = 3;
    public static final int MEDIA_QUALITY_BD = 4;
    private static final int[] QUALITY_DRAWABLE_RES;
    private SparseArray<String> mVideoSource;
    private String[] mMediaQualityDesc;
    private View mFlMediaQuality;
    private TextView mIvMediaQuality;
    private ListView mLvMediaQuality;
    private AdapterMediaQuality mQualityAdapter;
    private List<MediaQualityInfo> mQualityData;
    private boolean mIsShowQuality;
    private int mCurSelectQuality;
    private ImageView mIvCancelSkip;
    private TextView mTvSkipTime;
    private TextView mTvDoSkip;
    private View mLlSkipLayout;
    private int mSkipPosition;
    private Runnable mHideSkipTipRunnable;
    private static final int NORMAL_STATUS = 501;
    private static final int INTERRUPT_WHEN_PLAY = 502;
    private static final int INTERRUPT_WHEN_PAUSE = 503;
    private int mVideoStatus;
    private static final int DANMAKU_TAG_BILI = 701;
    private static final int DANMAKU_TAG_ACFUN = 702;
    private static final int DANMAKU_TAG_CUSTOM = 703;
    private int mDanmakuTag;
    private IDanmakuView mDanmakuView;
    private ImageView mIvDanmakuControl;
    private TextView mTvOpenEditDanmaku;
    private SeekBar mDanmakuPlayerSeek;
    private TextView mTvTimeSeparator;
    private View mEditDanmakuLayout;
    private EditText mEtDanmakuContent;
    private ImageView mIvCancelSend;
    private ImageView mIvDoSend;
    private View mDanmakuOptionsBasic;
    private RadioGroup mDanmakuTextSizeOptions;
    private RadioGroup mDanmakuTypeOptions;
    private RadioButton mDanmakuCurColor;
    private ImageView mDanmakuMoreColorIcon;
    private View mDanmakuMoreOptions;
    private RadioGroup mDanmakuColorOptions;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mDanmakuParser;
    private ILoader mDanmakuLoader;
    private BaseDanmakuConverter mDanmakuConverter;
    private OnDanmakuListener mDanmakuListener;
    private boolean mIsEnableDanmaku;
    private int mDanmakuTextColor;
    private float mDanmakuTextSize;
    private int mDanmakuType;
    private int mBasicOptionsWidth;
    private int mMoreOptionsWidth;
    private long mDanmakuTargetPosition;
    private ProgressBar mPbBatteryLevel;
    private TextView mTvSystemTime;
    private ImageView mIvScreenshot;
    private BatteryBroadcastReceiver mBatteryReceiver;
    private ScreenBroadcastReceiver mScreenReceiver;
    private boolean mIsScreenLocked;
    private ShareDialog mShareDialog;
    private OnDialogClickListener mDialogClickListener;
    private OnDialogClickListener mInsideDialogClickListener;
    private OnDialogDismissListener mDialogDismissListener;
    private File mSaveDir;

    public IjkPlayerView(Context context) {
        this(context, (AttributeSet)null);
    }

    public IjkPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 10086) {
                    int pos = IjkPlayerView.this._setProgress();
                    if (!IjkPlayerView.this.mIsSeeking && IjkPlayerView.this.mIsShowBar && IjkPlayerView.this.mVideoView.isPlaying()) {
                        msg = this.obtainMessage(10086);
                        this.sendMessageDelayed(msg, (long)(1000 - pos % 1000));
                    }
                } else if (msg.what == 10087 && IjkPlayerView.this.mOrientationListener != null) {
                    IjkPlayerView.this.mOrientationListener.enable();
                }

            }
        };
        this.mIsForbidTouch = false;
        this.mIsShowBar = true;
        this.mIsPlayComplete = false;
        this.mTargetPosition = -1L;
        this.mCurPosition = -1;
        this.mCurVolume = -1;
        this.mCurBrightness = -1.0F;
        this.mIsNeverPlay = true;
        this.mIsForbidOrientation = true;
        this.mIsAlwaysFullScreen = false;
        this.mExitTime = 0L;
        this.mVideoMatrix = new Matrix();
        this.mSaveMatrix = new Matrix();
        this.mIsNeedRecoverScreen = false;
        this.mSeekListener = new OnSeekBarChangeListener() {
            private long curPosition;

            public void onStartTrackingTouch(SeekBar bar) {
                IjkPlayerView.this.mIsSeeking = true;
                IjkPlayerView.this._showControlBar(3600000);
                IjkPlayerView.this.mHandler.removeMessages(10086);
                this.curPosition = (long)IjkPlayerView.this.mVideoView.getCurrentPosition();
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if (fromUser) {
                    long duration = (long)IjkPlayerView.this.mVideoView.getDuration();
                    IjkPlayerView.this.mTargetPosition = duration * (long)progress / 1000L;
                    int deltaTime = (int)((IjkPlayerView.this.mTargetPosition - this.curPosition) / 1000L);
                    String desc;
                    if (IjkPlayerView.this.mTargetPosition > this.curPosition) {
                        desc = StringUtils.generateTime(IjkPlayerView.this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n+" + deltaTime + "秒";
                    } else {
                        desc = StringUtils.generateTime(IjkPlayerView.this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n" + deltaTime + "秒";
                    }

                    IjkPlayerView.this._setFastForward(desc);
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                IjkPlayerView.this._hideTouchView();
                IjkPlayerView.this.mIsSeeking = false;
                IjkPlayerView.this.seekTo((int)IjkPlayerView.this.mTargetPosition);
                IjkPlayerView.this.mTargetPosition = -1L;
                IjkPlayerView.this._setProgress();
                IjkPlayerView.this._showControlBar(5000);
            }
        };
        this.mHideBarRunnable = new Runnable() {
            public void run() {
                IjkPlayerView.this._hideAllView(false);
            }
        };
        this.mPlayerGestureListener = new SimpleOnGestureListener() {
            private boolean isDownTouch;
            private boolean isVolume;
            private boolean isLandscape;
            private boolean isRecoverFromDanmaku;

            public boolean onDown(MotionEvent e) {
                this.isDownTouch = true;
                this.isRecoverFromDanmaku = IjkPlayerView.this.recoverFromEditVideo();
                return super.onDown(e);
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!IjkPlayerView.this.mIsForbidTouch && !IjkPlayerView.this.mIsNeverPlay) {
                    float mOldX = e1.getX();
                    float mOldY = e1.getY();
                    float deltaY = mOldY - e2.getY();
                    float deltaX = mOldX - e2.getX();
                    if (this.isDownTouch) {
                        this.isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                        this.isVolume = mOldX > (float)IjkPlayerView.this.getResources().getDisplayMetrics().widthPixels * 0.5F;
                        this.isDownTouch = false;
                    }

                    if (this.isLandscape) {
                        IjkPlayerView.this._onProgressSlide(-deltaX / (float)IjkPlayerView.this.mVideoView.getWidth());
                    } else {
                        float percent = deltaY / (float)IjkPlayerView.this.mVideoView.getHeight();
                        if (this.isVolume) {
                            IjkPlayerView.this._onVolumeSlide(percent);
                        } else {
                            IjkPlayerView.this._onBrightnessSlide(percent);
                        }
                    }
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (this.isRecoverFromDanmaku) {
                    return true;
                } else {
                    if (IjkPlayerView.this.mIsShowQuality) {
                        IjkPlayerView.this._toggleMediaQuality();
                    } else {
                        IjkPlayerView.this._toggleControlBar();
                    }

                    return true;
                }
            }

            public boolean onDoubleTap(MotionEvent e) {
                if (!IjkPlayerView.this.mIsNeverPlay && !this.isRecoverFromDanmaku) {
                    if (!IjkPlayerView.this.mIsForbidTouch) {
                        IjkPlayerView.this._refreshHideRunnable();
                        IjkPlayerView.this._togglePlayStatus();
                    }

                    return true;
                } else {
                    return true;
                }
            }
        };
        this.mHideTouchViewRunnable = new Runnable() {
            public void run() {
                IjkPlayerView.this._hideTouchView();
            }
        };
        this.mPlayerTouchListener = new OnTouchListener() {
            private static final int NORMAL = 1;
            private static final int INVALID_POINTER = 2;
            private static final int ZOOM_AND_ROTATE = 3;
            private int mode = 1;
            private PointF midPoint = new PointF(0.0F, 0.0F);
            private float degree = 0.0F;
            private int fingerFlag = -1;
            private float oldDist;
            private float scale;

            public boolean onTouch(View v, MotionEvent event) {
                switch(MotionEventCompat.getActionMasked(event)) {
                    case 0:
                        this.mode = 1;
                        IjkPlayerView.this.mHandler.removeCallbacks(IjkPlayerView.this.mHideBarRunnable);
                    case 1:
                    case 3:
                    case 4:
                    default:
                        break;
                    case 2:
                        if (this.mode == 3) {
                            float newRotate = MotionEventUtils.rotation(event, this.fingerFlag);
                            IjkPlayerView.this.mVideoView.setVideoRotation((int)(newRotate - this.degree));
                            IjkPlayerView.this.mVideoMatrix.set(IjkPlayerView.this.mSaveMatrix);
                            float newDist = MotionEventUtils.calcSpacing(event, this.fingerFlag);
                            this.scale = newDist / this.oldDist;
                            IjkPlayerView.this.mVideoMatrix.postScale(this.scale, this.scale, this.midPoint.x, this.midPoint.y);
                            IjkPlayerView.this.mVideoView.setVideoTransform(IjkPlayerView.this.mVideoMatrix);
                        }
                        break;
                    case 5:
                        if (event.getPointerCount() == 3 && IjkPlayerView.this.mIsFullscreen) {
                            IjkPlayerView.this._hideTouchView();
                            this.mode = 3;
                            MotionEventUtils.midPoint(this.midPoint, event);
                            this.fingerFlag = MotionEventUtils.calcFingerFlag(event);
                            this.degree = MotionEventUtils.rotation(event, this.fingerFlag);
                            this.oldDist = MotionEventUtils.calcSpacing(event, this.fingerFlag);
                            IjkPlayerView.this.mSaveMatrix = IjkPlayerView.this.mVideoView.getVideoTransform();
                        } else {
                            this.mode = 2;
                        }
                        break;
                    case 6:
                        if (this.mode == 3) {
                            IjkPlayerView.this.mIsNeedRecoverScreen = IjkPlayerView.this.mVideoView.adjustVideoView(this.scale);
                            if (IjkPlayerView.this.mIsNeedRecoverScreen && IjkPlayerView.this.mIsShowBar) {
                                IjkPlayerView.this.mTvRecoverScreen.setVisibility(0);
                            }
                        }

                        this.mode = 2;
                }

                if (this.mode == 1) {
                    if (IjkPlayerView.this.mGestureDetector.onTouchEvent(event)) {
                        return true;
                    }

                    if (MotionEventCompat.getActionMasked(event) == 1) {
                        IjkPlayerView.this._endGesture();
                    }
                }

                return false;
            }
        };
        this.mIsRenderingStart = false;
        this.mIsBufferingStart = false;
        this.mInfoListener = new OnInfoListener() {
            public boolean onInfo(IMediaPlayer iMediaPlayer, int status, int extra) {
                IjkPlayerView.this._switchStatus(status);
                if (IjkPlayerView.this.mOutsideInfoListener != null) {
                    IjkPlayerView.this.mOutsideInfoListener.onInfo(iMediaPlayer, status, extra);
                }

                return true;
            }
        };
        this.mVideoSource = new SparseArray();
        this.mIsShowQuality = false;
        this.mCurSelectQuality = 0;
        this.mSkipPosition = -1;
        this.mHideSkipTipRunnable = new Runnable() {
            public void run() {
                IjkPlayerView.this._hideSkipTip();
            }
        };
        this.mVideoStatus = 501;
        this.mDanmakuTag = 701;
        this.mIsEnableDanmaku = false;
        this.mDanmakuTextColor = -1;
        this.mDanmakuTextSize = -1.0F;
        this.mDanmakuType = 1;
        this.mBasicOptionsWidth = -1;
        this.mMoreOptionsWidth = -1;
        this.mDanmakuTargetPosition = -1L;
        this.mIsScreenLocked = false;
        this.mInsideDialogClickListener = new OnDialogClickListener() {
            public void onShare(Bitmap bitmap, Uri uri) {
                if (IjkPlayerView.this.mDialogClickListener != null) {
                    IjkPlayerView.this.mDialogClickListener.onShare(bitmap, IjkPlayerView.this.mVideoView.getUri());
                }

                File file = new File(IjkPlayerView.this.mSaveDir, System.currentTimeMillis() + ".jpg");

                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    Toast.makeText(IjkPlayerView.this.mAttachActivity, "保存成功，路径为:" + file.getAbsolutePath(), 0).show();
                } catch (IOException var5) {
                    Toast.makeText(IjkPlayerView.this.mAttachActivity, "保存本地失败", 0).show();
                }

            }
        };
        this.mDialogDismissListener = new OnDialogDismissListener() {
            public void onDismiss() {
                IjkPlayerView.this.recoverFromEditVideo();
            }
        };
        this._initView(context);
    }

    private void _initView(Context context) {
        if (context instanceof AppCompatActivity) {
            this.mAttachActivity = (AppCompatActivity)context;
            View.inflate(context, layout.layout_player_view, this);
            this.mVideoView = (IjkVideoView)this.findViewById(id.video_view);
            this.mPlayerThumb = (ImageView)this.findViewById(id.iv_thumb);
            this.mLoadingView = (ProgressBar)this.findViewById(id.pb_loading);
            this.mTvVolume = (TextView)this.findViewById(id.tv_volume);
            this.mTvBrightness = (TextView)this.findViewById(id.tv_brightness);
            this.mTvFastForward = (TextView)this.findViewById(id.tv_fast_forward);
            this.mFlTouchLayout = (FrameLayout)this.findViewById(id.fl_touch_layout);
            this.mIvBack = (ImageView)this.findViewById(id.iv_back);
            this.mTvTitle = (MarqueeTextView)this.findViewById(id.tv_title);
            this.mFullscreenTopBar = (LinearLayout)this.findViewById(id.fullscreen_top_bar);
            this.mIvBackWindow = (ImageView)this.findViewById(id.iv_back_window);
            this.mWindowTopBar = (FrameLayout)this.findViewById(id.window_top_bar);
            this.mIvPlay = (ImageView)this.findViewById(id.iv_play);
            this.mTvCurTime = (TextView)this.findViewById(id.tv_cur_time);
            this.mPlayerSeek = (SeekBar)this.findViewById(id.player_seek);
            this.mTvEndTime = (TextView)this.findViewById(id.tv_end_time);
            this.mIvFullscreen = (ImageView)this.findViewById(id.iv_fullscreen);
            this.mLlBottomBar = (LinearLayout)this.findViewById(id.ll_bottom_bar);
            this.mFlVideoBox = (FrameLayout)this.findViewById(id.fl_video_box);
            this.mIvPlayerLock = (ImageView)this.findViewById(id.iv_player_lock);
            this.mIvPlayCircle = (ImageView)this.findViewById(id.iv_play_circle);
            this.mTvRecoverScreen = (TextView)this.findViewById(id.tv_recover_screen);
            this.mTvSettings = (TextView)this.findViewById(id.tv_settings);
            this.mAspectRatioOptions = (RadioGroup)this.findViewById(id.aspect_ratio_group);
            this.mAspectOptionsHeight = this.getResources().getDimensionPixelSize(aiyzp.com.medialib.R.dimen.aspect_btn_size) * 4;
            this.mAspectRatioOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == id.aspect_fit_parent) {
                        IjkPlayerView.this.mVideoView.setAspectRatio(0);
                    } else if (checkedId == id.aspect_fit_screen) {
                        IjkPlayerView.this.mVideoView.setAspectRatio(1);
                    } else if (checkedId == id.aspect_16_and_9) {
                        IjkPlayerView.this.mVideoView.setAspectRatio(4);
                    } else if (checkedId == id.aspect_4_and_3) {
                        IjkPlayerView.this.mVideoView.setAspectRatio(5);
                    }

                    AnimHelper.doClipViewHeight(IjkPlayerView.this.mAspectRatioOptions, IjkPlayerView.this.mAspectOptionsHeight, 0, 150);
                }
            });
            this._initMediaQuality();
            this._initVideoSkip();
            this._initReceiver();
            this.mIvPlay.setOnClickListener(this);
            this.mIvBack.setOnClickListener(this);
            this.mIvFullscreen.setOnClickListener(this);
            this.mIvBackWindow.setOnClickListener(this);
            this.mIvPlayerLock.setOnClickListener(this);
            this.mIvPlayCircle.setOnClickListener(this);
            this.mTvRecoverScreen.setOnClickListener(this);
            this.mTvSettings.setOnClickListener(this);
        } else {
            throw new IllegalArgumentException("Context must be AppCompatActivity");
        }
    }

    private void _initMediaPlayer() {
        IjkMediaPlayer.loadLibrariesOnce((IjkLibLoader)null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.mAudioManager = (AudioManager)this.mAttachActivity.getSystemService("audio");
        this.mMaxVolume = this.mAudioManager.getStreamMaxVolume(3);

        try {
            int e = android.provider.Settings.System.getInt(this.mAttachActivity.getContentResolver(), "screen_brightness");
            float progress = 1.0F * (float)e / 255.0F;
            WindowManager.LayoutParams layout = this.mAttachActivity.getWindow().getAttributes();
            layout.screenBrightness = progress;
            this.mAttachActivity.getWindow().setAttributes(layout);
        } catch (SettingNotFoundException var4) {
            var4.printStackTrace();
        }

        this.mPlayerSeek.setMax(1000);
        this.mPlayerSeek.setOnSeekBarChangeListener(this.mSeekListener);
        this.mVideoView.setOnInfoListener(this.mInfoListener);
        this.mGestureDetector = new GestureDetector(this.mAttachActivity, this.mPlayerGestureListener);
        this.mFlVideoBox.setClickable(true);
        this.mFlVideoBox.setOnTouchListener(this.mPlayerTouchListener);
        this.mOrientationListener = new OrientationEventListener(this.mAttachActivity) {
            public void onOrientationChanged(int orientation) {
                IjkPlayerView.this._handleOrientation(orientation);
            }
        };
        if (this.mIsForbidOrientation) {
            this.mOrientationListener.disable();
        }

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mInitHeight == 0) {
            this.mInitHeight = this.getHeight();
            this.mWidthPixels = this.getResources().getDisplayMetrics().widthPixels;
        }

    }

    public void onResume() {
        Log.i("TTAG", "onResume");
        if (this.mIsScreenLocked) {
            this.mVideoView.setRender(2);
            this.mIsScreenLocked = false;
        }

        this.mVideoView.resume();
        if (!this.mIsForbidTouch && !this.mIsForbidOrientation) {
            this.mOrientationListener.enable();
        }

        if (this.mCurPosition != -1) {
            this.seekTo(this.mCurPosition);
            this.mCurPosition = -1;
        }

    }

    public void onPause() {
        Log.i("TTAG", "onPause");
        this.mCurPosition = this.mVideoView.getCurrentPosition();
        this.mVideoView.pause();
        this.mIvPlay.setSelected(false);
        this.mOrientationListener.disable();
        this._pauseDanmaku();
    }

    public int onDestroy() {
        int curPosition = this.mVideoView.getCurrentPosition();
        this.mVideoView.destroy();
        IjkMediaPlayer.native_profileEnd();
        if (this.mDanmakuView != null) {
            this.mDanmakuView.release();
            this.mDanmakuView = null;
        }

        if (this.mShareDialog != null) {
            this.mShareDialog.dismiss();
            this.mShareDialog = null;
        }

        this.mAttachActivity.unregisterReceiver(this.mBatteryReceiver);
        this.mAttachActivity.unregisterReceiver(this.mScreenReceiver);
        this.mAttachActivity.getWindow().clearFlags(128);
        return curPosition;
    }

    public boolean handleVolumeKey(int keyCode) {
        if (keyCode == 24) {
            this._setVolume(true);
            return true;
        } else if (keyCode == 25) {
            this._setVolume(false);
            return true;
        } else {
            return false;
        }
    }

    public boolean onBackPressed() {
        if (this.recoverFromEditVideo()) {
            return true;
        } else if (this.mIsAlwaysFullScreen) {
            this._exit();
            return true;
        } else if (this.mIsFullscreen) {
            this.mAttachActivity.setRequestedOrientation(1);
            if (this.mIsForbidTouch) {
                this.mIsForbidTouch = false;
                this.mIvPlayerLock.setSelected(false);
                this._setControlBarVisible(this.mIsShowBar);
            }

            return true;
        } else {
            return false;
        }
    }

    public IjkPlayerView init() {
        this._initMediaPlayer();
        return this;
    }

    public IjkPlayerView setVideoPath(String url) {
        return this.setVideoPath(Uri.parse(url));
    }

    public IjkPlayerView setVideoPath(Uri uri) {
        this.mVideoView.setVideoURI(uri);
        if (this.mCurPosition != -1) {
            this.seekTo(this.mCurPosition);
            this.mCurPosition = -1;
        } else {
            this.seekTo(0);
        }

        return this;
    }

    public IjkPlayerView setTitle(String title) {
        this.mTvTitle.setText(title);
        return this;
    }

    public IjkPlayerView alwaysFullScreen() {
        this.mIsAlwaysFullScreen = true;
        this._setFullScreen(true);
        this.mIvFullscreen.setVisibility(8);
        this.mAttachActivity.setRequestedOrientation(0);
        this._setUiLayoutFullscreen();
        return this;
    }

    public void start() {
        if (this.mIsPlayComplete) {
            if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
                this.mDanmakuView.seekTo(0L);
                this.mDanmakuView.pause();
            }

            this.mIsPlayComplete = false;
        }

        if (!this.mVideoView.isPlaying()) {
            this.mIvPlay.setSelected(true);
            this.mVideoView.start();
            this.mHandler.sendEmptyMessage(10086);
        }

        if (this.mIsNeverPlay) {
            this.mIsNeverPlay = false;
            this.mIvPlayCircle.setVisibility(8);
            this.mLoadingView.setVisibility(0);
            this.mIsShowBar = false;
            this._loadDanmaku();
        }

        this.mAttachActivity.getWindow().addFlags(128);
    }

    public boolean isPlaying() {
        return this.mVideoView.isPlaying();
    }

    public void pause() {
        this.mIvPlay.setSelected(false);
        if (this.mVideoView.isPlaying()) {
            this.mVideoView.pause();
        }

        this._pauseDanmaku();
        this.mAttachActivity.getWindow().clearFlags(128);
    }

    public void seekTo(int position) {
        this.mVideoView.seekTo(position);
        this.mDanmakuTargetPosition = (long)position;
    }

    public void stop() {
        this.pause();
        this.mVideoView.stopPlayback();
    }

    public void reset() {
    }

    private void _hideAllView(boolean isTouchLock) {
        this.mFlTouchLayout.setVisibility(8);
        this.mFullscreenTopBar.setVisibility(8);
        this.mWindowTopBar.setVisibility(8);
        this.mLlBottomBar.setVisibility(8);
        this._showAspectRatioOptions(false);
        if (!isTouchLock) {
            this.mIvPlayerLock.setVisibility(8);
            this.mIsShowBar = false;
        }

        if (this.mIsEnableDanmaku) {
            this.mDanmakuPlayerSeek.setVisibility(8);
        }

        if (this.mIsNeedRecoverScreen) {
            this.mTvRecoverScreen.setVisibility(8);
        }

    }

    private void _setControlBarVisible(boolean isShowBar) {
        if (this.mIsNeverPlay) {
            this.mIvPlayCircle.setVisibility(isShowBar ? 0 : 8);
        } else if (this.mIsForbidTouch) {
            this.mIvPlayerLock.setVisibility(isShowBar ? 0 : 8);
        } else {
            this.mLlBottomBar.setVisibility(isShowBar ? 0 : 8);
            if (!isShowBar) {
                this._showAspectRatioOptions(false);
            }

            if (this.mIsFullscreen) {
                this.mTvSystemTime.setText(StringUtils.getCurFormatTime());
                this.mFullscreenTopBar.setVisibility(isShowBar ? 0 : 8);
                this.mWindowTopBar.setVisibility(8);
                this.mIvPlayerLock.setVisibility(isShowBar ? 0 : 8);
                if (this.mIsEnableDanmaku) {
                    this.mDanmakuPlayerSeek.setVisibility(isShowBar ? 0 : 8);
                }

                if (this.mIsNeedRecoverScreen) {
                    this.mTvRecoverScreen.setVisibility(isShowBar ? 0 : 8);
                }
            } else {
                this.mWindowTopBar.setVisibility(isShowBar ? 0 : 8);
                this.mFullscreenTopBar.setVisibility(8);
                this.mIvPlayerLock.setVisibility(8);
                if (this.mIsEnableDanmaku) {
                    this.mDanmakuPlayerSeek.setVisibility(8);
                }

                if (this.mIsNeedRecoverScreen) {
                    this.mTvRecoverScreen.setVisibility(8);
                }
            }
        }

    }

    private void _toggleControlBar() {
        this.mIsShowBar = !this.mIsShowBar;
        this._setControlBarVisible(this.mIsShowBar);
        if (this.mIsShowBar) {
            this.mHandler.postDelayed(this.mHideBarRunnable, 5000L);
            this.mHandler.sendEmptyMessage(10086);
        }

    }

    private void _showControlBar(int timeout) {
        if (!this.mIsShowBar) {
            this._setProgress();
            this.mIsShowBar = true;
        }

        this._setControlBarVisible(true);
        this.mHandler.sendEmptyMessage(10086);
        this.mHandler.removeCallbacks(this.mHideBarRunnable);
        if (timeout != 0) {
            this.mHandler.postDelayed(this.mHideBarRunnable, (long)timeout);
        }

    }

    private void _togglePlayStatus() {
        if (this.mVideoView.isPlaying()) {
            this.pause();
        } else {
            this.start();
        }

    }

    private void _refreshHideRunnable() {
        this.mHandler.removeCallbacks(this.mHideBarRunnable);
        this.mHandler.postDelayed(this.mHideBarRunnable, 5000L);
    }

    private void _togglePlayerLock() {
        this.mIsForbidTouch = !this.mIsForbidTouch;
        this.mIvPlayerLock.setSelected(this.mIsForbidTouch);
        if (this.mIsForbidTouch) {
            this.mOrientationListener.disable();
            this._hideAllView(true);
        } else {
            if (!this.mIsForbidOrientation) {
                this.mOrientationListener.enable();
            }

            this.mFullscreenTopBar.setVisibility(0);
            this.mLlBottomBar.setVisibility(0);
            if (this.mIsEnableDanmaku) {
                this.mDanmakuPlayerSeek.setVisibility(0);
            }

            if (this.mIsNeedRecoverScreen) {
                this.mTvRecoverScreen.setVisibility(0);
            }
        }

    }

    private void _toggleMediaQuality() {
        if (this.mFlMediaQuality.getVisibility() == 8) {
            this.mFlMediaQuality.setVisibility(0);
        }

        if (this.mIsShowQuality) {
            ViewCompat.animate(this.mFlMediaQuality).translationX((float)this.mFlMediaQuality.getWidth()).setDuration(300L);
            this.mIsShowQuality = false;
        } else {
            ViewCompat.animate(this.mFlMediaQuality).translationX(0.0F).setDuration(300L);
            this.mIsShowQuality = true;
        }

    }

    private void _showAspectRatioOptions(boolean isShow) {
        if (isShow) {
            AnimHelper.doClipViewHeight(this.mAspectRatioOptions, 0, this.mAspectOptionsHeight, 150);
        } else {
            android.view.ViewGroup.LayoutParams layoutParams = this.mAspectRatioOptions.getLayoutParams();
            layoutParams.height = 0;
        }

    }

    public void onClick(View v) {
        this._refreshHideRunnable();
        int id = v.getId();
        if (id == aiyzp.com.medialib.R.id.iv_back) {
            if (this.mIsAlwaysFullScreen) {
                this._exit();
                return;
            }

            this.mAttachActivity.setRequestedOrientation(1);
        } else if (id == aiyzp.com.medialib.R.id.iv_back_window) {
            this.mAttachActivity.finish();
        } else if (id != aiyzp.com.medialib.R.id.iv_play && id != aiyzp.com.medialib.R.id.iv_play_circle) {
            if (id == aiyzp.com.medialib.R.id.iv_fullscreen) {
                this._toggleFullScreen();
            } else if (id == aiyzp.com.medialib.R.id.iv_player_lock) {
                this._togglePlayerLock();
            } else if (id == aiyzp.com.medialib.R.id.iv_media_quality) {
                if (!this.mIsShowQuality) {
                    this._toggleMediaQuality();
                }
            } else if (id == aiyzp.com.medialib.R.id.iv_cancel_skip) {
                this.mHandler.removeCallbacks(this.mHideSkipTipRunnable);
                this._hideSkipTip();
            } else if (id == aiyzp.com.medialib.R.id.tv_do_skip) {
                this.mLoadingView.setVisibility(0);
                this.seekTo(this.mSkipPosition);
                this.mHandler.removeCallbacks(this.mHideSkipTipRunnable);
                this._hideSkipTip();
                this._setProgress();
            } else if (id == aiyzp.com.medialib.R.id.iv_danmaku_control) {
                this._toggleDanmakuShow();
            } else if (id == aiyzp.com.medialib.R.id.tv_open_edit_danmaku) {
                if (this.mDanmakuListener == null || this.mDanmakuListener.isValid()) {
                    this.editVideo();
                    this.mEditDanmakuLayout.setVisibility(0);
                    SoftInputUtils.setEditFocusable(this.mAttachActivity, this.mEtDanmakuContent);
                }
            } else if (id == aiyzp.com.medialib.R.id.iv_cancel_send) {
                this.recoverFromEditVideo();
            } else if (id == aiyzp.com.medialib.R.id.iv_do_send) {
                this.recoverFromEditVideo();
                this.sendDanmaku(this.mEtDanmakuContent.getText().toString(), false);
                this.mEtDanmakuContent.setText("");
            } else if (id == aiyzp.com.medialib.R.id.input_options_more) {
                this._toggleMoreColorOptions();
            } else if (id == aiyzp.com.medialib.R.id.iv_screenshot) {
                this._doScreenshot();
            } else if (id == aiyzp.com.medialib.R.id.tv_recover_screen) {
                this.mVideoView.resetVideoView(true);
                this.mIsNeedRecoverScreen = false;
                this.mTvRecoverScreen.setVisibility(8);
            } else if (id == aiyzp.com.medialib.R.id.tv_settings) {
                this._showAspectRatioOptions(true);
            }
        } else {
            this._togglePlayStatus();
        }

    }

    public IjkPlayerView enableOrientation() {
        this.mIsForbidOrientation = false;
        this.mOrientationListener.enable();
        return this;
    }

    private void _toggleFullScreen() {
        if (WindowUtils.getScreenOrientation(this.mAttachActivity) == 0) {
            this.mAttachActivity.setRequestedOrientation(1);
        } else {
            this.mAttachActivity.setRequestedOrientation(0);
        }

    }

    private void _setFullScreen(boolean isFullscreen) {
        this.mIsFullscreen = isFullscreen;
        this._toggleDanmakuView(isFullscreen);
        this._handleActionBar(isFullscreen);
        this._changeHeight(isFullscreen);
        this.mIvFullscreen.setSelected(isFullscreen);
        this.mHandler.post(this.mHideBarRunnable);
        this.mIvMediaQuality.setVisibility(isFullscreen ? 0 : 8);
        this.mLlBottomBar.setBackgroundResource(isFullscreen ? aiyzp.com.medialib.R.color.bg_video_view : 17170445);
        if (this.mIsShowQuality && !isFullscreen) {
            this._toggleMediaQuality();
        }

        if (this.mIsNeedRecoverScreen) {
            if (isFullscreen) {
                this.mVideoView.adjustVideoView(1.0F);
                this.mTvRecoverScreen.setVisibility(this.mIsShowBar ? 0 : 8);
            } else {
                this.mVideoView.resetVideoView(false);
                this.mTvRecoverScreen.setVisibility(8);
            }
        }

        if (!isFullscreen) {
            this._showAspectRatioOptions(false);
        }

    }

    private void _handleOrientation(int orientation) {
        if (!this.mIsNeverPlay) {
            if (this.mIsFullscreen && !this.mIsAlwaysFullScreen) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330) {
                    this.mAttachActivity.setRequestedOrientation(1);
                }
            } else if (orientation >= 60 && orientation <= 120) {
                this.mAttachActivity.setRequestedOrientation(8);
            } else if (orientation >= 240 && orientation <= 300) {
                this.mAttachActivity.setRequestedOrientation(0);
            }

        }
    }

    private void _refreshOrientationEnable() {
        if (!this.mIsForbidOrientation) {
            this.mOrientationListener.disable();
            this.mHandler.removeMessages(10087);
            this.mHandler.sendEmptyMessageDelayed(10087, 3000L);
        }

    }

    private void _handleActionBar(boolean isFullscreen) {
        ActionBar supportActionBar = this.mAttachActivity.getSupportActionBar();
        if (supportActionBar != null) {
            if (isFullscreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
        }

    }

    private void _changeHeight(boolean isFullscreen) {
        if (!this.mIsAlwaysFullScreen) {
            android.view.ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            if (isFullscreen) {
                layoutParams.height = this.mWidthPixels;
            } else {
                layoutParams.height = this.mInitHeight;
            }

            this.setLayoutParams(layoutParams);
        }
    }

    private void _setUiLayoutFullscreen() {
        if (VERSION.SDK_INT >= 14) {
            View decorView = this.mAttachActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(5894);
            this.mAttachActivity.getWindow().addFlags(1024);
        }

    }

    public void configurationChanged(Configuration newConfig) {
        this._refreshOrientationEnable();
        if (VERSION.SDK_INT >= 14) {
            View decorView;
            if (newConfig.orientation == 2) {
                decorView = this.mAttachActivity.getWindow().getDecorView();
                this.mScreenUiVisibility = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(5894);
                this._setFullScreen(true);
                this.mAttachActivity.getWindow().addFlags(1024);
            } else if (newConfig.orientation == 1) {
                decorView = this.mAttachActivity.getWindow().getDecorView();
                decorView.setSystemUiVisibility(this.mScreenUiVisibility);
                this._setFullScreen(false);
                this.mAttachActivity.getWindow().clearFlags(1024);
            }
        }

    }

    private void _exit() {
        if (System.currentTimeMillis() - this.mExitTime > 2000L) {
            Toast.makeText(this.mAttachActivity, "再按一次退出", 0).show();
            this.mExitTime = System.currentTimeMillis();
        } else {
            this.mAttachActivity.finish();
        }

    }

    private int _setProgress() {
        if (this.mVideoView != null && !this.mIsSeeking) {
            int position = this.mVideoView.getCurrentPosition();
            int duration = this.mVideoView.getDuration();
            if (duration > 0) {
                long pos = 1000L * (long)position / (long)duration;
                this.mPlayerSeek.setProgress((int)pos);
                if (this.mIsEnableDanmaku) {
                    this.mDanmakuPlayerSeek.setProgress((int)pos);
                }
            }

            int percent = this.mVideoView.getBufferPercentage();
            this.mPlayerSeek.setSecondaryProgress(percent * 10);
            if (this.mIsEnableDanmaku) {
                this.mDanmakuPlayerSeek.setSecondaryProgress(percent * 10);
            }

            this.mTvEndTime.setText(StringUtils.generateTime((long)duration));
            this.mTvCurTime.setText(StringUtils.generateTime((long)position));
            return position;
        } else {
            return 0;
        }
    }

    private void _setFastForward(String time) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvFastForward.getVisibility() == 8) {
            this.mTvFastForward.setVisibility(0);
        }

        this.mTvFastForward.setText(time);
    }

    private void _hideTouchView() {
        if (this.mFlTouchLayout.getVisibility() == 0) {
            this.mTvFastForward.setVisibility(8);
            this.mTvVolume.setVisibility(8);
            this.mTvBrightness.setVisibility(8);
            this.mFlTouchLayout.setVisibility(8);
        }

    }

    private void _onProgressSlide(float percent) {
        int position = this.mVideoView.getCurrentPosition();
        long duration = (long)this.mVideoView.getDuration();
        long deltaMax = Math.min(100000L, duration / 2L);
        long delta = (long)((float)deltaMax * percent);
        this.mTargetPosition = delta + (long)position;
        if (this.mTargetPosition > duration) {
            this.mTargetPosition = duration;
        } else if (this.mTargetPosition <= 0L) {
            this.mTargetPosition = 0L;
        }

        int deltaTime = (int)((this.mTargetPosition - (long)position) / 1000L);
        String desc;
        if (this.mTargetPosition > (long)position) {
            desc = StringUtils.generateTime(this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n+" + deltaTime + "秒";
        } else {
            desc = StringUtils.generateTime(this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n" + deltaTime + "秒";
        }

        this._setFastForward(desc);
    }

    private void _setVolumeInfo(int volume) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvVolume.getVisibility() == 8) {
            this.mTvVolume.setVisibility(0);
        }

        this.mTvVolume.setText(volume * 100 / this.mMaxVolume + "%");
    }

    private void _onVolumeSlide(float percent) {
        if (this.mCurVolume == -1) {
            this.mCurVolume = this.mAudioManager.getStreamVolume(3);
            if (this.mCurVolume < 0) {
                this.mCurVolume = 0;
            }
        }

        int index = (int)(percent * (float)this.mMaxVolume) + this.mCurVolume;
        if (index > this.mMaxVolume) {
            index = this.mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }

        this.mAudioManager.setStreamVolume(3, index, 0);
        this._setVolumeInfo(index);
    }

    private void _setVolume(boolean isIncrease) {
        int curVolume = this.mAudioManager.getStreamVolume(3);
        if (isIncrease) {
            curVolume += this.mMaxVolume / 15;
        } else {
            curVolume -= this.mMaxVolume / 15;
        }

        if (curVolume > this.mMaxVolume) {
            curVolume = this.mMaxVolume;
        } else if (curVolume < 0) {
            curVolume = 0;
        }

        this.mAudioManager.setStreamVolume(3, curVolume, 0);
        this._setVolumeInfo(curVolume);
        this.mHandler.removeCallbacks(this.mHideTouchViewRunnable);
        this.mHandler.postDelayed(this.mHideTouchViewRunnable, 1000L);
    }

    private void _setBrightnessInfo(float brightness) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvBrightness.getVisibility() == 8) {
            this.mTvBrightness.setVisibility(0);
        }

        this.mTvBrightness.setText(Math.ceil((double)(brightness * 100.0F)) + "%");
    }

    private void _onBrightnessSlide(float percent) {
        if (this.mCurBrightness < 0.0F) {
            this.mCurBrightness = this.mAttachActivity.getWindow().getAttributes().screenBrightness;
            if (this.mCurBrightness < 0.0F) {
                this.mCurBrightness = 0.5F;
            } else if (this.mCurBrightness < 0.01F) {
                this.mCurBrightness = 0.01F;
            }
        }

        WindowManager.LayoutParams attributes = this.mAttachActivity.getWindow().getAttributes();
        attributes.screenBrightness = this.mCurBrightness + percent;
        if (attributes.screenBrightness > 1.0F) {
            attributes.screenBrightness = 1.0F;
        } else if (attributes.screenBrightness < 0.01F) {
            attributes.screenBrightness = 0.01F;
        }

        this._setBrightnessInfo(attributes.screenBrightness);
        this.mAttachActivity.getWindow().setAttributes(attributes);
    }

    private void _endGesture() {
        if (this.mTargetPosition >= 0L && this.mTargetPosition != (long)this.mVideoView.getCurrentPosition()) {
            this.seekTo((int)this.mTargetPosition);
            this.mPlayerSeek.setProgress((int)(this.mTargetPosition * 1000L / (long)this.mVideoView.getDuration()));
            if (this.mIsEnableDanmaku) {
                this.mDanmakuPlayerSeek.setProgress((int)(this.mTargetPosition * 1000L / (long)this.mVideoView.getDuration()));
            }

            this.mTargetPosition = -1L;
        }

        this._hideTouchView();
        this._refreshHideRunnable();
        this.mCurVolume = -1;
        this.mCurBrightness = -1.0F;
    }

    private void _switchStatus(int status) {
        Log.d("TTAG", "status " + status);
        switch(status) {
            case 3:
                this.mIsRenderingStart = true;
            case 702:
                this.mIsBufferingStart = false;
                this.mLoadingView.setVisibility(8);
                this.mPlayerThumb.setVisibility(8);
                this.mHandler.sendEmptyMessage(10086);
                if (this.mSkipPosition != -1) {
                    this._showSkipTip();
                }

                if (this.mVideoView.isPlaying()) {
                    this._resumeDanmaku();
                }
                break;
            case 331:
                this._pauseDanmaku();
            case 332:
            default:
                break;
            case 334:
                if (this.mIsRenderingStart && !this.mIsBufferingStart) {
                    this._resumeDanmaku();
                }
                break;
            case 336:
                this.pause();
                this.mIsPlayComplete = true;
                break;
            case 701:
                this.mIsBufferingStart = true;
                this._pauseDanmaku();
                if (!this.mIsNeverPlay) {
                    this.mLoadingView.setVisibility(0);
                }
        }

    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mVideoView.setOnPreparedListener(l);
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mVideoView.setOnCompletionListener(l);
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mVideoView.setOnErrorListener(l);
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOutsideInfoListener = l;
    }

    public void setDanmakuListener(OnDanmakuListener danmakuListener) {
        this.mDanmakuListener = danmakuListener;
    }

    private void _initMediaQuality() {
        this.mMediaQualityDesc = this.getResources().getStringArray(R.array.media_quality);
        this.mFlMediaQuality = this.findViewById(id.fl_media_quality);
        this.mIvMediaQuality = (TextView)this.findViewById(id.iv_media_quality);
        this.mIvMediaQuality.setOnClickListener(this);
        this.mLvMediaQuality = (ListView)this.findViewById(id.lv_media_quality);
        this.mQualityAdapter = new AdapterMediaQuality(this.mAttachActivity);
        this.mLvMediaQuality.setAdapter(this.mQualityAdapter);
        this.mLvMediaQuality.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (IjkPlayerView.this.mCurSelectQuality != ((MediaQualityInfo)IjkPlayerView.this.mQualityAdapter.getItem(position)).getIndex()) {
                    IjkPlayerView.this.setMediaQuality(((MediaQualityInfo)IjkPlayerView.this.mQualityAdapter.getItem(position)).getIndex());
                    IjkPlayerView.this.mLoadingView.setVisibility(0);
                    IjkPlayerView.this.start();
                }

                IjkPlayerView.this._toggleMediaQuality();
            }
        });
    }

    public IjkPlayerView setVideoSource(String mediaSmooth, String mediaMedium, String mediaHigh, String mediaSuper, String mediaBd) {
        boolean isSelect = true;
        this.mQualityData = new ArrayList();
        if (mediaSmooth != null) {
            this.mVideoSource.put(0, mediaSmooth);
            this.mQualityData.add(new MediaQualityInfo(0, this.mMediaQualityDesc[0], isSelect));
            this.mCurSelectQuality = 0;
            isSelect = false;
        }

        if (mediaMedium != null) {
            this.mVideoSource.put(1, mediaMedium);
            this.mQualityData.add(new MediaQualityInfo(1, this.mMediaQualityDesc[1], isSelect));
            if (isSelect) {
                this.mCurSelectQuality = 1;
            }

            isSelect = false;
        }

        if (mediaHigh != null) {
            this.mVideoSource.put(2, mediaHigh);
            this.mQualityData.add(new MediaQualityInfo(2, this.mMediaQualityDesc[2], isSelect));
            if (isSelect) {
                this.mCurSelectQuality = 2;
            }

            isSelect = false;
        }

        if (mediaSuper != null) {
            this.mVideoSource.put(3, mediaSuper);
            this.mQualityData.add(new MediaQualityInfo(3, this.mMediaQualityDesc[3], isSelect));
            if (isSelect) {
                this.mCurSelectQuality = 3;
            }

            isSelect = false;
        }

        if (mediaBd != null) {
            this.mVideoSource.put(4, mediaBd);
            this.mQualityData.add(new MediaQualityInfo(4, this.mMediaQualityDesc[4], isSelect));
            if (isSelect) {
                this.mCurSelectQuality = 4;
            }
        }

        this.mQualityAdapter.updateItems(this.mQualityData);
        this.mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, ContextCompat.getDrawable(this.mAttachActivity, QUALITY_DRAWABLE_RES[this.mCurSelectQuality]), (Drawable)null, (Drawable)null);
        this.mIvMediaQuality.setText(this.mMediaQualityDesc[this.mCurSelectQuality]);
        this.setVideoPath((String)this.mVideoSource.get(this.mCurSelectQuality));
        return this;
    }

    public IjkPlayerView setMediaQuality(int quality) {
        if (this.mCurSelectQuality != quality && this.mVideoSource.get(quality) != null) {
            this.mQualityAdapter.setMediaQuality(quality);
            this.mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, ContextCompat.getDrawable(this.mAttachActivity, QUALITY_DRAWABLE_RES[quality]), (Drawable)null, (Drawable)null);
            this.mIvMediaQuality.setText(this.mMediaQualityDesc[quality]);
            this.mCurSelectQuality = quality;
            if (this.mVideoView.isPlaying()) {
                this.mCurPosition = this.mVideoView.getCurrentPosition();
                this.mVideoView.release(false);
            }

            this.mVideoView.setRender(2);
            this.setVideoPath((String)this.mVideoSource.get(quality));
            return this;
        } else {
            return this;
        }
    }

    private void _initVideoSkip() {
        this.mLlSkipLayout = this.findViewById(id.ll_skip_layout);
        this.mIvCancelSkip = (ImageView)this.findViewById(id.iv_cancel_skip);
        this.mTvSkipTime = (TextView)this.findViewById(id.tv_skip_time);
        this.mTvDoSkip = (TextView)this.findViewById(id.tv_do_skip);
        this.mIvCancelSkip.setOnClickListener(this);
        this.mTvDoSkip.setOnClickListener(this);
    }

    public int getCurPosition() {
        return this.mVideoView.getCurrentPosition();
    }

    public IjkPlayerView setSkipTip(int targetPosition) {
        this.mSkipPosition = targetPosition;
        return this;
    }

    private void _showSkipTip() {
        if (this.mSkipPosition != -1 && this.mLlSkipLayout.getVisibility() == 8) {
            this.mLlSkipLayout.setVisibility(0);
            this.mTvSkipTime.setText(StringUtils.generateTime((long)this.mSkipPosition));
            AnimHelper.doSlideRightIn(this.mLlSkipLayout, this.mWidthPixels, 0, 800);
            this.mHandler.postDelayed(this.mHideSkipTipRunnable, 15000L);
        }

    }

    private void _hideSkipTip() {
        if (this.mLlSkipLayout.getVisibility() != 8) {
            ViewCompat.animate(this.mLlSkipLayout).translationX((float)(-this.mLlSkipLayout.getWidth())).alpha(0.0F).setDuration(500L).setListener(new ViewPropertyAnimatorListenerAdapter() {
                public void onAnimationEnd(View view) {
                    IjkPlayerView.this.mLlSkipLayout.setVisibility(8);
                }
            }).start();
            this.mSkipPosition = -1;
        }
    }

    private void _initDanmaku() {
        this.mDanmakuView = (IDanmakuView)this.findViewById(id.sv_danmaku);
        this.mIvDanmakuControl = (ImageView)this.findViewById(id.iv_danmaku_control);
        this.mTvOpenEditDanmaku = (TextView)this.findViewById(id.tv_open_edit_danmaku);
        this.mTvTimeSeparator = (TextView)this.findViewById(id.tv_separator);
        this.mEditDanmakuLayout = this.findViewById(id.ll_edit_danmaku);
        this.mEtDanmakuContent = (EditText)this.findViewById(id.et_danmaku_content);
        this.mIvCancelSend = (ImageView)this.findViewById(id.iv_cancel_send);
        this.mIvDoSend = (ImageView)this.findViewById(id.iv_do_send);
        this.mDanmakuPlayerSeek = (SeekBar)this.findViewById(id.danmaku_player_seek);
        this.mDanmakuPlayerSeek.setMax(1000);
        this.mDanmakuPlayerSeek.setOnSeekBarChangeListener(this.mSeekListener);
        this.mIvDanmakuControl.setOnClickListener(this);
        this.mTvOpenEditDanmaku.setOnClickListener(this);
        this.mIvCancelSend.setOnClickListener(this);
        this.mIvDoSend.setOnClickListener(this);
        int navigationBarHeight = NavUtils.getNavigationBarHeight(this.mAttachActivity);
        if (navigationBarHeight > 0) {
            this.mEditDanmakuLayout.setPadding(0, 0, navigationBarHeight, 0);
        }

        int oneBtnWidth = this.getResources().getDimensionPixelOffset(R.dimen.danmaku_input_options_color_radio_btn_size);
        this.mMoreOptionsWidth = oneBtnWidth * 12;
        this.mDanmakuOptionsBasic = this.findViewById(id.input_options_basic);
        this.mDanmakuMoreOptions = this.findViewById(id.input_options_more);
        this.mDanmakuMoreOptions.setOnClickListener(this);
        this.mDanmakuCurColor = (RadioButton)this.findViewById(id.input_options_color_current);
        this.mDanmakuMoreColorIcon = (ImageView)this.findViewById(id.input_options_color_more_icon);
        this.mDanmakuTextSizeOptions = (RadioGroup)this.findViewById(id.input_options_group_textsize);
        this.mDanmakuTypeOptions = (RadioGroup)this.findViewById(id.input_options_group_type);
        this.mDanmakuColorOptions = (RadioGroup)this.findViewById(id.input_options_color_group);
        this.mDanmakuTextSizeOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == id.input_options_small_textsize) {
                    IjkPlayerView.this.mDanmakuTextSize = 25.0F * (IjkPlayerView.this.mDanmakuParser.getDisplayer().getDensity() - 0.6F) * 0.7F;
                } else if (checkedId == id.input_options_medium_textsize) {
                    IjkPlayerView.this.mDanmakuTextSize = 25.0F * (IjkPlayerView.this.mDanmakuParser.getDisplayer().getDensity() - 0.6F);
                }

            }
        });
        this.mDanmakuTypeOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == id.input_options_rl_type) {
                    IjkPlayerView.this.mDanmakuType = 1;
                } else if (checkedId == id.input_options_top_type) {
                    IjkPlayerView.this.mDanmakuType = 5;
                } else if (checkedId == id.input_options_bottom_type) {
                    IjkPlayerView.this.mDanmakuType = 4;
                }

            }
        });
        this.mDanmakuColorOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String color = (String)IjkPlayerView.this.findViewById(checkedId).getTag();
                IjkPlayerView.this.mDanmakuTextColor = Color.parseColor(color);
                IjkPlayerView.this.mDanmakuCurColor.setBackgroundColor(IjkPlayerView.this.mDanmakuTextColor);
            }
        });
    }

    private void _loadDanmaku() {
        if (this.mIsEnableDanmaku) {
            this.mDanmakuContext = DanmakuContext.create();
            if (this.mDanmakuParser == null) {
                this.mDanmakuParser = new BaseDanmakuParser() {
                    protected Danmakus parse() {
                        return new Danmakus();
                    }
                };
            }

            this.mDanmakuView.setCallback(new Callback() {
                public void prepared() {
                    if (IjkPlayerView.this.mVideoView.isPlaying() && !IjkPlayerView.this.mIsBufferingStart) {
                        IjkPlayerView.this.mDanmakuView.start();
                    }

                }

                public void updateTimer(DanmakuTimer timer) {
                }

                public void danmakuShown(BaseDanmaku danmaku) {
                }

                public void drawingFinished() {
                }
            });
            this.mDanmakuView.enableDanmakuDrawingCache(true);
            this.mDanmakuView.prepare(this.mDanmakuParser, this.mDanmakuContext);
        }

    }

    public IjkPlayerView enableDanmaku() {
        this.mIsEnableDanmaku = true;
        this._initDanmaku();
        if (this.mIsAlwaysFullScreen) {
            this._toggleDanmakuView(true);
        }

        return this;
    }

    public IjkPlayerView setDanmakuSource(InputStream stream) {
        if (stream == null) {
            return this;
        } else if (!this.mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        } else {
            if (this.mDanmakuLoader == null) {
                this.mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
            }

            try {
                this.mDanmakuLoader.load(stream);
            } catch (IllegalDataException var3) {
                var3.printStackTrace();
            }

            IDataSource<?> dataSource = this.mDanmakuLoader.getDataSource();
            if (this.mDanmakuParser == null) {
                this.mDanmakuParser = new BiliDanmukuParser();
            }

            this.mDanmakuParser.load(dataSource);
            return this;
        }
    }

    public IjkPlayerView setDanmakuSource(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return this;
        } else if (!this.mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        } else {
            if (this.mDanmakuLoader == null) {
                this.mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
            }

            try {
                this.mDanmakuLoader.load(uri);
            } catch (IllegalDataException var3) {
                var3.printStackTrace();
            }

            IDataSource<?> dataSource = this.mDanmakuLoader.getDataSource();
            if (this.mDanmakuParser == null) {
                this.mDanmakuParser = new BiliDanmukuParser();
            }

            this.mDanmakuParser.load(dataSource);
            return this;
        }
    }

    public IjkPlayerView setDanmakuCustomParser(BaseDanmakuParser parser, ILoader loader, BaseDanmakuConverter converter) {
        this.mDanmakuParser = parser;
        this.mDanmakuLoader = loader;
        this.mDanmakuConverter = converter;
        return this;
    }

    public IjkPlayerView showOrHideDanmaku(boolean isShow) {
        if (isShow) {
            this.mIvDanmakuControl.setSelected(false);
            this.mDanmakuView.show();
        } else {
            this.mIvDanmakuControl.setSelected(true);
            this.mDanmakuView.hide();
        }

        return this;
    }

    public void sendDanmaku(String text, boolean isLive) {
        if (!this.mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        } else if (TextUtils.isEmpty(text)) {
            Toast.makeText(this.mAttachActivity, "内容为空", 0).show();
        } else {
            BaseDanmaku danmaku = this.mDanmakuContext.mDanmakuFactory.createDanmaku(this.mDanmakuType);
            if (danmaku != null && this.mDanmakuView != null) {
                if (this.mDanmakuTextSize == -1.0F) {
                    this.mDanmakuTextSize = 25.0F * (this.mDanmakuParser.getDisplayer().getDensity() - 0.6F);
                }

                danmaku.text = text;
                danmaku.padding = 5;
                danmaku.isLive = isLive;
                danmaku.priority = 0;
                danmaku.textSize = this.mDanmakuTextSize;
                danmaku.textColor = this.mDanmakuTextColor;
                danmaku.underlineColor = -16711936;
                danmaku.setTime(this.mDanmakuView.getCurrentTime() + 500L);
                this.mDanmakuView.addDanmaku(danmaku);
                if (this.mDanmakuListener != null) {
                    if (this.mDanmakuConverter != null) {
                        this.mDanmakuListener.onDataObtain(this.mDanmakuConverter.convertDanmaku(danmaku));
                    } else {
                        this.mDanmakuListener.onDataObtain(danmaku);
                    }
                }

            }
        }
    }

    public void editVideo() {
        if (this.mVideoView.isPlaying()) {
            this.pause();
            this.mVideoStatus = 502;
        } else {
            this.mVideoStatus = 503;
        }

        this._hideAllView(false);
    }

    public boolean recoverFromEditVideo() {
        if (this.mVideoStatus == 501) {
            return false;
        } else {
            if (this.mIsFullscreen) {
                this._recoverScreen();
            }

            if (this.mVideoStatus == 502) {
                this.start();
            }

            this.mVideoStatus = 501;
            return true;
        }
    }

    private void _resumeDanmaku() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared() && this.mDanmakuView.isPaused()) {
            if (this.mDanmakuTargetPosition != -1L) {
                this.mDanmakuView.seekTo(this.mDanmakuTargetPosition);
                this.mDanmakuTargetPosition = -1L;
            } else {
                this.mDanmakuView.resume();
            }
        }

    }

    private void _pauseDanmaku() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
            this.mDanmakuView.pause();
        }

    }

    private void _toggleDanmakuShow() {
        if (this.mIvDanmakuControl.isSelected()) {
            this.showOrHideDanmaku(true);
        } else {
            this.showOrHideDanmaku(false);
        }

    }

    private void _toggleDanmakuView(boolean isShow) {
        if (this.mIsEnableDanmaku) {
            if (isShow) {
                this.mIvDanmakuControl.setVisibility(0);
                this.mTvOpenEditDanmaku.setVisibility(0);
                this.mTvTimeSeparator.setVisibility(0);
                this.mDanmakuPlayerSeek.setVisibility(0);
                this.mPlayerSeek.setVisibility(8);
            } else {
                this.mIvDanmakuControl.setVisibility(8);
                this.mTvOpenEditDanmaku.setVisibility(8);
                this.mTvTimeSeparator.setVisibility(8);
                this.mDanmakuPlayerSeek.setVisibility(8);
                this.mPlayerSeek.setVisibility(0);
            }
        }

    }

    private void _recoverScreen() {
        this.mEditDanmakuLayout.clearFocus();
        this.mEditDanmakuLayout.setVisibility(8);
        SoftInputUtils.closeSoftInput(this.mAttachActivity);
        this._setUiLayoutFullscreen();
        if (this.mDanmakuColorOptions.getWidth() != 0) {
            this._toggleMoreColorOptions();
        }

    }

    private void _toggleMoreColorOptions() {
        if (this.mBasicOptionsWidth == -1) {
            this.mBasicOptionsWidth = this.mDanmakuOptionsBasic.getWidth();
        }

        if (this.mDanmakuColorOptions.getWidth() == 0) {
            AnimHelper.doClipViewWidth(this.mDanmakuOptionsBasic, this.mBasicOptionsWidth, 0, 300);
            AnimHelper.doClipViewWidth(this.mDanmakuColorOptions, 0, this.mMoreOptionsWidth, 300);
            ViewCompat.animate(this.mDanmakuMoreColorIcon).rotation(180.0F).setDuration(150L).setStartDelay(250L).start();
        } else {
            AnimHelper.doClipViewWidth(this.mDanmakuOptionsBasic, 0, this.mBasicOptionsWidth, 300);
            AnimHelper.doClipViewWidth(this.mDanmakuColorOptions, this.mMoreOptionsWidth, 0, 300);
            ViewCompat.animate(this.mDanmakuMoreColorIcon).rotation(0.0F).setDuration(150L).setStartDelay(250L).start();
        }

    }

    private void _initReceiver() {
        this.mPbBatteryLevel = (ProgressBar)this.findViewById(id.pb_battery);
        this.mTvSystemTime = (TextView)this.findViewById(id.tv_system_time);
        this.mTvSystemTime.setText(StringUtils.getCurFormatTime());
        this.mBatteryReceiver = new BatteryBroadcastReceiver();
        this.mScreenReceiver = new ScreenBroadcastReceiver();
        this.mAttachActivity.registerReceiver(this.mBatteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.mAttachActivity.registerReceiver(this.mScreenReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.mIvScreenshot = (ImageView)this.findViewById(id.iv_screenshot);
        this.mIvScreenshot.setOnClickListener(this);
        if (SDCardUtils.isAvailable()) {
            this._createSaveDir(SDCardUtils.getRootPath() + File.separator + "IjkPlayView");
        }

    }

    private void _doScreenshot() {
        this.editVideo();
        this._showShareDialog(this.mVideoView.getScreenshot());
    }

    private void _showShareDialog(Bitmap bitmap) {
        if (this.mShareDialog == null) {
            this.mShareDialog = new ShareDialog();
            this.mShareDialog.setClickListener(this.mInsideDialogClickListener);
            this.mShareDialog.setDismissListener(this.mDialogDismissListener);
            if (this.mDialogClickListener != null) {
                this.mShareDialog.setShareMode(true);
            }
        }

        this.mShareDialog.setScreenshotPhoto(bitmap);
        this.mShareDialog.show(this.mAttachActivity.getSupportFragmentManager(), "share");
    }

    public IjkPlayerView setDialogClickListener(OnDialogClickListener dialogClickListener) {
        this.mDialogClickListener = dialogClickListener;
        if (this.mShareDialog != null) {
            this.mShareDialog.setShareMode(true);
        }

        return this;
    }

    private void _createSaveDir(String path) {
        this.mSaveDir = new File(path);
        if (!this.mSaveDir.exists()) {
            this.mSaveDir.mkdirs();
        } else if (!this.mSaveDir.isDirectory()) {
            this.mSaveDir.delete();
            this.mSaveDir.mkdirs();
        }

    }

    public IjkPlayerView setSaveDir(String path) {
        this._createSaveDir(path);
        return this;
    }

    static {
        QUALITY_DRAWABLE_RES = new int[]{R.mipmap.ic_media_quality_smooth, R.mipmap.ic_media_quality_medium, R.mipmap.ic_media_quality_high, R.mipmap.ic_media_quality_super, aiyzp.com.medialib.R.
                mipmap.ic_media_quality_bd};
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private ScreenBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                IjkPlayerView.this.mIsScreenLocked = true;
            }

        }
    }

    class BatteryBroadcastReceiver extends BroadcastReceiver {
        private static final int BATTERY_LOW_LEVEL = 15;

        BatteryBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int curPower = level * 100 / scale;
                int status = intent.getIntExtra("status", 1);
                if (status == 2) {
                    IjkPlayerView.this.mPbBatteryLevel.setSecondaryProgress(0);
                    IjkPlayerView.this.mPbBatteryLevel.setProgress(curPower);
                    IjkPlayerView.this.mPbBatteryLevel.setBackgroundResource(aiyzp.com.medialib.R.mipmap.ic_battery_charging);
                } else if (curPower < 15) {
                    IjkPlayerView.this.mPbBatteryLevel.setProgress(0);
                    IjkPlayerView.this.mPbBatteryLevel.setSecondaryProgress(curPower);
                    IjkPlayerView.this.mPbBatteryLevel.setBackgroundResource(aiyzp.com.medialib.R.mipmap.ic_battery_red);
                } else {
                    IjkPlayerView.this.mPbBatteryLevel.setSecondaryProgress(0);
                    IjkPlayerView.this.mPbBatteryLevel.setProgress(curPower);
                    IjkPlayerView.this.mPbBatteryLevel.setBackgroundResource(aiyzp.com.medialib.R.mipmap.ic_battery);
                }
            }

        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    public @interface DanmakuTag {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    public @interface MediaQuality {
    }
}
