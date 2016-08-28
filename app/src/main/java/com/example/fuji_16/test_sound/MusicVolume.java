
package com.example.fuji_16.test_sound;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicVolume extends Service  implements Runnable {
    private final int REPEAT_INTERVAL = 5000;

    private MediaPlayer player = new MediaPlayer();
    // private AudioManager manager;

    private Handler handler = new Handler();
    private Runnable runnable;
    static int dB;
    private int vol;

    //bind
    private final IBinder mBinder = new MusicVolumeBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MVです", "onCreatでーす"  );
        runnable = new Runnable(){
            @Override
            public void run(){
                Change();
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        };
        handler.postDelayed(runnable, REPEAT_INTERVAL);
        //manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }
/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
*/

    public void Change() {
        /** Called when the activity is first created. */
        try {
            Log.d("MVです", "volは"+vol+"dBは"+dB  );
            vol=15;
            //dB=SoundSwitch.max;
            dB=vol;
            Log.d("MV２です", "volは"+vol+"dBは"+dB  );
            if(dB < 500) {
                //manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 4), 0);
            }else if(dB >= 2000){
                //manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 2), 0);
            }else{
                // manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 3), 0);
            }

            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override //別スレッド開始
    public void run() {
        Log.d("MVです", "running中です");
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Log.d("MVです", "通ってほしい");
        int ringVolume = manager.getStreamVolume(AudioManager.STREAM_RING);
        while (SoundSwitch.isRecoding) {


            int vol;
            vol = 15;
            dB=SoundSwitch.max;
            //dB = vol;
            Log.d("MVです", "db:" + dB + "です");
            if (dB < 500) {
                Log.d("MVです", dB + "なので4分の１にします");
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 4), 0);
                Log.d("MVです", dB + "なので4分の１にしました");
            } else if (dB >= 2000) {
                Log.d("MVです", dB + "なので2分の１にします");
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 2), 0);
                Log.d("MVです", dB + "なので2分の１にしました");
            } else {
                Log.d("MVです", dB + "なので3分の１にします");
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol / 3), 0);
                Log.d("MVです", dB + "なので3分の１にしました");
            }
            Log.d("MVです", "running1週終わり");
            Log.d("MVです", "1秒お休み"  );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public class MusicVolumeBinder extends Binder {
        MusicVolume getService() { return MusicVolume.this; }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return true;
    }
}
