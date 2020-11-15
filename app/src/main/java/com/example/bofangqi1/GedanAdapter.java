package com.example.bofangqi1;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lw on 2017/4/14.
 */

public class GedanAdapter extends ArrayAdapter{
    private final int resourceId;

    public GedanAdapter(Context context, int textViewResourceId, List<Gedan> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gedan gedan = (Gedan) getItem(position); // 获取当前项的Gedan实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView GedanId = (ImageView) view.findViewById(R.id.Gedan_id);//获取该布局内的图片视图
        TextView GedanName = (TextView) view.findViewById(R.id.Gedan_name);//获取该布局内的文本视图
        GedanId.setImageResource(gedan.getGedanId());//为图片视图设置图片资源
        GedanName.setText(gedan.getName());//为文本视图设置文本内容
        return view;
    }
}