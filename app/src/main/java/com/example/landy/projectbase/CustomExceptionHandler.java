package com.example.landy.projectbase;

import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by landy on 16/9/10.
 */
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CustomExceptionHandler";
    private Thread.UncaughtExceptionHandler defaultUEH;
    private String localPath, feedbackUrl = null;
    private Context context;

    public CustomExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stackTrace = result.toString();
        printWriter.close();

        Log.e(TAG, "uncaughtException: " + stackTrace);


//        if (localPath != null) {
//            writeToFile(stackTrace, System.currentTimeMillis() + ".stacktrace");
//        }
//        if (feedbackUrl != null) {
//            writeToNet(stackTrace);
//        }

        defaultUEH.uncaughtException(t, e);
    }

//    private void writeToFile(String stacktrace, String filename) {
//        try {
//            FileOutputStream fos = new FileOutputStream(localPath + "/" + filename);
//            fos.write(stacktrace.getBytes());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void writeToNet(String stacktrace) {
//        try {
//            new HttpRequest(feedbackUrl + URLEncoder.encode(stacktrace)).getResponse();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
