package club.seliote.voa.GlobalUtils;

import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogToFile {

    /**
     * 每个方法调用了该方法获得返回值后，需要手动关闭返回的RandomAccessFile，否则很可能造成数据写入不完整
     * @return 用于写入日志的RandomAccessFile
     */
    @Nullable
    private static RandomAccessFile openLogFile() {
        RandomAccessFile randomAccessFile = null;
        try {
            File file = new File(GlobalContextApplication.getContext().getExternalCacheDir(), ConstantValue.LOG_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(file.length());
        } catch (IOException exp) {
            Toast.makeText(GlobalContextApplication.getContext(), "[0]日志写入出错！", Toast.LENGTH_LONG);
        }
        return randomAccessFile;
    }

    public static void i(String msg) {
        RandomAccessFile randomAccessFile = LogToFile.openLogFile();
        if (randomAccessFile == null) {
            return;
        }
        try {
            randomAccessFile.writeChars("[info ] : " + msg + "\n");
            randomAccessFile.close();
        } catch (IOException exp) {
            Toast.makeText(GlobalContextApplication.getContext(), "[1]日志写入出错！", Toast.LENGTH_LONG);
        }
    }

    public static void w(String msg) {
        RandomAccessFile randomAccessFile = LogToFile.openLogFile();
        if (randomAccessFile == null) {
            return;
        }
        try {
            randomAccessFile.writeChars("[warn ] : " + msg + "\n");
            randomAccessFile.close();
        } catch (IOException exp) {
            Toast.makeText(GlobalContextApplication.getContext(), "[1]日志写入出错！", Toast.LENGTH_LONG);
        }
    }

    public static void e(String msg) {
        RandomAccessFile randomAccessFile = LogToFile.openLogFile();
        if (randomAccessFile == null) {
            return;
        }
        try {
            randomAccessFile.writeChars("[error] : " + msg + "\n");
            randomAccessFile.close();
        } catch (IOException exp) {
            Toast.makeText(GlobalContextApplication.getContext(), "[1]日志写入出错！", Toast.LENGTH_LONG);
        }
    }

}
