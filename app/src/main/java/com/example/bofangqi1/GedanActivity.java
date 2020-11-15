package com.example.bofangqi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GedanActivity  extends Activity{
    private List<Gedan> gedanList = new ArrayList<Gedan>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initGedans(); // 初始化歌单数据
        GedanAdapter adapter = new GedanAdapter(GedanActivity.this, R.layout.gedan_item, gedanList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Gedan gedan = gedanList.get(position);
                Intent intent = new Intent(GedanActivity.this,MainActivity.class);
                intent.putExtra("name",gedan.getName());
                intent.putExtra("photo",gedan.getGedanId());
                intent.putExtra("song",gedan.getGedanSong());
                Toast.makeText(GedanActivity.this, gedan.getName(),
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                System.exit(0);
            }

        });
    }

    private void initGedans() {
        Gedan yinyue = new Gedan("男孩",R.drawable.nanhai,R.raw.song);
        Gedan yinyue1 = new Gedan("男",R.drawable.nan,R.raw.song1);
        Gedan yinyue2 = new Gedan("不再联系",R.drawable.buzailianxi,R.raw.song2);
        Gedan yinyue3 = new Gedan("烟火"  ,R.drawable.yanhuo,R.raw.song3);
        Gedan yinyue4 = new Gedan("画心",R.drawable.huaxin,R.raw.song4);
        Gedan yinyue5 = new Gedan("老男孩",R.drawable.laonanhai,R.raw.song5);
        gedanList.add(yinyue);
        gedanList.add(yinyue1);
        gedanList.add(yinyue2);
        gedanList.add(yinyue3);
        gedanList.add(yinyue4);
        gedanList.add(yinyue5);
    }

}

