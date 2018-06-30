package com.audioid.emre.audioid;

import java.io.File;

/**
 * Created by lenovo on 4.11.2017.
 */
public class RowItem {

    private String music_name;
    private int singer_pic_id;
    private String singer_name;
    private int audio;

    public RowItem(String music_name, int singer_pic_id, String singer_name,int audio) {

        this.music_name = music_name;
        this.singer_pic_id = singer_pic_id;
        this.singer_name = singer_name;
        this.audio=audio;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String member_name) {
        this.music_name = music_name;
    }

    public int getSinger_pic_id() {
        return singer_pic_id;
    }

    public void setSinger_pic_id(int singer_pic_id) {
        this.singer_pic_id = singer_pic_id;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String status) {
        this.singer_name = singer_name;
    }
    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio= audio;
    }
}