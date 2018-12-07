package com.quootta.mdate.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.quootta.mdate.constant.LocalConfig;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ryon on 2016/5/4/0004.
 */
public class FileUtil {

    public static void createApplicationFolder() {
        File f = new File(Environment.getExternalStorageDirectory(),
                File.separator + LocalConfig.FILE_DIR);
        if( !f.exists()){
            f.mkdirs();
        }
        f = new File(Environment.getExternalStorageDirectory(),
                File.separator + LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSED_VIDEOS_DIR);
        if (!f.exists()){
            f.mkdirs();
        }
        f = new File(Environment.getExternalStorageDirectory(),
                File.separator + LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSOR_TEMP_DIR);
        if (!f.exists()){
            f.mkdirs();
        }

    }

    public static File saveTempFile(String fileName, Context context, Uri uri) {

        File mFile = null;
        ContentResolver resolver = context.getContentResolver();
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = resolver.openInputStream(uri);
            mFile = new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSOR_TEMP_DIR, fileName);
            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            LogUtil.d("FileUtil","catch exception");
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return mFile;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            LogUtil.e("FileUtil", e.toString());
        }
    }
}
