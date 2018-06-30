package com.audioid.emre.audioid;

/**
 * Created by lenovo on 4.11.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItems;

    CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        ImageView singer_pic_id;
        TextView music_name;
        TextView singer_name;
        int audio;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {convertView = mInflater.inflate(R.layout.list_item, null);holder = new ViewHolder();

            holder.music_name = (TextView) convertView.findViewById(R.id.music_name);
            holder.singer_pic_id = (ImageView) convertView.findViewById(R.id.singer_pic);
            holder.singer_name = (TextView) convertView.findViewById(R.id.singer_name);

            RowItem row_pos = rowItems.get(position);

            holder.singer_pic_id.setImageResource(row_pos.getSinger_pic_id());
            holder.music_name.setText(row_pos.getMusic_name());
            holder.singer_name.setText(row_pos.getSinger_name());
            holder.audio=row_pos.getAudio();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
