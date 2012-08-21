package tatu.bowshield.util;

import android.util.Log;

public class DebugLog {
    private static String TAG = "tatu";

    public static void log(String message) {
        Log.v(TAG, message);
    }
}
