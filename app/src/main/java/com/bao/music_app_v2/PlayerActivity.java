package com.bao.music_app_v2;

import static com.bao.music_app_v2.MainActivity.musicFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    TextView song_name, artist_name, duration_play, total_play;
    ImageView cover_art, btn_next, btn_pre, btn_back, btn_shuffle, btn_repeat;
    FloatingActionButton btn_playpause;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<MusicFiles> listSongs=new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    Handler handler=new Handler();

    Thread playThread, preThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null&&b){
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        metaData(uri);

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_play.setText(formatedTime(mCurrentPosition));

                }
                handler.postDelayed(this,1000);
            }
        });


    }

    @Override
    protected void onResume() {
        playThreadBtn();
        preThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                super.run();
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClick() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position=(position+1)%listSongs.size();
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            btn_playpause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=(position+1)%listSongs.size();
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            btn_playpause.setImageResource(R.drawable.ic_play);
        }
    }

    private void preThreadBtn() {
        preThread=new Thread(){
            @Override
            public void run() {
                super.run();
                btn_pre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preBtnClick();
                    }
                });
            }
        };
        preThread.start();
    }

    private void preBtnClick() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position -1)<0 ? (listSongs.size()-1): (position-1));
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            btn_playpause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position -1)<0 ? (listSongs.size()-1): (position-1));
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            btn_playpause.setImageResource(R.drawable.ic_play);
        }
    }

    private void playThreadBtn() {
        playThread=new Thread(){
            @Override
            public void run() {
                super.run();
                btn_playpause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClick();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClick() {
        if(mediaPlayer.isPlaying()){
            btn_playpause.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else
        {
            btn_playpause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formatedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds=String.valueOf(mCurrentPosition%60);
        String min=String.valueOf(mCurrentPosition/60);
        totalOut=min+":"+seconds;
        totalNew=min+":"+"0"+seconds;
        if(seconds.length()==1){
            return totalNew;
        }else{
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra("position", -1);
        listSongs= musicFiles;
        if(listSongs!=null){
            btn_playpause.setImageResource(R.drawable.ic_pause);
            uri=Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
    }

    private void initView() {
        song_name=findViewById(R.id.song_name);
        artist_name=findViewById(R.id.artist_name);
        duration_play=findViewById(R.id.duration_play);
        total_play=findViewById(R.id.total_play);

        cover_art=findViewById(R.id.cover_art);
        btn_next=findViewById(R.id.id_next);
        btn_pre=findViewById(R.id.id_pre);
        btn_back=findViewById(R.id.back_btn);
        btn_shuffle=findViewById(R.id.id_shuffle);
        btn_repeat=findViewById(R.id.id_repeat);

        btn_playpause=findViewById(R.id.play_pause);

        seekBar=findViewById(R.id.seek_bar);

    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal=Integer.parseInt(listSongs.get(position).getDuration())/1000;
        total_play.setText(formatedTime(durationTotal));
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art!=null){
            Glide.with(this).asBitmap().load(art).into(cover_art);
            bitmap=BitmapFactory.decodeByteArray(art,0,art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
                    if(swatch!=null){
                        ImageView gredient=findViewById(R.id.img_view_gredient);
                        RelativeLayout mContainer=findViewById(R.id.mContainer);
                        gredient.setBackgroundResource(R.drawable.gredient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),0x00000000});
                        gredient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBG=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBG);
                        song_name.setTextColor(swatch.getTitleTextColor());
                        artist_name.setTextColor(swatch.getBodyTextColor());
                    }else{
                        ImageView gredient=findViewById(R.id.img_view_gredient);
                        RelativeLayout mContainer=findViewById(R.id.mContainer);
                        gredient.setBackgroundResource(R.drawable.gredient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0x00000000});
                        gredient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBG=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0xff000000});
                        mContainer.setBackground(gradientDrawableBG);
                        song_name.setTextColor(Color.WHITE);
                        artist_name.setTextColor(Color.DKGRAY);
                    }
                }
            });
        }
        else
        {
            Glide.with(this).asBitmap().load(R.drawable.compact_disc).into(cover_art);
            ImageView gredient=findViewById(R.id.img_view_gredient);
            RelativeLayout mContainer=findViewById(R.id.mContainer);
            gredient.setBackgroundResource(R.drawable.gredient_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);

            song_name.setTextColor(Color.WHITE);
            artist_name.setTextColor(Color.DKGRAY);
        }
    }
}