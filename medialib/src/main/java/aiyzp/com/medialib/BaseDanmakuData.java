package aiyzp.com.medialib;

public abstract class BaseDanmakuData {
    public int type;
    public String content;
    public long time;
    public float textSize;
    public int textColor;

    public BaseDanmakuData() {
    }

    public String toString() {
        return "BaseDanmakuData{type=" + this.type + ", content='" + this.content + '\'' + ", time=" + this.time + ", textSize=" + this.textSize + ", textColor=" + this.textColor + '}';
    }
}
