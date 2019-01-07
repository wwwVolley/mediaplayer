package aiyzp.com.medialib;


import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList();
    }

    public BaseListAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public BaseListAdapter(Context context, T[] datas) {
        this.mContext = context;
        this.mDatas = new ArrayList();
        Collections.addAll(this.mDatas, datas);
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public T getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public void updateItems(List<T> items) {
        this.mDatas = items;
        this.notifyDataSetChanged();
    }

    public void addItem(T item) {
        this.mDatas.add(0, item);
        this.notifyDataSetChanged();
    }

    public void addItem(T item, int position) {
        position = Math.min(position, this.mDatas.size());
        this.mDatas.add(position, item);
        this.notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        this.mDatas.addAll(items);
    }

    public void removeItem(int position) {
        if (position <= this.mDatas.size() - 1) {
            this.mDatas.remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(T item) {
        int pos = 0;

        for(Iterator var3 = this.mDatas.iterator(); var3.hasNext(); ++pos) {
            T info = (T) var3.next();
            if (item.hashCode() == info.hashCode()) {
                this.removeItem(pos);
                break;
            }
        }

    }

    public void cleanItems() {
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }
}
