package aiyzp.com.medialib;


public final class MediaQualityInfo {
    private int index;
    private String desc;
    private boolean isSelect;

    public MediaQualityInfo(int index, String desc, boolean isSelect) {
        this.index = index;
        this.desc = desc;
        this.isSelect = isSelect;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }

    public String toString() {
        return "MediaQualityInfo{index=" + this.index + ", desc='" + this.desc + '\'' + ", isSelect=" + this.isSelect + '}';
    }
}
