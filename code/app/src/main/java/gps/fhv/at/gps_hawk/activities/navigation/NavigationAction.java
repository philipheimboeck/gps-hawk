package gps.fhv.at.gps_hawk.activities.navigation;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Author: Philip Heimböck
 * Date: 01.11.15
 */
public class NavigationAction {

    public enum NavigationActionType {
        ACTION_NOTHING,
        ACTION_CHANGE_FRAGMENT,
        ACTION_CHANGE_ACTIVITY,
        ACTION_FINISH
    }

    private NavigationActionType mActionType;

    private Object mObject;

    public NavigationAction() {
        mActionType = NavigationActionType.ACTION_NOTHING;
    }

    public NavigationAction(Fragment object) {
        mObject = object;
        mActionType = NavigationActionType.ACTION_CHANGE_FRAGMENT;
    }

    public NavigationAction(Intent object) {
        mObject = object;
        mActionType = NavigationActionType.ACTION_CHANGE_ACTIVITY;
    }

    public Object getObject() {
        return mObject;
    }

    public NavigationActionType getActionType() {
        return mActionType;
    }

    /**
     * When changing the action type, ensure that you also set the object
     * @param actionType
     */
    public NavigationAction setActionType(NavigationActionType actionType) {
        mActionType = actionType;
        return this;
    }

    public NavigationAction setObject(Object object) {
        mObject = object;
        return this;
    }
}
