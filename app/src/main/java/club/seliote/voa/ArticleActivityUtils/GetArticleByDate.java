package club.seliote.voa.ArticleActivityUtils;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.GlobalUtils.FromUrlRequestString;
import club.seliote.voa.GlobalUtils.GlobalContextApplication;
import club.seliote.voa.GlobalUtils.LogToFile;

public class GetArticleByDate {

    public static ArticleHolder get(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date);
        File folder = new File(GlobalContextApplication.getContext().getExternalCacheDir().getPath() + "/articles/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, dateString);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                try (FileWriter fileWriter = new FileWriter(file)) {
                    ArticleHolder articleHolder = GetArticleByDate.getInInternet(dateString);
                    List<String> englishList = articleHolder.getEnglishList();
                    List<String> chineseList = articleHolder.getChineseList();
                    for (int index = 0; index < englishList.size(); ++index) {
                        fileWriter.write(englishList.get(index) + "\n");
                        fileWriter.write(chineseList.get(index) + "\n");
                    }
                    return articleHolder;
                }
            }
        } catch (IOException exp) {
            LogToFile.e("occur error when write article");
        }
        return GetArticleByDate.getInLocal(dateString);
    }

    private static ArticleHolder getInLocal(String dateString) {
        List<String> englishList = new ArrayList<>();
        List<String> chineseList = new ArrayList<>();
        File file = new File(GlobalContextApplication.getContext().getExternalCacheDir().getPath() + "/articles/" + dateString);
        if (!file.exists()) {
            LogToFile.e("article not in sdcard, file name : " + dateString);
            Toast.makeText(GlobalContextApplication.getContext(), "文件访问出错～快点联系我吧～", Toast.LENGTH_LONG).show();
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (count == 0) {
                    englishList.add(line);
                } else {
                    chineseList.add(line);
                }
                count = (count == 0 ? 1 : 0);
            }
        } catch (IOException exp) {
            LogToFile.e("article is in adcard, but could not read it");
        }
        ArticleHolder articleHolder = new ArticleHolder(englishList, chineseList);
        return articleHolder;
    }

    private static ArticleHolder getInInternet(String dateString) {
        List<String> englishList = new ArrayList<>();
        List<String> chineseList = new ArrayList<>();
        String urlString = ConstantValue.GET_ARTICLE_BASE_URL + dateString + ".php";
        FromUrlRequestString.RequestInfo requestInfo = new FromUrlRequestString.RequestInfo(urlString, ConstantValue.GET_METHOD, null);
        FromUrlRequestString fromUrlRequestString = new FromUrlRequestString();
        String rawData = "";
        try {
            rawData = fromUrlRequestString.execute(requestInfo).get();
        } catch (InterruptedException|ExecutionException exp) {
            LogToFile.e("get article error");
        }
        Pattern englishPattern = Pattern.compile("<br/><br/>([^<]{5,}?)<br/>");
        Matcher englishMatcher = englishPattern.matcher(rawData);
        while (englishMatcher.find()) {
            englishList.add(englishMatcher.group(1));
        }
        Pattern chinesePattern = Pattern.compile("<br/>([^<]{5,}?)<br/><br/><br/>");
        Matcher chineseMatcher = chinesePattern.matcher(rawData);
        while (chineseMatcher.find()) {
            chineseList.add(chineseMatcher.group(1));
        }
        ArticleHolder articleHolder = new ArticleHolder(englishList, chineseList);
        GetArticleByDate.writeToExternalCacheDir(articleHolder);
        return articleHolder;
    }

    private static void writeToExternalCacheDir (ArticleHolder articleHolder) {

    }

}
