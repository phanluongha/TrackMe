package com.plh.trackme.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.plh.trackme.Util.CommonUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Duration extends TextView {

    int initialDelay = 1000; //first update in miliseconds
    int period = 1000;      //nexts updates in miliseconds

    private long time = 0;
    private Timer timer;
    private TimerTask task;

    public Duration(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void startCount() {
        stopCount();
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                time += period;
                Message msg = new Message();
                mHandler.sendMessage(msg);
            }
        };
        timer.scheduleAtFixedRate(task, initialDelay, period);
    }

    public void stopCount() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setText(CommonUtil.getDurationText(time));
        }
    };

}
