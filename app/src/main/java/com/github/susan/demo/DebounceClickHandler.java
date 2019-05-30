package com.github.susan.demo;

import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


public class DebounceClickHandler {

    /**
     * Frozen window in millions, apps may override it.
     */
    public static long FROZEN_WINDOW_MILLIS = 300L;

    private static final String TAG = DebounceClickHandler.class.getSimpleName();

    private static final Map<View, FrozenView> viewWeakHashMap = new WeakHashMap<>();

    public static boolean shouldDoClick(View targetView, Object object) {
        Log.d("Debounce-1", "targetView->" + targetView.getId());
        Log.d("Debounce-1", "viewWeakHashMap->" + viewWeakHashMap.size());
        FrozenView frozenView = viewWeakHashMap.get(targetView);
        final long now = now();

        if (frozenView == null) {
            Log.d("Debounce-1", "frozenView == null");
            frozenView = new FrozenView(targetView);
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            frozenView.setHostHashCode(object.hashCode());
            viewWeakHashMap.put(targetView, frozenView);
            return true;
        }

        Log.d("Debounce-1", "frozenView ->" + frozenView.getFrozenWindowTime());
        if (now >= frozenView.getFrozenWindowTime() && (new Object().hashCode()) == frozenView.getHostHashCode()) {
            Log.d("Debounce-1", "frozenView -> now >= frozenView.getFrozenWindowTime()");
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
        private int hostHashCode;

        public int getHostHashCode() {
            return hostHashCode;
        }

        public void setHostHashCode(int hostHashCode) {
            this.hostHashCode = hostHashCode;
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
