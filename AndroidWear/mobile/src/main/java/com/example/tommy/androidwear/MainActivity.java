package com.example.tommy.androidwear;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tommy.androidwear.Utils.FTPUtils;
import com.example.tommy.androidwear.Utils.FileTransfer;
import com.example.tommy.androidwear.Utils.PcmToWav;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    final String TAG = "MainActivity";
    final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SensorAudioRecords";
    Button button;
    TextView mlogboard;
    FileTransfer fileTransfer;
    EditText ipEt,userEt,passwordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.start);
        mlogboard = (TextView) findViewById(R.id.LogWindow);

        ipEt = (EditText)findViewById(R.id.ipEt);
        userEt = (EditText)findViewById(R.id.userEt);
        passwordEt = (EditText)findViewById(R.id.passwordEt);


        fileTransfer = new FileTransfer(this);
        fileTransfer.connect();
        mlogboard.setText("\tconnecting : "+String.valueOf(fileTransfer.isconnected())+"\n");
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileTransfer.connect();
        mlogboard.setText("\tconnecting : "+String.valueOf(fileTransfer.isconnected())+"\n");

    }

    @Override
    protected void onPause() {
        super.onPause();
        fileTransfer.disconnect();
    }

    public void onSend(View view){
        FTPUtils.getInstance().initFTPSetting(ipEt.getText().toString(),21,userEt.getText().toString(),passwordEt.getText().toString());
        File directory = new File(FILE_PATH);
        File[] files = directory.listFiles();
        for (File file :files){
            Log.i(TAG, "onSend: "+file.getName());
            if (FTPUtils.getInstance().uploadFile(file.getAbsolutePath(),file.getName())){
                Log.i(TAG, "onSend: "+file.getName()+"  --success");
                final String tmp_string = file.getName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tmp = (TextView) findViewById(R.id.LogWindow);
                        tmp.append("\n\thave sent file "+tmp_string +"\n");
                    }
                });
            }else{
                Log.i(TAG, "onSend: " + file.getName() + "   --failed");
            }

        }
    }

    public void onWav(View view){
        File directory = new File(FILE_PATH);
        File[] files = directory.listFiles();
        for (File file :files){
            if (file.getName().contains("audio")){
                String num = file.getName().substring(file.getName().indexOf('t')+1);
                Log.d(TAG, "onWav: "+file.getName()+" "+num);
                PcmToWav.makePCMFileToWAVFile(file.getAbsolutePath(),FILE_PATH+"/wavAudio"+num+".wav",false);
            }
        }
        if( files.length != 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tmp = (TextView) findViewById(R.id.LogWindow);
                    tmp.append("\n\thave converted audio data to wav type." + "\n");
                }
            });
        }
    }

    public void onDelete(View view){
        File directory = new File(FILE_PATH);
        File[] files = directory.listFiles();
        for (File file :files){
            file.delete();
        }
        if (files.length != 0 ){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tmp = (TextView) findViewById(R.id.LogWindow);
                    tmp.setText("\tdumped all data."+"\n");

                    tmp.append("\n\tconnecting : " + String.valueOf(fileTransfer.isconnected()) + "\n");
                }
            });
        }
    }


}
