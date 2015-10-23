package gps.fhv.at.gps_hawk.activities.navigation;

import android.app.Fragment;

/**
 * Author: Philip Heimböck
 * Date: 22.10.15
 */
public class NavigationItem {

    private String mText;
    private int mIconRes;
    private boolean mVisible;
    private Fragment mFragment;

    public NavigationItem() {
    }

    public NavigationItem(Fragment fragment, String text, int iconRes) {
        this(fragment, text, iconRes, true);
    }

    public NavigationItem(Fragment fragment, String text, int iconRes, boolean visible) {
        mFragment = fragment;
        mText = text;
        mIconRes = iconRes;
        mVisible = visible;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
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
