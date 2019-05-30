package com.github.susan.debounceclick.java;

import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class DebounceClickHandler {

    /**
     * Frozen window in millions, apps may override it.
     */
    public static long FROZEN_WINDOW_MILLIS = 300L;

    private static final String TAG = DebounceClickHandler.class.getSimpleName();

    private static final Map<View, FrozenView> viewWeakHashMap = new WeakHashMap<>();

    public static boolean shouldDoClick(View targetView, Object object) {
        Log.d("Debounce", "targetView->" + targetView.getId());
        Log.d("Debounce", "viewWeakHashMap->" + viewWeakHashMap.size());
        FrozenView frozenView = viewWeakHashMap.get(targetView);
        final long now = now();

        if (frozenView == null) {
            Log.d("Debounce", "frozenView == null");
            frozenView = new FrozenView(targetView);
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            frozenView.setHost(object);
            viewWeakHashMap.put(targetView, frozenView);
            return true;
        }

        Log.d("Debounce", "frozenView ->" + frozenView.getFrozenWindowTime());
        if (now >= frozenView.getFrozenWindowTime() && object == frozenView.getHost()) {
            Log.d("Debounce", "frozenView -> now >= frozenView.getFrozenWindowTime()");
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            return true;
        }

        return false;
    }

    public static boolean shouldDoClick(View targetView) {
        Log.d("Debounce", "targetView->" + targetView.getId());
        Log.d("Debounce", "viewWeakHashMap->" + viewWeakHashMap.size());
        FrozenView frozenView = viewWeakHashMap.get(targetView);
        final long now = now();

        if (frozenView == null) {
            Log.d("Debounce", "frozenView == null");
            frozenView = new FrozenView(targetView);
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            viewWeakHashMap.put(targetView, frozenView);
            return true;
        }

        Log.d("Debounce", "frozenView ->" + frozenView.getFrozenWindowTime());
        if (now >= frozenView.getFrozenWindowTime()) {
            Log.d("Debounce", "frozenView -> now >= frozenView.getFrozenWindowTime()");
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            return true;
        }

        return false;
    }


    private static long now() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    private static class FrozenView extends WeakReference<View> {
        private long FrozenWindowTime;
        private Object host;

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
    }
}
