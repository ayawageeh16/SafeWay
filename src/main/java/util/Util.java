package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Util {


    public static void initToast(Context c, String message){
        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();
    }

    public  static boolean verifyInternetConnection(Context context) {
        boolean connectionStatus;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectionStatus = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return connectionStatus;
    }


}
