package club.seliote.voa.ArticleActivityUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.GlobalUtils.GlobalContextApplication;
import club.seliote.voa.GlobalUtils.LogToFile;

public class GetMedia {

    private Date mDate;
    private boolean mDownloadSuccess = false;

    public GetMedia(@NonNull Date date) {
        mDate = date;
    }

    @Nullable
    public File get() {
        File file = this.getInLocal();
        if (file != null && file.exists() && file.length() != 0) {
            return file;
        }
        this.download();
        return null;
    }

    @Nullable
    private File getInLocal() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File folder = new File(GlobalContextApplication.getContext().getExternalCacheDir().getPath() + "/media/");
        if (!folder.exists()) {
            folder.mkdir();
            return null;
        }
        File file = new File(folder, simpleDateFormat.format(mDate) + ".mp3");
        if (file.exists() && file.length() != 0) {
            return file;
        }
        return null;
    }

    private void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    File folder = new File(GlobalContextApplication.getContext().getExternalCacheDir().getPath() + "/media/");
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    File file = new File(folder, "tmp.mp3");
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        String urlString = ConstantValue.GET_MEDIA_BASE_URL + simpleDateFormat.format(mDate) + ".mp3";
                        URL url = new URL(urlString);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(6000);
                        httpURLConnection.setReadTimeout(8000);
                        try (InputStream inputStream = httpURLConnection.getInputStream()) {
                            byte[] bytes = new byte[1024];
                            int len;
                            while ((len = inputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes);
                            }
                        }
                    }
                    File destFile = new File(folder, simpleDateFormat.format(mDate) + ".mp3");
                    file.renameTo(destFile);
                    GetMedia.this.mDownloadSuccess = true;
                } catch (IOException exp) {
                    LogToFile.e("save mp3 error, date: " + mDate.toString());
                }
            }
        }).start();
    }
}
