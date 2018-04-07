package club.seliote.voa.GlobalUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    public static final boolean get() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GlobalContextApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isAvailable());
    }

}
