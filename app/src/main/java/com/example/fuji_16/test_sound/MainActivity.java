package com.example.fuji_16.test_sound;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener{
    private SoundSwitch mSoundSwitch;
    private MusicVolume mMusicVolume;
    private Handler mHandler = new Handler();
    private TextView maintext;
    private int flag = 0;
    private Button start_button;
    private Button stop_button;
    private boolean rokuon;
    //結合・・・トミmain
    MusicVolume musicVolume = null;
    Intent serviceIntent;
    SoundSwitch soundswitch=null;
    boolean connectionStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        short max =0;
        super.onCreate(savedInstanceState);
        Log.d("MAです", "onCreatでせーす"  );
        mTimer.schedule(mTimerTask,0,1000);



        setContentView(R.layout.activity_main);

        start_button = (Button)findViewById(R.id.start_button);
        start_button.setOnClickListener(this);

        stop_button = (Button)findViewById(R.id.stop_button);
        stop_button.setOnClickListener(this);
        //結合・・・トミmain
        //serviceIntent = new Intent(this, MusicVolume.class);
       // startService(serviceIntent);
       // stopService(serviceIntent);
    }
    @Override
    public void onClick(View v) {//ボタンが押された処理すべて
        TextView textView=(TextView)findViewById(R.id.mainText);
        TextView textView555=(TextView)findViewById(R.id.textView555);
        TextView textView2=(TextView)findViewById(R.id.SouonText);//デバック用
        TextView textView3=(TextView)findViewById(R.id.testtext);//デバック用

        switch(v.getId()) {

            case R.id.start_button:    //開始ボタン(id=startbutton)が押された時
                Log.d("テストテスト", "スタートボタン押された："+rokuon  );
                if(rokuon != true) {

                    int text=555;
                    textView555.setText("適音kaisi" );
                    Log.d("text_name", "textんは555が入るよー：:" + text );
                    onResume1();
                }
                Log.d("text_name", "textんは555が入るよー：:かも"  );
                //startService(serviceIntent);
                textView.setText("適音開始");
                textView2.setText("騒音量:"+SoundSwitch.max+"");
                textView3.setText("騒音量:"+MusicVolume.dB+"だよ～");
                //Bind
                bindService(new Intent(MainActivity.this, MusicVolume.class), serviceConnection, Context.BIND_AUTO_CREATE);
                connectionStatus = true;
                break;
            case R.id.stop_button:    //終了ボタン(id=stopbutton)が押された時
                if (rokuon == true) {
                    onPause();
                }
                //serviceIntent = new Intent(this, MusicVolume.class);
                //stopService(serviceIntent);
                textView.setText("適音終了");
                textView2.setText("騒音量:"+SoundSwitch.max+"");
                //bind
                if(connectionStatus){
                    unbindService(serviceConnection);
                }
                connectionStatus = false;
               // textView3.setText(String.valueOf(flag));
                break;

        }
    }

    //@Override
    public void onResume1() {
        super.onResume();
        Log.d("MAです", "onREsume1開始"  );
        mSoundSwitch = new SoundSwitch();// リスナーを登録して音を感知できるように
        mMusicVolume = new MusicVolume();
        rokuon = mSoundSwitch.start();
        Log.d("MAです", "rokuon:"+rokuon  );
        new Thread(mSoundSwitch).start();// 別スレッドとしてSoundSwitchを開始（録音を開始）
        Log.d("MAです", "MVのスレッド開始"  );
        new Thread(mMusicVolume).start();
    }


    @Override
    public void onPause() {

        //Activityの状態がonPauseの時の処理
        super.onPause();//superクラスのonPauseを呼び出す
        rokuon= mSoundSwitch.stop();// 録音を停止
    }

    public class MainTimeTask extends TimerTask{
        @Override
        public void run(){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView textView5=(TextView)findViewById(R.id.SouonText);//デバック用
                    textView5.setText("騒音量:"+SoundSwitch.max+"");
                }
            });
        }
    }
    Timer mTimer= new Timer();
    TimerTask mTimerTask=new MainTimeTask();
    Handler getmHandler = new Handler();

    //結合・・・トミmain
    private ServiceConnection serviceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder service){
            musicVolume = ((MusicVolume.MusicVolumeBinder)service).getService();
            soundswitch = ((SoundSwitch.SoundSwitchBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName name){
            //TODO Auto-generated method stub
            musicVolume = null;
            soundswitch = null;
        }

    };

    // /////////////////////////////////


}