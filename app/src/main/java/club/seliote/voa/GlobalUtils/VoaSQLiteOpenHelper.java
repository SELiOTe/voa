package club.seliote.voa.GlobalUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VoaSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 该方法没有使用SQLiteDatabase.CursorFactory
     */
    public VoaSQLiteOpenHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantValue.SQL_CREATE_LIST_TABLE_VERSION_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing yet
    }
}
