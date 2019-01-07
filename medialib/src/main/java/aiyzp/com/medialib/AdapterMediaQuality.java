package aiyzp.com.medialib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import aiyzp.com.medialib.R.id;
import aiyzp.com.medialib.R.layout;

public class AdapterMediaQuality extends BaseListAdapter<MediaQualityInfo> {
    public AdapterMediaQuality(Context context, List<MediaQualityInfo> datas) {
        super(context, datas);
    }

    public AdapterMediaQuality(Context context) {
        super(context);
    }

    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(layout.adapter_media_quality, parent, false);
        }

        TextView qualityDesc = (TextView)view.findViewById(id.tv_media_quality);
        qualityDesc.setText(((MediaQualityInfo)this.mDatas.get(i)).getDesc());
        qualityDesc.setSelected(((MediaQualityInfo)this.mDatas.get(i)).isSelect());
        return view;
    }

    private void _cleanSelected() {
        Iterator var1 = this.mDatas.iterator();

        while(var1.hasNext()) {
            MediaQualityInfo info = (MediaQualityInfo)var1.next();
            if (info.isSelect()) {
                info.setSelect(false);
            }
        }

    }

    public void setMediaQuality(int quality) {
        Iterator var2 = this.mDatas.iterator();

        MediaQualityInfo info;
        do {
            if (!var2.hasNext()) {
                return;
            }

            info = (MediaQualityInfo)var2.next();
        } while(info.getIndex() != quality);

        if (!info.isSelect()) {
            this._cleanSelected();
            info.setSelect(true);
            this.notifyDataSetChanged();
        }

    }
}
