package com.example.tommy.androidwear.Audio;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.example.tommy.androidwear.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HXL on 16/8/11.
 * 管理录音文件的类
 */
public class FileUtils {

    //static final String FILE_BASE_DIR = "/data/user/0/com.example.tommy.mic/files";
    static final String FILE_BASE_DIR = MainActivity.FILE_BASE_DIR;
    static final String TAG = "FileUtils";

    private  static String rootPath="pauseRecordDemo";
    //原始文件(不能播放)
    private final static String AUDIO_PCM_BASEPATH = "/"+rootPath+"/pcm/";
    //可播放的高质量音频文件
    private final static String AUDIO_WAV_BASEPATH = "/"+rootPath+"/wav/";

    private static void setRootPath(String rootPath){
        FileUtils.rootPath=rootPath;
    }

    public static String getPcmFileAbsolutePath(String fileName){
        if(TextUtils.isEmpty(fileName)){
            throw new NullPointerException("fileName isEmpty");
        }

        String mAudioRawPath = "";
        if (!fileName.endsWith(".pcm")) {
            fileName = fileName + ".pcm";
        }
        //String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_PCM_BASEPATH;
        File file = new File(FILE_BASE_DIR);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioRawPath = FILE_BASE_DIR + fileName;

        return mAudioRawPath;
    }

    public static String getWavFileAbsolutePath(String fileName) {
        if(fileName==null){
            throw new NullPointerException("fileName can't be null");
        }

        String mAudioWavPath = "";

        if (!fileName.endsWith(".wav")) {
            fileName = fileName + ".wav";
        }
        //String fileBasePath = Environment.getRootDirectory().getAbsolutePath() + AUDIO_WAV_BASEPATH;
        File file = new File(FILE_BASE_DIR);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioWavPath = FILE_BASE_DIR + fileName;
        return mAudioWavPath;
    }

    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取全部pcm文件列表
     *
     * @return
     */
    public static List<File> getPcmFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = MainActivity.FILE_BASE_DIR + AUDIO_PCM_BASEPATH;
        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {

            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }

        }
        return list;

    }

    /**
     * 获取全部wav文件列表
     *
     * @return
     */
    public static List<File> getWavFiles() {
        List<File> list = new ArrayList<>();
        //String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_WAV_BASEPATH;

        File rootFile = new File(FILE_BASE_DIR+ AUDIO_WAV_BASEPATH);
        if (!rootFile.exists()) {
        } else {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }

        }
        return list;
    }
}
