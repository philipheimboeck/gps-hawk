package gps.fhv.at.gps_hawk.activities.navigation;

/**
 * Author: Philip Heimböck
 * Date: 22.10.15
 */
public class NavigationItem {

    private String mText;
    private int mIconRes;
    private boolean mVisible;

    public NavigationItem() {
    }

    public NavigationItem(String text, int iconRes) {
        this(text, iconRes, true);
    }

    public NavigationItem(String text, int iconRes, boolean visible) {
        mText = text;
        mIconRes = iconRes;
        mVisible = visible;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public void setIconRes(int iconRes) {
        this.mIconRes = iconRes;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }
}
