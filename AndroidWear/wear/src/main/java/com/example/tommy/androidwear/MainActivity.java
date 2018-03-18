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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tommy.androidwear.Audio.AudioRecorder;
import com.example.tommy.androidwear.Audio.PcmToWav;
import com.example.tommy.androidwear.Utils.DBHelper;
import com.example.tommy.androidwear.Utils.FileTransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.tommy.androidwear.MainActivity.SaveState.Pre;

public class MainActivity extends Activity{

    public static String FILE_BASE_DIR;
    public static String PCM_PATH;
    private final String TAG = "MainActivity";
    private Button  startButton,genrateButton,wipeDataButton,saveDataButton;
    private EditText durationTime;
    private EditText FileNameSpace;
    private TextView CfileLabel;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    FileTransfer fileTransfer;
    static int Filenum = 1;
    public static int SStatus;
    public static class SaveState{
        static int Empty = 1,Pre = 2,Latest = 3;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("filenum",Filenum);
        outState.putString("filename", FileNameSpace.getText() + "");
        outState.putString("fileLabel", CfileLabel.getText()+"");
        outState.putInt("SStates",SStatus);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the files array
        SendingFiles = new ArrayList<File>();

        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);


        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                startButton = (Button)stub.findViewById(R.id.startButton);
                genrateButton = (Button)stub.findViewById(R.id.generateFile);
                durationTime = (EditText)stub.findViewById(R.id.durationTime);
                FileNameSpace = (EditText)stub.findViewById(R.id.EnterName);
                saveDataButton = (Button) stub.findViewById(R.id.saveData);
                wipeDataButton = (Button)stub.findViewById(R.id.wipeData);
                CfileLabel = (TextView) stub.findViewById(R.id.ShowFileName);
                Log.d("CFname+textview",(CfileLabel==null)+"" );
                genrateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Sending Num of Files:",SendingFiles.size()+"");
                        if (SStatus != SaveState.Empty) {
                            genrateButton.setEnabled(false);
                            saveDataButton.setEnabled(false);
                            startButton.setEnabled(false);
                            wipeDataButton.setEnabled(false);
                            FileNameSpace.setEnabled(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!fileTransfer.sendThroughFTP(
                                            FileNameSpace.getText() + "",
                                            (File[]) SendingFiles.toArray(new File[SendingFiles.size()]))){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MainActivity.this,"Failed!Check your connectivity!",Toast.LENGTH_SHORT).show();
                                                genrateButton.setEnabled(true);
                                                saveDataButton.setEnabled(true);
                                                startButton.setEnabled(true);
                                                wipeDataButton.setEnabled(true);
                                                FileNameSpace.setEnabled(true);
                                            }
                                        });
                                        return;
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this,"Upload finished!",Toast.LENGTH_SHORT).show();
                                            genrateButton.setEnabled(true);
                                            saveDataButton.setEnabled(true);
                                            startButton.setEnabled(true);
                                            wipeDataButton.setEnabled(true);
                                            FileNameSpace.setEnabled(true);
                                        }
                                    });
                                }
                            }).start();

                        }
                    }
                });

                wipeDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(SStatus != SaveState.Empty) {
                            Filenum = 1;
                            CfileLabel.setText("No." + Filenum);
                            SendingFiles.clear();
                            SStatus = SaveState.Empty;
                            Toast.makeText(MainActivity.this,"Wipe All data!",Toast.LENGTH_SHORT).show();
                            wipeDataButton.setEnabled(false);
                            genrateButton.setEnabled(false);
                        }else{
                            Toast.makeText(MainActivity.this,"Already reset!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onService();

                    }
                });
                saveDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(SStatus == SaveState.Latest) {
                            genrateButton.setEnabled(false);
                            wipeDataButton.setEnabled(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (SensorActivity.CStates != SensorActivity.States_Set.finished){
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {}
                                    }
                                    if (SensorService.xAcceArray.isEmpty()){
                                        return;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Generating File", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    onGenerate();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, Filenum + " Saved!", Toast.LENGTH_SHORT).show();
                                            Filenum++;
                                            CfileLabel.setText("No." + Filenum);
                                            startButton.setEnabled(true);
                                            genrateButton.setEnabled(true);
                                            wipeDataButton.setEnabled(true);
                                        }
                                    });
                                }
                            }).start();



                            SStatus = SaveState.Pre;


                            saveDataButton.setEnabled(false);

                        }
                    }
                });
            }
        });
        if (savedInstanceState == null) {
            SStatus = SaveState.Empty;
            Filenum = 1;
        } else{
//            outState.putInt("filenum",Filenum);
//            outState.putString("filename", FileNameSpace.getText() + "");
//            outState.putString("fileLabel", CfileLabel.getText()+"");
//            outState.putInt("SStates",SStatus);
            Log.d("onCreate","reload situation...");
            if (savedInstanceState.getInt("SStates") != SaveState.Empty){
                SStatus = SaveState.Pre;
                Filenum = savedInstanceState.getInt("filenum");
                FileNameSpace.setText(savedInstanceState.getString("filename"));
                CfileLabel.setText(savedInstanceState.getString("fileLabel"));
                genrateButton.setEnabled(true);
                wipeDataButton.setEnabled(true);
            }else{
                SStatus = SaveState.Empty;
                Filenum = 1;
            }

        }
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
        if(!saveDataButton.isEnabled()){
            saveDataButton.setEnabled(true);
        }
    }


    ArrayList<File> SendingFiles;
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
        //保存文件到手表中
        File xAcceDataFile = writeArrayFile(SensorService.xAcceArray,"xAcceDataAsset"+Filenum);
        File yAcceDataFile = writeArrayFile(SensorService.yAcceArray,"yAcceDataAsset"+Filenum);
        File zAcceDataFile = writeArrayFile(SensorService.zAcceArray,"zAcceDataAsset"+Filenum);

        File xGyroDataFile = writeArrayFile(SensorService.xGyroArray,"xGyroData"+Filenum);
        File yGyroDataFile = writeArrayFile(SensorService.yGyroArray,"yGyroData"+Filenum);
        File zGyroDataFile = writeArrayFile(SensorService.zGyroArray,"zGyroData"+Filenum);
//        File audioDataFile = new File(FileUtils.getWavFileAbsolutePath("test"));
//        Log.i(TAG, "onGenerate: " + FileUtils.getWavFileAbsolutePath("test"));

        File PcmFile = new File(PCM_PATH);
        Log.i(TAG, "onGenerate: audioDataFile size = " + PcmFile.length());
        Log.i(TAG, "onGenerate: " +PCM_PATH);
        // 保存Files文件
        SendingFiles.add(xAcceDataFile);
        SendingFiles.add(yAcceDataFile);
        SendingFiles.add(zAcceDataFile);
        SendingFiles.add(xGyroDataFile);
        SendingFiles.add(yGyroDataFile);
        SendingFiles.add(zGyroDataFile);
        SendingFiles.add(PcmFile);






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
