package aiyzp.com.medialib;

public interface OnDanmakuListener<T> {
    boolean isValid();

    void onDataObtain(T var1);
}
