package com.example.tommy.androidwear.Utils;

import android.os.StrictMode;
import android.util.Log;

import com.example.tommy.androidwear.StaticConfig;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by Tommy on 2018/1/17.
 */

public class FTPUtils {

    private static final String TAG = "FTPUtils";
    public static String PATH_NAME  ="/data/";
    private FTPClient ftpClient = null;
    private static FTPUtils ftpUtilsInstance = null;
    private String FTPUrl;
    private int FTPPort;
    private String UserName;
    private String UserPassword;

    private FTPUtils()
    {
        PATH_NAME  = StaticConfig.PATH;
        ftpClient = new FTPClient();
    }
    /*
     * 得到类对象实例（因为只能有一个这样的类对象，所以用单例模式）
     */
    public  static FTPUtils getInstance() {
        if (ftpUtilsInstance == null)
        {
            ftpUtilsInstance = new FTPUtils();
        }
        return ftpUtilsInstance;
    }
    /**
     * 设置FTP服务器
     * @param FTPUrl   FTP服务器ip地址
     * @param FTPPort   FTP服务器端口号
     * @param UserName    登陆FTP服务器的账号
     * @param UserPassword    登陆FTP服务器的密码
     * @return
     */
    public boolean initFTPSetting(String FTPUrl, int FTPPort, String UserName, String UserPassword)
    {
        this.FTPUrl = FTPUrl;
        this.FTPPort = FTPPort;
        this.UserName = UserName;
        this.UserPassword = UserPassword;

        int reply;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // 获得本机的所有网络接口
            Log.d("ftp info",FTPUrl+":"+FTPPort);
            //1.要连接的FTP服务器Url,Port

            ftpClient.connect(FTPUrl, FTPPort);

            Log.d("connected","success");

            //2.登陆FTP服务器
            ftpClient.login(UserName, UserPassword);


            //3.看返回的值是不是230，如果是，表示登陆成功
            reply = ftpClient.getReplyCode();
            Log.d("login","success");


            if (!FTPReply.isPositiveCompletion(reply))
            {
                //断开
                ftpClient.disconnect();
                return false;
            }

            return true;

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件
     * @param FilePath    要上传文件所在SDCard的路径
     * @param FileName    要上传的文件的文件名(如：Sim唯一标识码)
     * @return    true为成功，false为失败
     */
    public boolean uploadFile(String FilePath, String FileName) {

        Log.i("", "uploadFile: "+FilePath);
        Log.i("", "uploadFile: " +FileName);
        Log.i("", "uploadFile: "+PATH_NAME);
        if (!ftpClient.isConnected())
        {
            if (!initFTPSetting(FTPUrl,  FTPPort,  UserName,  UserPassword))
            {
                return false;
            }
        }

        try {
            //设置存储路径
            String path = PATH_NAME;

            CreateDirecroty(PATH_NAME);
            ftpClient.changeWorkingDirectory(PATH_NAME);

//            while (path.contains("/")){
//                path = path.substring(0,PATH_NAME.indexOf('/'));
//                String longPath = path.substring(PATH_NAME.indexOf('/'));
//                Log.i(TAG, "uploadFile: path : " + path);
//                Log.i(TAG, "uploadFile: long path : " + longPath);
//                ftpClient.makeDirectory(path);
//                ftpClient.changeWorkingDirectory(path);
//            }


            //设置上传文件需要的一些基本信息
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            //文件上传吧～
            FileInputStream fileInputStream = new FileInputStream(FilePath);
            ftpClient.storeFile(FileName, fileInputStream);

            //关闭文件流
            fileInputStream.close();

            //退出登陆FTP，关闭ftpCLient的连接
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 下载文件
     * @param FilePath  要存放的文件的路径
     * @param FileName   远程FTP服务器上的那个文件的名字
     * @return   true为成功，false为失败
     */
    public boolean downLoadFile(String FilePath, String FileName) {

        if (!ftpClient.isConnected())
        {
            if (!initFTPSetting(FTPUrl,  FTPPort,  UserName,  UserPassword))
            {
                return false;
            }
        }

        try {
            // 转到指定下载目录
            ftpClient.changeWorkingDirectory(PATH_NAME);

            //如果listFile比较慢，可以执行下面的方法试一试

           //ftpClient.enterLocalPassiveMode();
            // 列出该目录下所有文件
            FTPFile[] files = ftpClient.listFiles();

            // 遍历所有文件，找到指定的文件
            for (FTPFile file : files) {
                if (file.getName().equals(FileName)) {
                    //根据绝对路径初始化文件
                    File localFile = new File(FilePath);

                    // 输出流
                    OutputStream outputStream = new FileOutputStream(localFile);

                    // 下载文件
                    ftpClient.retrieveFile(file.getName(), outputStream);

                    //关闭流
                    outputStream.close();
                }
            }

            //退出登陆FTP，关闭ftpCLient的连接
            ftpClient.logout();
            ftpClient.disconnect();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    /** */
    /**
     * 递归创建远程服务器目录
     *
     * @param remote
     *            远程服务器文件绝对路径
     *
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/")
                && !ftpClient.changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end));
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录失败");
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

}
