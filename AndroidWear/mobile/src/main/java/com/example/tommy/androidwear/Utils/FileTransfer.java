package com.example.tommy.androidwear.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tommy.androidwear.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/16.
 */

public class FileTransfer implements DataApi.DataListener{
    //输出提示信息
    final Activity mainActivity;
    Toast mToast;
    String tmp_string="";
    //
    final String ASSET_NAME_ARRAY[] = {"xDataAsset","yDataAsset","zDataAsset","audioAsset"};
    final String TAG = "FileTransfer";
    final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SensorAudioRecords";
    GoogleApiClient mGoogleApiClient;
    //init Connection
    public FileTransfer(Context ctx){
        //
        mainActivity = (Activity) ctx;
        //
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Wearable.DataApi.addListener(mGoogleApiClient,FileTransfer.this);
                        Log.i(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.i(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
    }

    public void connect(){
        mGoogleApiClient.connect();
    }

    public boolean isconnected(){
        return  mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting();
    }

    public void disconnect(){
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    public void send(String type,String key,String msg){
        PutDataMapRequest dataMap = PutDataMapRequest.create(File.pathSeparator + type);
        DataMap dm = dataMap.getDataMap();
        dm.putString(key, msg);
        PutDataRequest request = dataMap.asPutDataRequest();
        if (mGoogleApiClient.isConnected()) {
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    String timestr = (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date());
                    Log.i("-------------", "Sending status: " + dataItemResult.getStatus().isSuccess() + " at time " + timestr);
                }
            });
        }else{
            Log.i(TAG, "send: not connected");
        }
    }
    public void send(String type,String key,File file){
        //将文件写到Asset中
        PutDataMapRequest dataMap = PutDataMapRequest.create(File.pathSeparator + type);
        DataMap dm = dataMap.getDataMap();
        try {
            Asset asset = Asset.createFromFd(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            dm.putAsset(key,asset);
            PutDataRequest request = dataMap.asPutDataRequest();
            if (mGoogleApiClient.isConnected()) {
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        String timestr = (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date());
                        Log.i("-------------", "Sending status: " + dataItemResult.getStatus().isSuccess() + " at time " + timestr);
                    }
                });
            }else{
                Log.i(TAG, "send: not connected");
            }
        }catch (Exception e){
            Log.i(TAG, "Error:"+e.toString());
        }

    }

    public void send(String type,String[] key,File[] file){
        if (key.length != file.length){
            Log.i(TAG, "Error :key length not equal to file length");
            return;
        }

        //将文件写到Asset中
        PutDataMapRequest dataMap = PutDataMapRequest.create("/" + type);
        DataMap dm = dataMap.getDataMap();
        try {
            for (int i = 0 ; i<file.length;i++){
                Asset asset = Asset.createFromFd(ParcelFileDescriptor.open(file[i], ParcelFileDescriptor.MODE_READ_ONLY));
                dm.putAsset(key[i],asset);
            }

            PutDataRequest request = dataMap.asPutDataRequest();
            if (mGoogleApiClient.isConnected()) {
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        String timestr = (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date());
                        Log.i("-------------", "Sending status: " + dataItemResult.getStatus().isSuccess() + " at time " + timestr);
                    }
                });
            }else{
                Log.i(TAG, "send: not connected");
            }
        }catch (Exception e){
            Log.i(TAG, "Error:"+e.toString());
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.i(TAG, "onDataChanged: Received a data map");
        for (DataEvent event : dataEventBuffer){
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.i(TAG, "onDataChanged: DataEvent changed");
                DataItem item = event.getDataItem();
                Log.i(TAG, "DataPath = " + item.getUri().getPath()+"\n");

                //如果需要拓展的话，需要重写onDataChanged
                if (item.getUri().getPath().compareTo("/send_file") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    saveAsset(dataMap);
                    Log.i(TAG, "onDataChanged: finished");
                }else if (item.getUri().getPath().compareTo("/send_msg") == 0){
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final String msg = dataMap.getString("msg");
                    Log.e(TAG, "Received A msg: " + msg);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i(TAG, "DataEvent = deleted");
            }
        }
    }
    
    private void saveAsset(DataMap dataMap){
        File dir = new File(FILE_PATH);
        dir.mkdir();
        for (int i = 0; i < ASSET_NAME_ARRAY.length;i++){
            int cnt= 0;
            if(dataMap.get(ASSET_NAME_ARRAY[i]) != null){
                Asset asset = dataMap.get(ASSET_NAME_ARRAY[i]);
                String fileName = ASSET_NAME_ARRAY[i] + cnt ;
                File file = new File(dir,fileName);
                while (file.exists()){
                    fileName = ASSET_NAME_ARRAY[i] + cnt++;
                    file = new File(dir,fileName);
                }
                saveFileFromAssetToDisk(asset,dir,fileName);
            }
        }
        Log.i(TAG, "saveAsset: finished");
    }

    private void saveFileFromAssetToDisk(final Asset asset, final File dir, final String filename) {
        if(asset == null){
            return ;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParcelFileDescriptor pfd = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getFd();
                FileDescriptor fd = pfd.getFileDescriptor();
                FileInputStream fis = new FileInputStream(fd);
                File output = new File(dir, filename);
                try {
                    FileOutputStream fos = new FileOutputStream(output);
                    byte[] buffer = new byte[1024];
                    int readinLen = 0;
                    while ((readinLen = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, readinLen);
                    }
                    fos.flush();
                    fos.close();
                    Log.i("--------", "run: finished" + output.getAbsolutePath());
                    final String msg = output.getName();
//                    mainActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mToast == null) {
//                                mToast = Toast.makeText(mainActivity, "", Toast.LENGTH_SHORT);
//                            }
//                            mToast.setText("received" + msg);
//                            mToast.show();
//                        }
//                    });
                    tmp_string = msg;
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tmp = (TextView) mainActivity.findViewById(R.id.LogWindow);
                            tmp.append("\n\tdata collected:"+tmp_string+"\n");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
