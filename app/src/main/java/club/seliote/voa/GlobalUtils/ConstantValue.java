package club.seliote.voa.GlobalUtils;

public class ConstantValue {

    public static final String VOA_LIST_ACTIVITY_USER_SELECTED_DATE = "club.seliote.voa.voalistactivityutils.recyclerviewadapter";

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final String LOG_FILE_NAME = "voa.log";

    public static final boolean NETWORK_CLOSE = false;

    public static final boolean NETWORK_CONNECT =  true;

    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    public static final String GET_VOA_LIST_WEBSITE = "https://seliote.club/VOA/dates.php";

    public static final String GET_ARTICLE_BASE_URL = "https://seliote.club/VOA/source/";

    public static final String GET_MEDIA_BASE_URL = "https://seliote.club/VOA/source/";

    public static final String SQL_CREATE_LIST_TABLE_VERSION_1 = "CREATE TABLE DateList (" +
            "id integer primary key autoincrement, " +
            "date text)";

    public static class DbSchema {
        public static final int LATEST_VERSION = 1;
        public static final String DB_NAME = "VOA.db";
        public static class VoaDb {
            public static final String DATE_LIST_TABLE_NAME = "DateList";
            public static class DateList {
                public static final String ID = "id";
                public static final String DATE = "date";
            }
        }
    }
}
