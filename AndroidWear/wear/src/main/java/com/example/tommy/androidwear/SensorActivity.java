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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tommy on 2017/10/13.
 */

public class SensorActivity extends Activity implements AccelerateSensorService.MsgListener{

    private static final String TAG = "SensorActivity";
    private int durationTime;
    private TextView textViewValue;
    private DataView dataView;
    //静态变量用来缓存需要的数据
    public static ArrayList<Float> xArray = new ArrayList<>();
    public static ArrayList<Float> yArray = new ArrayList<>();
    public static ArrayList<Float> zArray = new ArrayList<>();
    //加速度服务
    private AccelerateSensorService sensorService;
    private Intent sensorServiceIntent;
    //录音服务
    AudioRecorder audioRecorder;

    TextView xTv,yTv,zTv;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        xArray = new ArrayList<>();
        yArray = new ArrayList<>();
        zArray = new ArrayList<>();

        xTv = (TextView)findViewById(R.id.xAxis);
        yTv = (TextView)findViewById(R.id.yAxis);
        zTv = (TextView)findViewById(R.id.zAxis);

        Intent intent = getIntent();

        durationTime = Integer.parseInt(intent.getExtras().get("time").toString());
        textViewValue = (TextView)findViewById(R.id.value);
        dataView = (DataView)findViewById(R.id.dataView);

        //绑定服务（用来检测加速度）
        sensorServiceIntent = new Intent(this,AccelerateSensorService.class);
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
            sensorService = ((AccelerateSensorService.Mbinder)service).getService();
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
        xArray = new ArrayList<>();
        yArray = new ArrayList<>();
        zArray = new ArrayList<>();
    }

    protected void onDestroy(){
        super.onDestroy();
        sensorService.unregister();
        stopService(sensorServiceIntent);
        unbindService(sensorServiceConnection);
        stopRecord();
        ifstop = 1;

        Log.d("count",count+"");
        Log.d("rawdata lenth", xArray.size() + "");
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
//        Log.d("count",count+"");
        //采样频率计算
//        if(time == 0){
//            time = System.currentTimeMillis();
//        }else{
//
//            diff = ((System.currentTimeMillis() - time)/1000.0);
//            Log.d("diff",diff+"" );
//            time = System.currentTimeMillis();
//        }
        //
//        dataView.updateView(z);
        //显示采样频率
//        textViewValue.setText("symrate= " + diff + "");
        //
        xArray.add(x);
        yArray.add(y);
        zArray.add(z);
        xTv.setText("x = " + x);
        yTv.setText("y = " + y);
        zTv.setText("z = " + z);
    }
}
