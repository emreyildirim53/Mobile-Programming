package com.audioid.emre.audioid;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer.OnCompletionListener;
public class PlayScreen extends Activity implements OnCompletionListener,SeekBar.OnSeekBarChangeListener{
    MediaPlayer audio=null;
    Handler mHandler = new Handler();
    TimeConvertor convertor;
    SeekBar progressBar;
    SeekBar volumeProgressbar;
    AudioManager audioManager = null;
    SharedPreferences prefs;
    private int seekForwardBackwardTime = 10000;
    //CurrentId mevcutsa ana menüdeki altta bulunan footerbara tıklanmıştır.
    //footer bara tıklandığında önceden playscreen ekranında kaydedilmiş preferens verileri çekilmektedir.
    //CurrentID mevcut değilse listeden herhangi bir şarkıya tıklanmıştır. Veriler intentle aktarılır.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        prefs = getPreferences(MODE_PRIVATE);
        if(getIntent().getIntExtra("CurrentDuration", -1)==-1)
            playSetting(getIntent().getIntExtra("ImageID", 2130837511),getIntent().getExtras().getString("MusicName"),getIntent().getExtras().getString("SignerName"),getIntent().getIntExtra("AudioID", 2131034112),0);
        else
            playSetting(prefs.getInt("ImageID", 2130837511),prefs.getString("MusicName", "Noname"),prefs.getString("SignerName", "Undefined"),getIntent().getIntExtra("AudioID", 2131034112),prefs.getInt("CurrentDuration",0));
        volumeSetting();
    }
    //Ses yarları için kullanılan metod
    private void volumeSetting() {
        try {
            volumeProgressbar = (SeekBar)findViewById(R.id.volumeProgressBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeProgressbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeProgressbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeProgressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar arg0) {}
                public void onStartTrackingTouch(SeekBar arg0) {}
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        } catch (Exception e) {e.printStackTrace();}
    }
    // Play screen ekranınındaki tanımlama ve bilgilerin aktarılması için tasarlandı.
    // CurrentId mevcudiyetine göre pref veya intent parametresi yollandı.
    void playSetting(int signer_pic_source,String music_name_source,String singer_name_source,int audioID_source,int currentDuration){
        try{
            ImageView signer_pic= (ImageView)findViewById(R.id.singer_pic);
            TextView music_name= (TextView)findViewById(R.id.music_name);
            TextView singer_name= (TextView)findViewById(R.id.singer_name);

            signer_pic.setImageResource(signer_pic_source);
            music_name.setText(music_name_source);
            singer_name.setText(singer_name_source);
            audio= MediaPlayer.create(this,audioID_source);

            final ImageView pauseBtn=(ImageView)findViewById(R.id.pauseBtn);
            final ImageView startBtn=(ImageView)findViewById(R.id.startBtn);
            final ImageView nextBtn=(ImageView)findViewById(R.id.nextBtn);
            final ImageView backBtn=(ImageView)findViewById(R.id.backBtn);
            progressBar=(SeekBar)findViewById(R.id.songProgressBar);
            if(currentDuration!=0 && audio!=null)
                if(getIntent().getExtras()!=null){
                    audio.seekTo(getIntent().getIntExtra("CurrentDuration", 0)+3000);
                    progressBar.setProgress(getIntent().getIntExtra("CurrentDuration", 0)+3000);
                }else {
                    audio.seekTo(prefs.getInt("CurrentDuration", 0) + 3000);
                    progressBar.setProgress(prefs.getInt("CurrentDuration", 0) + 3000);
                }
            audio.start();
            pauseBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pauseBtn.setVisibility(View.INVISIBLE);
                    startBtn.setVisibility(View.VISIBLE);
                    audio.pause();
                }
            });
            startBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startBtn.setVisibility(View.INVISIBLE);
                    pauseBtn.setVisibility(View.VISIBLE);
                    audio.start();
                }
            });
            nextBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int currentPosition = audio.getCurrentPosition();
                    if(currentPosition + seekForwardBackwardTime <= audio.getDuration())
                        audio.seekTo(currentPosition + seekForwardBackwardTime);
                    else audio.seekTo(audio.getDuration());
                }
            });
            backBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int currentPosition = audio.getCurrentPosition();
                    if(currentPosition - seekForwardBackwardTime >= 0)
                        audio.seekTo(currentPosition - seekForwardBackwardTime);
                    else audio.seekTo(0);
                }
            });
            updateProgressBar();
        } catch (IllegalArgumentException e) {e.printStackTrace();
        } catch (IllegalStateException e) {e.printStackTrace();}
        progressBar.setOnSeekBarChangeListener(this);
        audio.setOnCompletionListener(this);
    }
    //SeekBak için runtime barın değerinin ilerletilmesi
    public void updateProgressBar() {mHandler.postDelayed(mUpdateTimeTask, 100);}
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
        convertor = new TimeConvertor();
        TextView totalDuration = (TextView) findViewById(R.id.finishDuration);
        totalDuration.setText("" + convertor.milliSecondsToTimer(audio.getDuration()));
        TextView duration = (TextView) findViewById(R.id.startDuration);
        duration.setText("" + convertor.milliSecondsToTimer(audio.getCurrentPosition()));
        int progress = (int) (convertor.getProgressPercentage(audio.getCurrentPosition(), audio.getDuration()));
        progressBar.setProgress(progress);
        mHandler.postDelayed(this, 100);
        }
    };
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {}
    public void onStartTrackingTouch(SeekBar seekBar) {mHandler.removeCallbacks(mUpdateTimeTask);}
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = audio.getDuration();
        int currentPosition = convertor.progressToTimer(seekBar.getProgress(), totalDuration);
        audio.seekTo(currentPosition);
        updateProgressBar();
    }
    // PlayScreen aktivitesinden geri dönüldüğünde o anki değerler prefte tutuldu
    // Eğer aynı ekran aynı değerlerle çağırılırsa preften okuma yapılacaktır.
    // Aksi halde intentle veriler aktarılacaktır.
    public void onBackPressed() {
        super.onDestroy();
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("ImageID", getIntent().getIntExtra("ImageID", -1));
        prefsEditor.putString("MusicName", getIntent().getExtras().getString("MusicName"));
        prefsEditor.putString("SignerName", getIntent().getExtras().getString("SignerName"));
        prefsEditor.putInt("AudioID", getIntent().getIntExtra("AudioID", 2131034112));
        prefsEditor.putInt("CurrentDuration", getIntent().getIntExtra("CurrentDuration", -1));
        prefsEditor.apply();
        if(audio!=null)
         audio.stop();
        Intent intent = new Intent(this, MainActivity.class);
        if(getIntent().getExtras()!=null) {
            intent.putExtra("ImageID", getIntent().getIntExtra("ImageID", 2130837511));
            intent.putExtra("MusicName", getIntent().getExtras().getString("MusicName"));
            intent.putExtra("CurrentDuration", (audio.getCurrentPosition() + 3500));
            intent.putExtra("AudioID", getIntent().getIntExtra("AudioID", 2131034112));
        } else{
            intent.putExtra("ImageID", prefs.getInt("ImageID", 2130837511));
            intent.putExtra("MusicName",  prefs.getString("MusicName", "Noname"));
            intent.putExtra("CurrentDuration", (audio.getCurrentPosition() + 3500));
            intent.putExtra("AudioID", prefs.getInt("AudioID", 2131034112));
        }
        startActivity(intent);
    }
    public void onCompletion(MediaPlayer arg0) {
        audio.stop();
        playSetting(getIntent().getIntExtra("ImageID", 2130837511),getIntent().getExtras().getString("MusicName"),getIntent().getExtras().getString("SignerName"),getIntent().getIntExtra("AudioID", 2131034112),0);
    }

}

