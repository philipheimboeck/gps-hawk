package gps.fhv.at.gps_hawk.activities.navigation;

/**
 * Author: Philip Heimböck
 * Date: 22.10.15
 */
public class NavigationItem {

    private String mText;
    private int mIconRes;
    private boolean mVisible;
    private NavigationAction mAction;

    public NavigationItem(NavigationAction action, String text, int iconRes) {
        this(action, text, iconRes, true);
    }

    public NavigationItem(NavigationAction action, String text, int iconRes, boolean visible) {
        mAction = action;
        mText = text;
        mIconRes = iconRes;
        mVisible = visible;
    }

    public NavigationAction getAction() {
        return mAction;
    }

    public void setAction(NavigationAction action) {
        mAction = action;
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
