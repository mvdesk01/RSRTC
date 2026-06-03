package com.mtech.rsrtcsc.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLogger {

    private static final String TAG = "FileLogger";
    private static final String LOG_FILE_NAME = "Rsrtc_Logs.txt";

    public static void log(Context context, String tag, String message) {
        Log.d(tag, message); // Normal Logcat logging
        writeToFile(context, tag + " : " + message);
    }

    public static void error(Context context, String tag, String message) {
        Log.e(tag, message);
        writeToFile(context, "ERROR : " + tag + " : " + message);
    }

    private static void writeToFile(Context context, String text) {
        try {
                File logDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "logs");

            Log.e("LOGGER_DEBUG", "Base dir = " + logDir);
            if (!logDir.exists()) logDir.mkdirs();

            File logFile = new File(logDir, LOG_FILE_NAME);
            FileWriter writer = new FileWriter(logFile, true);

            String timeStamp = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
            ).format(new Date());

            writer.append(timeStamp)
                    .append("  ")
                    .append(text)
                    .append("\n");

            writer.flush();
            writer.close();
            Log.e("LOGGER_DEBUG", " writer. close() called successfully and finished the process");

        } catch (IOException e) {
            Log.e(TAG, "File write failed", e);
        }
    }
}

