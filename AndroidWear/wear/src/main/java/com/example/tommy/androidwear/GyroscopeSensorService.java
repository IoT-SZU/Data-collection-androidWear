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
 * Created by Tommy on 2018/3/5.
 */

public class GyroscopeSensorService extends Service implements SensorEventListener {

    final String TAG = "SensorService";
    private Sensor GyroscopeSensor;
    private GyroscopeSensorService.Mbinder mbinder = new Mbinder();
    private GyroscopeSensorService.MsgListener msgListener;
    private SimpleRate simpleRate;

    public static ArrayList<Float> xArrayList;
    public static ArrayList<Float> yArrayList;
    public static ArrayList<Float> zArrayList;

    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        simpleRate = new SimpleRate();
        GyroscopeSensor = SensorActivity.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        SensorActivity.sensorManager.registerListener(this, GyroscopeSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        xArrayList.clear();
        yArrayList.clear();
        zArrayList.clear();
        return mbinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xArrayList.add(event.values[0]);
        yArrayList.add(event.values[1]);
        zArrayList.add(event.values[2]);
    }

    public void unregister(){
        SensorActivity. sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface MsgListener{
        void getMsg(float x,float y,float z);
    }

    void setMsgListener(GyroscopeSensorService.MsgListener msgListener){
        this.msgListener = msgListener;
    }

    public class Mbinder extends Binder {
        public GyroscopeSensorService getService(){
            return GyroscopeSensorService.this;
        }
    }
}