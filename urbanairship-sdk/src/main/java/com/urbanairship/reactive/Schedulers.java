/* Copyright 2017 Urban Airship and Contributors */

package com.urbanairship.reactive;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

/**
 * Scheduler implementations
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class Schedulers {
    /**
     * Creates a Scheduler that targets the provided looper at scheduler time.
     *
     * @param looper The looper to schedule on.
     * @return A Scheduler.
     */
    public static LooperScheduler looper(Looper looper) {
        return new LooperScheduler(looper);
    }

    /**
     * Scheduler that targets a specific RunLoop at schedule time.
     */
    public static class LooperScheduler implements Scheduler {

        private Looper looper;

        /**
         * Run loop Scheduler constructor.
         * @param looper The looper to scheduler on.
         */
        public LooperScheduler(@NonNull Looper looper) {
            if (looper == null) {
                throw new IllegalArgumentException("Looper cannot be null");
            }
            this.looper = looper;
        }

        public Subscription schedule(final Runnable runnable) {
            final Subscription subscription = Subscription.empty();

            new Handler(looper).post(new Runnable() {
                @Override
                public void run() {
                    if (!subscription.isCancelled()) {
                        runnable.run();
                    }
                }
            });

            return subscription;
        }

        public Subscription schedule(final Runnable runnable, long delayTimeMs) {
            final Subscription subscription = Subscription.empty();

            new Handler(looper).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!subscription.isCancelled()) {
                        runnable.run();
                    }
                }
            }, delayTimeMs);

            return subscription;
        }
    }
}
