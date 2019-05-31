package com.github.susan.debounceclick.java;

import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


public class DebounceClickHandler {

    /**
     * Frozen window in millions, apps may override it.
     */
    public static long FROZEN_WINDOW_MILLIS = 700L;

    private static final String TAG = DebounceClickHandler.class.getSimpleName();

    private static final Map<View, FrozenView> viewWeakHashMap = new WeakHashMap<>();

//    public static boolean shouldDoClick(View targetView, Object object) {
//        Log.d("Debounce", "targetView->" + targetView.getId());
//        Log.d("Debounce", "viewWeakHashMap->" + viewWeakHashMap.size());
//        FrozenView frozenView = viewWeakHashMap.get(targetView);
//        final long now = now();
//
//        if (frozenView == null) {
//            Log.d("Debounce", "frozenView == null");
//            frozenView = new FrozenView(targetView);
//            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
//            frozenView.setHost(object);
//            viewWeakHashMap.put(targetView, frozenView);
//            return true;
//        }
//
//        Log.d("Debounce", "frozenView ->" + frozenView.getFrozenWindowTime());
//        if (now >= frozenView.getFrozenWindowTime() && object == frozenView.getHost()) {
//            Log.d("Debounce", "frozenView -> now >= frozenView.getFrozenWindowTime()");
//            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
//            return true;
//        }
//
//        return false;
//    }

    public static boolean shouldDoClick(View targetView) {
        int stackLayer = getStackLayer(targetView.getId());
        Log.d("Debounce", "stackLayer->" +stackLayer);
        FrozenView frozenView = viewWeakHashMap.get(targetView);
        final long now = now();

        if (frozenView == null) {
            Log.d("Debounce", "frozenView == null");
            frozenView = new FrozenView(targetView);
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            frozenView.setStackTraceLayer(stackLayer);
            viewWeakHashMap.put(targetView, frozenView);
            return true;
        }
        Log.d("Debounce", "FrozenWindowTime ->" + frozenView.getFrozenWindowTime());
        Log.d("Debounce", "FrozenWindowTime now->" + now);
        if (now >= frozenView.getFrozenWindowTime() || frozenView.getStackTraceLayer() != stackLayer) {
            Log.d("Debounce", "frozenView -> now >= frozenView.getFrozenWindowTime()");
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            return true;
        }

        return false;
    }


    private static int getStackLayer(int s) {
        StackTraceElement[] stackTraceElements = Looper.getMainLooper().getThread().getStackTrace();
        Log.e("stackTrace->", s + " == " + stackTraceToString(stackTraceElements));
        List<StackTraceElement> list = Arrays.asList(stackTraceElements);
        int androidClickIndex = 0;
        int debounceIndex = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getClassName().equals("android.view.View") && list.get(i).getMethodName().equals("performClick")) {
                androidClickIndex = i;
            }
            if (list.get(i).getClassName().equals("com.github.susan.debounceclick.java.DebounceClickHandler") && list.get(i).getMethodName().equals("shouldDoClick")) {
                debounceIndex = i;
            }
        }
        return debounceIndex-androidClickIndex;

    }

    //05-30 20:32:45.345 10716-10716/com.cm.android.doubleclick E/stackTrace->: 2131492965 == [com.github.susan.demo.DebounceClickHandler:shouldDoClick(51)][com.github.susan.demo.MainActivity:onClick(51)][android.view.View:performClick(5198)][android.view.View$PerformClick:run(21147)][android.os.Handler:handleCallback(739)][android.os.Handler:dispatchMessage(95)][android.os.Looper:loop(148)][android.app.ActivityThread:main(5417)][java.lang.reflect.Method:invoke(-2)][com.android.internal.os.ZygoteInit$MethodAndArgsCaller:run(726)][com.android.internal.os.ZygoteInit:main(616)]
    public static String stackTraceToString(final StackTraceElement[] stackTrace) {
        if ((stackTrace == null) || (stackTrace.length < 4)) {
            return "";
        }

        StringBuilder t = new StringBuilder();

        for (int i = 3; i < stackTrace.length; i++) {
            t.append('[');
            t.append(stackTrace[i].getClassName());
            t.append(':');
            t.append(stackTrace[i].getMethodName());
            t.append("(" + stackTrace[i].getLineNumber() + ")]");
        }
        return t.toString();
    }

    private static long now() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    private static class FrozenView extends WeakReference<View> {
        private long FrozenWindowTime;
        private Object host;
        private int stackTraceLayer;

        public Object getHost() {
            return host;
        }

        public void setHost(Object host) {
            this.host = host;
        }

        FrozenView(View referent) {
            super(referent);
        }

        long getFrozenWindowTime() {
            return FrozenWindowTime;
        }

        void setFrozenWindow(long expirationTime) {
            this.FrozenWindowTime = expirationTime;
        }

        public void setStackTraceLayer(int layer) {
            stackTraceLayer = layer;
        }

        public int getStackTraceLayer() {
            return stackTraceLayer;
        }
    }
}
