package club.seliote.voa.VoaListActivityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListDataLab {

    private static List<Date> mDates;

    // 空方法，单纯的阻止外部创建
    private ListDataLab() {
    }

    public static List<Date> getDates(boolean refresh) {
        if (refresh) {
            GetListDate.sync();
        }
        mDates = GetListDate.getFromSqlite();
        return mDates;
    }
}
