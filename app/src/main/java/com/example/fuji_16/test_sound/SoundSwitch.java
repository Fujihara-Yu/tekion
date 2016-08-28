/*このプログラムが古川わからないです助けて*/

package com.example.fuji_16.test_sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.util.Log;

/**
 * Created by fuji_16 on 2016/08/22.
 */
public class SoundSwitch implements Runnable {

    // ボリューム感知リスナー
    private OnReachedVolumeListener mListener;

    // 録音中フラグ
    //private boolean isRecoding = false;
    static boolean isRecoding = false;

    // サンプリングレート
    private static final int SAMPLE_RATE = 8000;//80.0KHz

    // ボーダー音量
    private short mBorderVolume = 100;

    static short max = 0;
    static short average = 0;


    // ボーダー音量をセット
    public void setBorderVolume(short volume) {
        mBorderVolume = volume;
    }

    // ボーダー音量を取得
    public short getBorderVolume() {
        return mBorderVolume;
    }

    // 録音を停止
    public boolean stop() {
        isRecoding = false;
        return isRecoding;
    }
    public boolean start() {
        Log.d("SSです", "録音開始だよー"  );
        isRecoding = true;
        return isRecoding;
    }


    // OnReachedVolumeListenerをセット
    public void setOnVolumeReachedListener(OnReachedVolumeListener listener) {
        mListener = listener;
    }
    // ボーダー音量を検知した時のためのリスナー
    public interface OnReachedVolumeListener {//ボーダー音量を超える音量を検知した時に呼び出されるメソッドです。

        void onReachedVolume(short volume);

    }
    // スレッド開始（録音を開始）
    public void run() {
        Log.d("SSです", "running中"  );
        android.os.Process.setThreadPriority(
                android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        int bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
        short[] buffer = new short[bufferSize];
        audioRecord.startRecording();
        while(isRecoding) {
            audioRecord.read(buffer, 0, bufferSize);
            //short max = 0;
            max=buffer[0];//確認

            for (int i=0; i<bufferSize; i++) {// 最大音量を計算
                //max= buffer[i];
                //average=buffer[i];
                //max = (short)Math.max(max, buffer[i]);// 最大音量がボーダーを超えていたら
               // Log.d("SSです", "捜索中"  );
            }
            Log.d("SSです", "大きい値みつけました"  );
            try{
                Log.d("SSです", "1秒お休み"  );
                Thread.sleep(1000);
            }catch (InterruptedException e){
            }
            Log.d("SSです", "おはよう"  );
            //max=0;



        }
        audioRecord.stop();
        audioRecord.release();
    }

    public class SoundSwitchBinder extends Binder {
        SoundSwitch getService() { return SoundSwitch.this; }
    }
}