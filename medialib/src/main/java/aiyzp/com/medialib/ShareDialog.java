package aiyzp.com.medialib;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import aiyzp.com.medialib.R.id;
import aiyzp.com.medialib.R.layout;
import aiyzp.com.medialib.R.style;

public class ShareDialog extends DialogFragment {
    private OnDialogClickListener mClickListener;
    private OnDialogDismissListener mDismissListener;
    private Bitmap mBitmap;
    private boolean mIsShareMode = false;

    public ShareDialog() {
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(1);
        Window window = this.getDialog().getWindow();
        window.addFlags(2);
        window.setWindowAnimations(style.AnimateDialog);
        window.setBackgroundDrawable(new ColorDrawable(0));
        View view = inflater.inflate(layout.dialog_share, container);
        ImageView photo = (ImageView)view.findViewById(id.iv_screenshot_photo);
        LayoutParams layoutParams = photo.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels * 7 / 10;
        layoutParams.height = this.getResources().getDisplayMetrics().heightPixels * 7 / 10;
        photo.setLayoutParams(layoutParams);
        if (this.mBitmap != null) {
            photo.setImageBitmap(this.mBitmap);
        }

        view.findViewById(id.btn_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShareDialog.this.dismiss();
            }
        });
        TextView tvShare = (TextView)view.findViewById(id.btn_share);
        if (this.mIsShareMode) {
            tvShare.setText("分享");
        }

        tvShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ShareDialog.this.mClickListener != null) {
                    ShareDialog.this.mClickListener.onShare(ShareDialog.this.mBitmap, (Uri)null);
                }

                ShareDialog.this.dismiss();
            }
        });
        return view;
    }

    public void dismiss() {
        super.dismiss();
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }

    }

    public void setScreenshotPhoto(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setClickListener(OnDialogClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setDismissListener(OnDialogDismissListener dismissListener) {
        this.mDismissListener = dismissListener;
    }

    public void setShareMode(boolean shareMode) {
        this.mIsShareMode = shareMode;
    }

    public interface OnDialogDismissListener {
        void onDismiss();
    }

    public interface OnDialogClickListener {
        void onShare(Bitmap var1, Uri var2);
    }
}

