package com.example.tommy.androidwear;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tommy.androidwear.Utils.DBHelper;
import com.example.tommy.androidwear.Utils.FileTransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity{

    public static String FILE_BASE_DIR;
    public static String PCM_PATH;
    private final String TAG = "MainActivity";
    private Button  startButton,genrateButton,wipeDataButton;
    private EditText durationTime;
    private EditText FileNameSpace;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    FileTransfer fileTransfer;
    static int Filenum = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                startButton = (Button)stub.findViewById(R.id.startButton);
                genrateButton = (Button)stub.findViewById(R.id.generateFile);
                durationTime = (EditText)stub.findViewById(R.id.durationTime);
                FileNameSpace = (EditText)stub.findViewById(R.id.EnterName);

                wipeDataButton = (Button)stub.findViewById(R.id.wipeData);

                genrateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGenerate();
                    }
                });

                wipeDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Filenum = 1;
                    }
                });
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onService();
                    }
                });
            }
        });
        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        fileTransfer = new FileTransfer(this);

        FILE_BASE_DIR =  getFilesDir().getAbsolutePath()+File.separator;
        Log.i(TAG, "onCreate: " + FILE_BASE_DIR);
    }

    void onService() {
        dbHelper.onUpgrade(sqLiteDatabase,1,1);
        SensorService.xAcceArray.clear();
        SensorService.yAcceArray.clear();
        SensorService.zAcceArray.clear();

        SensorService.xGyroArray.clear();
        SensorService.yGyroArray.clear();
        SensorService.zGyroArray.clear();
        SensorActivity.count = 0;
        String time = durationTime.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("time",time);
        intent.setClass(MainActivity.this,SensorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileTransfer.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        fileTransfer.disconnect();
    }


    void onGenerate(){
        Toast.makeText(this,"Generating File", Toast.LENGTH_SHORT).show();
        //保存文件到手表中
        File xAcceDataFile = writeArrayFile(SensorService.xAcceArray,"xAcceData"+Filenum+".txt");
        File yAcceDataFile = writeArrayFile(SensorService.yAcceArray,"yAcceData"+Filenum+".txt");
        File zAcceDataFile = writeArrayFile(SensorService.zAcceArray,"zAcceData"+Filenum+".txt");

        File xGyroDataFile = writeArrayFile(SensorService.xGyroArray,"xGyroData"+Filenum+".txt");
        File yGyroDataFile = writeArrayFile(SensorService.yGyroArray,"yGyroData"+Filenum+".txt");
        File zGyroDataFile = writeArrayFile(SensorService.zGyroArray,"zGyroData"+Filenum+".txt");
//        File audioDataFile = new File(FileUtils.getWavFileAbsolutePath("test"));
//        Log.i(TAG, "onGenerate: " + FileUtils.getWavFileAbsolutePath("test"));
        File audioDataFile = new File(PCM_PATH);
        Log.i(TAG, "onGenerate: audioDataFile size = " + audioDataFile.length());
        Log.i(TAG, "onGenerate: " +PCM_PATH);
        //使用封装好的FileTransfer发送request
        fileTransfer.sendThroughFTP(
                FileNameSpace.getText()+""
                ,new String[]{"xAcceDataAsset","yAcceDataAsset","zAcceDataAsset","audioAsset","xGyroDataAsset","yGyroDataAsset","zGyroDataAsset"}
                ,new File[]{xAcceDataFile,yAcceDataFile,zAcceDataFile,audioDataFile,xGyroDataFile,yGyroDataFile,zGyroDataFile});
    }

    File writeArrayFile(ArrayList<Float> arrayList, String file_name){
        if (arrayList == null){
            Log.e(TAG, "writeArrayFile: arrayList is null");
        }
        File basedir = getFilesDir();
        File logFile = new File(basedir, file_name);
        try {
            FileOutputStream logStream = new FileOutputStream(logFile);
            PrintWriter logPrintWriter = new PrintWriter(logStream);

            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()){
                float data = (float) iterator.next();
                logPrintWriter.write(data+ "    ");
            }

            logPrintWriter.flush();
            logPrintWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return logFile;
    }

}
