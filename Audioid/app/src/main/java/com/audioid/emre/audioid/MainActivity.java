package com.audioid.emre.audioid;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    String[] music_names;
    TypedArray singer_pics;
    String[] singer_names;
    TypedArray audios;
    List<RowItem> rowItems;
    ListView mylistview;
    TimeConvertor convertor;
    MediaPlayer audio;
    Handler mHandler = new Handler();
    ImageView btnStart;
    ImageView btnPause;
    ConstraintLayout footerBar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //string dosyasında bulunan önceden kaydedilmiş veriler listviewe aktarıldı.
        rowItems = new ArrayList<RowItem>();
        music_names = getResources().getStringArray(R.array.music_name);
        singer_pics = getResources().obtainTypedArray(R.array.singer_pic);
        singer_names = getResources().getStringArray(R.array.singer_name);
        audios=getResources().obtainTypedArray(R.array.audio);
        for (int i = 0; i < music_names.length; i++) {
            RowItem item = new RowItem(music_names[i],singer_pics.getResourceId(i, -1), singer_names[i],audios.getResourceId(i, -1));
            rowItems.add(item);
        }
        mylistview = (ListView) findViewById(R.id.listSounds);
        CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);
        mylistview.setOnItemClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Ana Ekrana Geri Gelindiğinde Çalan Şarkı Bilgileri Ana Ekrana Çekiliyor.
        // Objelere ilk atamaları yapılıyor.
        if(getIntent().getIntExtra("ImageID", -1)!=-1){
            footerBar=(ConstraintLayout)findViewById(R.id.footerBar);
            footerBar.bringToFront();
            footerBar.setVisibility(View.VISIBLE);
            ListView listSounds=(ListView)findViewById(R.id.listSounds);
            listSounds.setPadding(0,0,0,60);

            ImageView singerPic=(ImageView)findViewById(R.id.singerPic);
            TextView audioName=(TextView)findViewById(R.id.audioName);
            TextView currentDuration=(TextView)findViewById(R.id.currentDuration);
            btnPause=(ImageView)findViewById(R.id.btnPause);
            btnStart=(ImageView)findViewById(R.id.btnStart);

            singerPic.setImageResource(getIntent().getIntExtra("ImageID", 2130837511));
            audioName.setText(getIntent().getExtras().getString("MusicName"));
            convertor = new TimeConvertor();
            currentDuration.setText(""+convertor.milliSecondsToTimer(getIntent().getIntExtra("CurrentDuration", 0)));
            audio= MediaPlayer.create(this,getIntent().getIntExtra("AudioID", 2131034112));
            audio.seekTo(getIntent().getIntExtra("CurrentDuration", 0));
            audio.start();
            btnPause.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnPause.setVisibility(View.INVISIBLE);
                    btnStart.setVisibility(View.VISIBLE);
                    audio.pause();
                }
            });
            btnStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnStart.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                    audio.start();
                }
            });
            footerBar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    audio.stop();
                    intent = new Intent(v.getContext(), PlayScreen.class);
                    intent.putExtra("ImageID", getIntent().getIntExtra("ImageID", 2130837511));
                    intent.putExtra("MusicName", getIntent().getExtras().getString("MusicName"));
                    intent.putExtra("CurrentDuration", (audio.getCurrentPosition() + 3500));
                    intent.putExtra("AudioID", getIntent().getIntExtra("AudioID", 2131034112));

                    startActivity(intent);
                }
            });
            updateDuration();
        }
    }
    // Süre değerinin run time müziğin o anki değerini göstermesi için ayarlandı.
    public void updateDuration() {mHandler.postDelayed(mUpdateTimeTask, 100);}
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            convertor = new TimeConvertor();
            TextView currentDuration=(TextView)findViewById(R.id.currentDuration);
            currentDuration.setText("" + convertor.milliSecondsToTimer(audio.getCurrentPosition()));
            mHandler.postDelayed(this, 100);
        }
    };
    //Listviewde seçilen müzik bilgileri PlayScreen ekranına aktarıldı.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        if(audio!=null)audio.stop();
        intent = new Intent(this, PlayScreen.class);
        intent.putExtra("ImageID",rowItems.get(position).getSinger_pic_id());
        intent.putExtra("MusicName",rowItems.get(position).getMusic_name());
        intent.putExtra("SignerName",rowItems.get(position).getSinger_name());
        intent.putExtra("AudioID",rowItems.get(position).getAudio());
        startActivity(intent);
    }
}
