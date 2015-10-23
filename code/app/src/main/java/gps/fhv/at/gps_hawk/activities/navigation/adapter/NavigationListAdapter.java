package gps.fhv.at.gps_hawk.activities.navigation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationItem;

/**
 * Author: Philip Heimböck
 * Date: 22.10.15
 */
public class NavigationListAdapter extends BaseAdapter {

    private Context mContext;
    private List<NavigationItem> mItems;

    public NavigationListAdapter(Context context, List<NavigationItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.navigation_item, null);
        }

        // Find the views
        ImageView imageView = (ImageView) convertView.findViewById(R.id.nav_icon);
        TextView textView = (TextView) convertView.findViewById(R.id.nav_title);

        // Get the item
        NavigationItem item = (NavigationItem) getItem(position);

        // Set image source
        if(item.getIconRes() > 0) {
            imageView.setImageResource(item.getIconRes());
        }

        // Set text
        textView.setText(item.getText());

        return convertView;
    }
}
