package com.example.tommy.androidwear;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.example.tommy.androidwear.Audio.AudioRecorder;

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
    public static SensorManager sensorManager;

    //加速度服务
    private AccelerateSensorService accelerateSensorService;
    private Intent acceSensorServiceIntent;
    //陀螺仪服务
    private GyroscopeSensorService gyroscopeSensorService;
    private Intent gyroscopeServiceIntent;
    //录音服务
    AudioRecorder audioRecorder;

    TextView xTv,yTv,zTv;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        xTv = (TextView)findViewById(R.id.xAxis);
        yTv = (TextView)findViewById(R.id.yAxis);
        zTv = (TextView)findViewById(R.id.zAxis);

        accelerateSensorService = new AccelerateSensorService();
        gyroscopeSensorService = new GyroscopeSensorService();

        Intent intent = getIntent();

        durationTime = Integer.parseInt(intent.getExtras().get("time").toString());
        textViewValue = (TextView)findViewById(R.id.value);
        dataView = (DataView)findViewById(R.id.dataView);

        //绑定服务（用来检测陀螺仪）
        gyroscopeServiceIntent = new Intent(this,GyroscopeSensorService.class);
        bindService(gyroscopeServiceIntent,gyroSensorServiceConnection,BIND_AUTO_CREATE);
        startService(gyroscopeServiceIntent);

        //绑定服务（用来检测加速度）
        acceSensorServiceIntent = new Intent(this,AccelerateSensorService.class);
        bindService(acceSensorServiceIntent,acceSensorServiceConnection,BIND_AUTO_CREATE);
        startService(acceSensorServiceIntent);



        audioRecorder = AudioRecorder.getInstance();


        startRecord();

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
    ServiceConnection acceSensorServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            accelerateSensorService = ((AccelerateSensorService.Mbinder)service).getService();
            accelerateSensorService.setMsgListener(SensorActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            accelerateSensorService.setMsgListener(null);
        }
    };

    //加速度传感器服务
    ServiceConnection gyroSensorServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gyroscopeSensorService = ((GyroscopeSensorService.Mbinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
        gyroscopeSensorService.unregister();
        accelerateSensorService.unregister();
        stopService(acceSensorServiceIntent);
        stopService(gyroscopeServiceIntent);
        unbindService(acceSensorServiceConnection);
        unbindService(gyroSensorServiceConnection);
        stopRecord();

    }



    @Override
    public void getMsg(float x,float y,float z) {

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

        xTv.setText("x = " + x);
        yTv.setText("y = " + y);
        zTv.setText("z = " + z);
    }
}
