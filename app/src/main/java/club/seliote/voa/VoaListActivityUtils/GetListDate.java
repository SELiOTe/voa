package club.seliote.voa.VoaListActivityUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.GlobalUtils.FromUrlRequestString;
import club.seliote.voa.GlobalUtils.GlobalContextApplication;
import club.seliote.voa.GlobalUtils.LogToFile;
import club.seliote.voa.GlobalUtils.VoaSQLiteOpenHelper;

public class GetListDate {

    public static void sync() {
        List<Date> datesFromWeb = GetListDate.getFromWebsite();
        List<Date> datesFromSqlite = GetListDate.getFromSqlite();
        if (datesFromSqlite.size() != datesFromWeb.size()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            VoaSQLiteOpenHelper voaSQLiteOpenHelper = new VoaSQLiteOpenHelper(GlobalContextApplication.getContext(), ConstantValue.DbSchema.DB_NAME, ConstantValue.DbSchema.LATEST_VERSION);
            SQLiteDatabase sqLiteDatabase = voaSQLiteOpenHelper.getWritableDatabase();
            List<Date> datesDiff = diffList(datesFromWeb, datesFromSqlite);
            ContentValues contentValues = new ContentValues();
            for (Date date : datesDiff) {
                contentValues.put(ConstantValue.DbSchema.VoaDb.DateList.DATE, simpleDateFormat.format(date));
                sqLiteDatabase.insert(ConstantValue.DbSchema.VoaDb.DATE_LIST_TABLE_NAME, null, contentValues);
                contentValues.clear();
            }
        }
    }

    // 记得优化
    public static List<Date> diffList(List<Date> longList, List<Date> shortList) {
        List<Date> result = new ArrayList<>();
        int longSize = longList.size();
        int shortSize = shortList.size();
        Date outerDate = null;
        Date innerDate = null;
        for (int longIndex = 0; longIndex < longSize; ++longIndex) {
            outerDate = longList.get(longIndex);
            int shortIndex = 0;
            for (; shortIndex < shortSize; ++shortIndex) {
                innerDate = shortList.get(shortIndex);
                if (outerDate.equals(innerDate)) {
                    break;
                }
            }
            if (shortIndex == shortSize) {
                result.add(outerDate);
            }
        }
        return result;
    }

    // get list from website
    public static final List<Date> getFromWebsite() {
        FromUrlRequestString.RequestInfo requestInfo = new FromUrlRequestString.RequestInfo(ConstantValue.GET_VOA_LIST_WEBSITE, ConstantValue.GET_METHOD, null);
        FromUrlRequestString fromUrlRequestString = new FromUrlRequestString();
        List<Date> dates = new ArrayList<>();
        String dateJson = null;
        try {
            dateJson = fromUrlRequestString.execute(requestInfo).get();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            JSONArray jsonArray = new JSONArray(dateJson);
            for (int index = 0; index < jsonArray.length(); ++index) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                dates.add(simpleDateFormat.parse(jsonObject.getString("date")));
            }
        } catch (InterruptedException|ExecutionException exp) {
            LogToFile.e("get VOA list from website, but thread interrupted or execute error");
        } catch (JSONException|ParseException exp) {
            LogToFile.e("get VOA list from website success, but occur error when parse, json string is :" + dateJson);
        }
        return dates;
    }

    // get list from sqlite
    public static final List<Date> getFromSqlite() {
        List<Date> dates = new ArrayList<>();
        VoaSQLiteOpenHelper voaSQLiteOpenHelper = new VoaSQLiteOpenHelper(GlobalContextApplication.getContext(), ConstantValue.DbSchema.DB_NAME, ConstantValue.DbSchema.LATEST_VERSION);
        // maybe there should use getReadableDatabase()
        SQLiteDatabase sqLiteDatabase = voaSQLiteOpenHelper.getWritableDatabase();
        try (Cursor cursor = sqLiteDatabase.query(ConstantValue.DbSchema.VoaDb.DATE_LIST_TABLE_NAME, null, null, null, null,null, null)) {
            cursor.moveToFirst();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            while (!cursor.isAfterLast()) {
                String dateString = cursor.getString(cursor.getColumnIndex(ConstantValue.DbSchema.VoaDb.DateList.DATE));
                Date date = simpleDateFormat.parse(dateString);
                dates.add(date);
                cursor.moveToNext();
            }
        } catch (ParseException exp) {
            LogToFile.e("Parse date from sql error!");
        }
        return dates;
    }
}
