package com.wekan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wekan.view.LogView;


/**
 * A login screen that offers login via email/password.
 */
public class AbstractActivity extends AppCompatActivity {
    protected LogView logView;
    public static final String MESSAGE_HANDLE = "HANDLE_MESSAGE";
    protected Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (null != logView) {
                String message = msg.getData().getString(MESSAGE_HANDLE);
                logView.showMessage(message);
            }
        }
    };

    public void showMessage(String tag, String message) {
        Message m = new Message();
        Bundle b = new Bundle();
        b.putString(MESSAGE_HANDLE, "[" + tag + "]:" + message);
        m.setData(b);
        mHandler.sendMessage(m);
        Log.e(tag, message);
    }


}

