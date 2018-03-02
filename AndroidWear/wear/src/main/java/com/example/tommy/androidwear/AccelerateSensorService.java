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

/**
 * Created by Tommy on 2017/10/13.
 */

public class AccelerateSensorService extends Service implements SensorEventListener{

    final String TAG = "SensorService";
    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Mbinder mbinder = new Mbinder();
    private MsgListener msgListener;
    private SimpleRate simpleRate;
    float gravity = (float)9.8;

    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        simpleRate = new SimpleRate();
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (msgListener == null){
//            return;
//        }else {
//            msgListener.getMsg(event.values[2]);
//        }
        if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        if (msgListener == null)
            return;
//        final float alpha = (float) 0.8;
//        gravity = alpha * gravity  + (1-alpha)*event.values[2];
//        float linearAcceleration = event.values[2]  - gravity;
        msgListener.getMsg(event.values[0],event.values[1],event.values[2]);
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
        public AccelerateSensorService getService(){
            return AccelerateSensorService.this;
        }
    }
}
