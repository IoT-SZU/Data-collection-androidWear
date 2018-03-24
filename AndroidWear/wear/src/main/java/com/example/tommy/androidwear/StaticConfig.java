package com.example.tommy.androidwear;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommy on 2018/3/24.
 */

public class StaticConfig {
    public static String FTP_URL = "172.31.73.52";
    public static String USER_NAME = "Gryffindor";
    public static String PASSWORD = "Alohomora";
    public static int FTP_PORT = 21;
    public static String PATH = "/data";
    public static String FINAL_PATH  = PATH+ new SimpleDateFormat("yyyy_MM_dd_hhmm").format(new Date());
}
