package com.example.tommy.androidwear;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.example.tommy.androidwear.Audio.AudioRecorder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tommy on 2017/10/13.
 */

public class SensorActivity extends Activity implements SensorService.MsgListener{

    private static final String TAG = "SensorActivity";
    private int durationTime;
    private TextView textViewValue;
    private DataView dataView;

    //加速度服务
    private SensorService sensorService;
    private Intent sensorServiceIntent;
    //录音服务
    AudioRecorder audioRecorder;

    TextView xTv,yTv,zTv;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        xTv = (TextView)findViewById(R.id.xAxis);
        yTv = (TextView)findViewById(R.id.yAxis);
        zTv = (TextView)findViewById(R.id.zAxis);

        Intent intent = getIntent();

        durationTime = Integer.parseInt(intent.getExtras().get("time").toString());
        textViewValue = (TextView)findViewById(R.id.value);
        dataView = (DataView)findViewById(R.id.dataView);

        //绑定服务（用来检测加速度）
        sensorServiceIntent = new Intent(this,SensorService.class);
        bindService(sensorServiceIntent,sensorServiceConnection,BIND_AUTO_CREATE);
        startService(sensorServiceIntent);

        audioRecorder = AudioRecorder.getInstance();


        startRecord();
        Log.d("count",count+"");
        count = 0;
        ifstop = 0;
        //控制时间
        TimeControl();

    }
    public void startRecord(){
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY) {
            //初始化录音
            audioRecorder.createDefaultAudio("pcmData");
            audioRecorder.startRecord(null);
        }
    }


    public void stopRecord(){
        audioRecorder.stopRecord();
    }

    public void pauseRecord(){
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
            //暂停录音
            audioRecorder.pauseRecord();
        }
    }
    //加速度传感器服务
    ServiceConnection sensorServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sensorService = ((SensorService.Mbinder)service).getService();
            sensorService.setMsgListener(SensorActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sensorService.setMsgListener(null);
        }
    };


    void TimeControl(){
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {//创建一个线程来执行run方法中的代码
            @Override
            public void run() {
                finish();
            }
        };
        mTimer.schedule(mTimerTask, durationTime * 1000);//秒
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy(){
        super.onDestroy();
        sensorService.unregister();
        stopService(sensorServiceIntent);
        unbindService(sensorServiceConnection);
        stopRecord();
        ifstop = 1;

        Log.d("count",count+"");

    }



    //计算采样频率
    private long time = 0;
    private double diff = 0;
    static int count = 0;
    static int ifstop = 1;
    //
    @Override
    public void getMsg(float x,float y,float z) {
        if (ifstop == 1){
            return;
        }
        count++;

        xTv.setText("x = " + x);
        yTv.setText("y = " + y);
        zTv.setText("z = " + z);
    }
}
