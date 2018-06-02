package com.example.rrty6.vcardapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import static com.example.rrty6.vcardapp.utils.UIHandler.WhatValue.*;

public class UIHandler extends Handler {
    private Context context;

    public UIHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Toast toast;
        switch (msg.what) {
            case methodStart:
                toast = Toast.makeText(context, "MEthod have been started", Toast.LENGTH_LONG);
                toast.show();
                break;
            case methodEnd:
                toast = Toast.makeText(context, "MEthod have been ended", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }

    public interface WhatValue {
        int methodStart = 1;
        int methodEnd = 2;
    }
}
