package com.quootta.mdate.constant;

/**
 * Created by kky on 2016/4/25.
 */
public class LocalConfig {

    /**
     * 群呼单价
     */

    public static final int GROUP_CALL_PRIZE = 1000;

    /**
     * Cookie相关
     */
    private static final String SET_COOKIE_KEY = "set-cookie";
    private static final String COOKIE_KEY = "Cookie";

    /**
     * 媒体文件相关请求码
     */
    public static final int REQUEST_CODE_CAMARA = 0;
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_GALLERY_VIDEO = 2;

    /**
     * 文件路径相关
     */
    public static final String FILE_DIR = "chunhuaTemp/";
    public static final String FILE_COMPRESSOR_TEMP_DIR = "Temp/";
    public static final String FILE_COMPRESSED_VIDEOS_DIR = "CompressedVideos/";
    
    public static final String SELFIE_NAME = "selfie.jpg";
    public static final String DRIVER_NAME = "driver.jpg";
    public static final String AVATAR_NAME = "avatar.jpg";
    public static final String ALBUM_NAME = "album.jpg";
    public static final String VIDEO_TEMP_NAME = "temp.mp4";
    public static final String VIDEO_NAME = "compressed.mp4";


    public static final String SELFIE_PATH = getPath("selfie.jpg");
    public static final String DRIVER_PATH = getPath("driver.jpg");
    public static final String AVATAR_PATH = getPath("avatar.jpg");
    public static final String ALBUM_PATH = getPath("album.jpg");
    public static final String VIDEO_PATH = getVideoPath("compressed.mp4");

    public static String getVideoPath(String fileName) {
        StringBuffer path = new StringBuffer(FILE_DIR);
        path.append(FILE_COMPRESSED_VIDEOS_DIR);
        path.append(fileName);
        return path.toString();
    }

    public static String getPath(String fileName) {
        StringBuffer path = new StringBuffer(FILE_DIR);
        path.append(fileName);
        return path.toString();
    }
}
