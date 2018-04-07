package club.seliote.voa.GlobalUtils;

import android.app.Application;
import android.content.Context;

public class GlobalContextApplication extends Application {

    private static Context mContext;

    // this is onCreate()!!! Not constructor!!!
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
