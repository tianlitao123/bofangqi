package com.example.bofangqi1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myTag";
    int id = 0;
    int randomID = 0;//生成随机数
    private MediaPlayer mediaPlayer;
    ImageView titlePhoto;
    private List<String> songList = new ArrayList<String>();//存放歌曲清单
    private List<String> photoList = new ArrayList<String>();//存放图片清单
    private List<String> nameList = new ArrayList<String>();//存放名称清单
    private SeekBar mSeekBar;//拖动条
    Handler handler = new Handler();
    private TextSwitcher mSwitcher;//textview上下滚动
    private boolean running;//判断是否在运行
    private boolean random;//判断是否随机
    private ObjectAnimator discAnimation;//自定义指针和唱盘


    //---------------------播放到当前音乐的滑动条及时间设置--------------------------
    /*public void run(){
        Thread thread = new Thread((Runnable) this);
        thread.start();
        try {
            while (mediaPlayer.isPlaying()) {
                long musicDuration = mediaPlayer.getDuration();//获取文件总长度
                final long position = mediaPlayer.getCurrentPosition();//获取播放当前进度
                final Date dateTotal = new Date(musicDuration);
                final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
                mSeekBar.setMax((int) musicDuration);
                mSeekBar.setProgress((int) position);
                mSwitcher.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                Date date = new Date(position);
                                String time = sb.format(date) + "/" + sb.format(dateTotal);
                                mSwitcher.setCurrentText(time);
                            }
                        }
                );
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_about_us: //关于
                String version = "1.0.190712";
                AlertDialog aboutDialog = new AlertDialog.Builder(this).create();
                aboutDialog.setTitle("音乐播放器");
                aboutDialog.setMessage("当前版本：" + version);
                aboutDialog.show();
                break;
            case R.id.id_scan_some: //歌单
                Intent intent = new Intent();
                startActivity(new Intent(MainActivity.this, GedanActivity.class));
                break;
            case R.id.id_close_app: //退出
                AlertDialog exitDialog = new AlertDialog.Builder(this).create();
                exitDialog.setTitle("系统提示");
                exitDialog.setMessage("确定要退出吗");
                exitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "欢迎回来", Toast.LENGTH_SHORT).show();
                    }
                });
                exitDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);*/

        /*如果需要播放完停止，则需要注册OnCompletionListener监听器
        在本示例中，用户可以选择是否循环播放，如果选择了循环播放，则播放完后会自动转到Started状态，再次播放
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.v(TAG,"setOnCompletionListener");
                mp.release();
            }
        });*/

        final TextView txtLoopState = (TextView) findViewById(R.id.txtLoopState);
        final TextView title = (TextView) findViewById(R.id.smalltitle);//正在播放的音乐
        final ImageView titlePhoto = (ImageView) findViewById(R.id.disc);
        songList.add(String.valueOf(R.raw.song));
        photoList.add(String.valueOf(R.drawable.nanhai));
        nameList.add("男孩");
        songList.add(String.valueOf(R.raw.song1));
        photoList.add(String.valueOf(R.drawable.nan));
        nameList.add("男");
        songList.add(String.valueOf(R.raw.song2));
        photoList.add(String.valueOf(R.drawable.buzailianxi));
        nameList.add("不再联系");
        songList.add(String.valueOf(R.raw.song3));
        photoList.add(String.valueOf(R.drawable.yanhuo));
        nameList.add("烟火");
        songList.add(String.valueOf(R.raw.song4));
        photoList.add(String.valueOf(R.drawable.huaxin));
        nameList.add("画心");
        songList.add(String.valueOf(R.raw.song5));
        photoList.add(String.valueOf(R.drawable.laonanhai));
        nameList.add("老男孩");
        mediaPlayer = MediaPlayer.create(this, Integer.parseInt(songList.get(0)));
        final Button buttonStart = (Button) findViewById(R.id.buttonStart);
        final Button buttonPause = (Button) findViewById(R.id.buttonPAUSE);
        final Button buttonStop = (Button) findViewById(R.id.buttonSTOP);
        final Button buttonLoop = (Button) findViewById(R.id.buttonLOOP);
        final Button buttonRandom = (Button) findViewById(R.id.buttonRandom);
        final Button buttonNext = (Button) findViewById(R.id.buttonNext);
        final Button buttonPre = (Button) findViewById(R.id.buttonPRE);
        mSwitcher = (TextSwitcher) findViewById(R.id.text_switcher);
        mSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        buttonPause.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonLoop.setEnabled(false);
        buttonRandom.setEnabled(false);
        getData();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null)
            name = "音乐";
        title.setText(name);
        int photo = intent.getIntExtra("photo", 0);
        if (photo == 0) {
            photo = R.drawable.music;
        }
        titlePhoto.setImageResource(photo);
        int song = intent.getIntExtra("song", Integer.parseInt(songList.get(0)));
        Toast.makeText(MainActivity.this, song, Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(this, song);
        final long musicDuration = mediaPlayer.getDuration();//获取文件总长度
        final Date dateTotal = new Date(musicDuration);
        final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
        final long position = mediaPlayer.getCurrentPosition();//获取播放当前进度
        mSeekBar = (SeekBar) findViewById(R.id.music_seek_bar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != 0) {
                    int s = progress / 1000;
                    String string = s / 60 + ":" + s % 60;
                    Date date = new Date(position);
                    String time = string + "/" + sb.format(dateTotal);
                    mSwitcher.setCurrentText(time);
                    if (mediaPlayer.getCurrentPosition()==Integer.valueOf(String.valueOf(musicDuration))&&random){
                        String s1 = songList.get(randomID);
                        //Uri uri = Uri.parse(s1);
                        if (!mediaPlayer.isPlaying()){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.valueOf(s1));
                            mediaPlayer.start();
                        }else{
                            mediaPlayer.stop();
                            mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.valueOf(s1));
                            mediaPlayer.start();
                        }

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.postDelayed(run, 1000);
            }
        });
        //  id =song;
        //失败的intent实例
        /*Intent intent = new Intent();
        Bundle b = new Bundle();
        title.setText("EE");
        String rString = title.toString();
        rString = b.getString("name");
        int a = b.getInt("key");

        Toast.makeText(MainActivity.this, rString, Toast.LENGTH_SHORT).show();*/
        //开始播放
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.start();
                    buttonPause.setEnabled(true);
                    buttonStop.setEnabled(true);
                    buttonLoop.setEnabled(true);
                    buttonRandom.setEnabled(true);
                    final long musicDuration = mediaPlayer.getDuration();//获取文件总长度
                    final long position = mediaPlayer.getCurrentPosition();//获取播放当前进度
                    System.out.println((int) musicDuration);
                    System.out.println((int) position);
                    mSeekBar.setMax((int) musicDuration);
                    mSeekBar.setProgress((int) position);
                    mSwitcher.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Date date = new Date(position);
                                    String time = sb.format(date) + "/" + sb.format(dateTotal);
                                    mSwitcher.setCurrentText(time);

                                }
                            }
                    );
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //run();
            }
        });
       /* while(mediaPlayer.isPlaying()){
            long musicDuration = mediaPlayer.getDuration();//获取文件总长度
            final long position = mediaPlayer.getCurrentPosition();//获取播放当前进度
            final Date dateTotal = new Date(musicDuration);
            final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
            mSeekBar.setMax((int) musicDuration);
            mSeekBar.setProgress((int) position);
            mSwitcher.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            Date date = new Date(position);
                            String time = sb.format(date) + "/" + sb.format(dateTotal);
                            mSwitcher.setCurrentText(time);

                        }
                    }
            );
        }*/


        //暂停播放
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    buttonPause.setText("Play");
                    mediaPlayer.pause();
                } else {
                    buttonPause.setText("Pause");
                    mediaPlayer.start();
                }

            }
        });
        //停止播放
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();

            }
        });
    //随机播放
    buttonRandom.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "RANDOM");
            random = true;
            //randomID = (int) (Math.random() * 6);
            txtLoopState.setText("随机播放");
                /*mediaPlayer.setOnCompletionListener(new onCompletionListener() {

                });*/
                /*if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(MainActivity.this, randomID);*/
            System.out.println(randomID);
        }
    });

        //循环播放
        buttonLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Looping");

                boolean loop = mediaPlayer.isLooping();
                mediaPlayer.setLooping(!loop);


                if (!loop)
                    txtLoopState.setText("循环播放");
                else
                    txtLoopState.setText("一次播放");


            }
        });
        //播放下一首
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                id = id + 1;
                if (id <= 5 && id >= 0) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.parseInt(songList.get(id)));
                    title.setText(nameList.get(id));
                    titlePhoto.setImageResource(Integer.parseInt(photoList.get(id)));
                } else if (id > 5) {
                    id = id - 1;
                    mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.parseInt(songList.get(5)));
                    title.setText(nameList.get(5));
                    titlePhoto.setImageResource(Integer.parseInt(photoList.get(5)));
                    Toast.makeText(MainActivity.this, "已经到底了", Toast.LENGTH_SHORT).show();
                } else if (id < 0) {
                    id = id + 1;
                    mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.parseInt(songList.get(id)));
                    title.setText(nameList.get(id));
                    titlePhoto.setImageResource(Integer.parseInt(photoList.get(id)));
                }
            }
        });
        //播放上一首
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                id = id - 1;
                if (id >= 0) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.parseInt(songList.get(id)));
                    title.setText(nameList.get(id));
                    titlePhoto.setImageResource(Integer.parseInt(photoList.get(id)));
                } else {
                    id = id + 1;
                    mediaPlayer = MediaPlayer.create(MainActivity.this, Integer.parseInt(songList.get(0)));
                    title.setText(nameList.get(0));
                    titlePhoto.setImageResource(Integer.parseInt(photoList.get(0)));
                    Toast.makeText(MainActivity.this, "已经到顶了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setAnimations();
    }

    //动画设置
    public void setAnimations() {
        discAnimation = ObjectAnimator.ofFloat(titlePhoto, "rotation", 0, 360);
        discAnimation.setDuration(20000);
        discAnimation.setInterpolator(new LinearInterpolator());
        discAnimation.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void getData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(run, 100);
        }
    };
}
