package com.example.tommy.androidwear;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.tommy.androidwear.Utils.SimpleRate;

import java.util.ArrayList;

/**
 * Created by Tommy on 2017/10/13.
 */

public class SensorService extends Service implements SensorEventListener{

    final String TAG = "SensorService";
    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor gryoscopeSensor;
    private Mbinder mbinder = new Mbinder();
    private MsgListener msgListener;
    private SimpleRate simpleRate;
    float gravity = (float)9.8;

    //静态变量用来缓存需要的数据
    public static ArrayList<Float> xAcceArray = new ArrayList<>();
    public static ArrayList<Float> yAcceArray = new ArrayList<>();
    public static ArrayList<Float> zAcceArray = new ArrayList<>();
    //静态变量用来缓存需要的数据
    public static ArrayList<Float> xGyroArray = new ArrayList<>();
    public static ArrayList<Float> yGyroArray = new ArrayList<>();
    public static ArrayList<Float> zGyroArray = new ArrayList<>();

    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        simpleRate = new SimpleRate();
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gryoscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,gryoscopeSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (msgListener == null)
            return;
        synchronized (this){
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    msgListener.getMsg(event.values[0],event.values[1],event.values[2]);
                    xAcceArray.add(event.values[0]);
                    yAcceArray.add(event.values[1]);
                    zAcceArray.add(event.values[2]);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    xGyroArray.add(event.values[0]);
                    yGyroArray.add(event.values[1]);
                    zGyroArray.add(event.values[2]);
                    break;
            }
        }

    }

    public void unregister(){
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface MsgListener{
        void getMsg(float x,float y,float z);
    }

    void setMsgListener(MsgListener msgListener){
        this.msgListener = msgListener;
    }

    public class Mbinder extends Binder{
        public SensorService getService(){
            return SensorService.this;
        }
    }
}
