package edu.byuh.cis.cs203.ender1.util;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/24/17.
 */

public class Timer extends Handler {

    private List<TickListener> subscribers;
    private static Timer singleton = null;

    private Timer() {
        subscribers = new ArrayList<>();
        sendEmptyMessageDelayed(0, 50);
    }

    public static Timer getInstance() {
        if (singleton == null) {
            singleton = new Timer();
        }
        return singleton;
    }

    public void subscribe(TickListener t) {
        subscribers.add(t);
    }

    public void unsubscribe(TickListener t) {
        subscribers.remove(t);
    }

    @Override
    public void handleMessage(Message m) {
        for (TickListener tl : subscribers) {
            tl.tick();
        }
        sendEmptyMessageDelayed(0, 50);
    }

}
