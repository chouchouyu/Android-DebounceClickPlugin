package com.cm.android.doubleclick.java;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


public class DebouncedPredictor {

    /**
     * Frozen window in millions, apps may override it.
     */
    public static long FROZEN_WINDOW_MILLIS = 300L;

    private static final String TAG = DebouncedPredictor.class.getSimpleName();

    private static final Map<View, FrozenView> viewWeakHashMap = new WeakHashMap<>();

    public static void maybe(View targetView) {
        System.out.println("doubleclick -------------maybe  ");
    }

    public static boolean shouldDoClick(View targetView) {
        FrozenView frozenView = viewWeakHashMap.get(targetView);
        final long now = now();

        if (frozenView == null) {
            frozenView = new FrozenView(targetView);
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            viewWeakHashMap.put(targetView, frozenView);
            System.out.println("doubleclick -------------pass  ");
            return true;
        }

        if (now >= frozenView.getFrozenWindowTime()) {
            frozenView.setFrozenWindow(now + FROZEN_WINDOW_MILLIS);
            System.out.println("doubleclick -------------pass  " + now);
            return true;
        }

        System.out.println("doubleclick -------------fobbident  ");
        return false;
    }


    private static long now() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    private static class FrozenView extends WeakReference<View> {
        private long FrozenWindowTime;

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
