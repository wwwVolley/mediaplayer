package aiyzp.com.medialib;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.Iterator;

public class AndroidMediaController extends MediaController implements IMediaController {
    private ActionBar mActionBar;
    private ArrayList<View> mShowOnceArray = new ArrayList();

    public AndroidMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public AndroidMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        this.initView(context);
    }

    public AndroidMediaController(Context context) {
        super(context);
        this.initView(context);
    }

    private void initView(Context context) {
    }

    public void setSupportActionBar(@Nullable ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (this.isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }

    }

    public void show() {
        super.show();
        if (this.mActionBar != null) {
            this.mActionBar.show();
        }

    }

    public void hide() {
        super.hide();
        if (this.mActionBar != null) {
            this.mActionBar.hide();
        }

        Iterator var1 = this.mShowOnceArray.iterator();

        while(var1.hasNext()) {
            View view = (View)var1.next();
            view.setVisibility(8);
        }

        this.mShowOnceArray.clear();
    }

    public void showOnce(@NonNull View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(0);
        this.show();
    }
}
