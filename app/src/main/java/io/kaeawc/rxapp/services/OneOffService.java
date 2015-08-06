package io.kaeawc.rxapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class OneOffService extends IntentService {

    private static final String SERVICE_NAME = OneOffService.class.getSimpleName();
    // initialize SCHEDULED Executor
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static int SCHEDULED_CORE_SIZE;

    static {
        SCHEDULED_CORE_SIZE = CPU_COUNT;
        // not need more that 2 threads in SCHEDULED_POOL
        if (CPU_COUNT > 2) {
            SCHEDULED_CORE_SIZE = 2;
        }
    }

    private static ScheduledExecutorService mExecutorService = Executors.newScheduledThreadPool
            (SCHEDULED_CORE_SIZE);

    // The number of attempts made for this action
    public static final String ARG_ATTEMPT = "attempt";
    private static final int DEFAULT_ATTEMPT = 0;

    // Origin of the request
    public static final String ARG_ORIGIN = "origin";

    // Type of delay algorithm to use between attempts
    public static final String ARG_DELAY_TYPE = "delayType";
    public static final String DELAY_CONSTANT = "constant";
    public static final String DELAY_EXPONENTIAL = "exponential";

    // Amount to delay between attempts
    public static final String ARG_DELAY_VALUE = "delayValue";
    public static final int DELAY_INITIAL = 0;
    public static final int DELAY_DEFAULT = 2;

    public OneOffService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null) {
            Timber.w("Null intent");
            return;
        }

        String action = intent.getAction();
        if (action == null || action.trim().equals("")) {
            Timber.w("Null or blank action");
            return;
        }

        final int attempt = intent.getIntExtra(ARG_ATTEMPT, DEFAULT_ATTEMPT);
        final String origin = getOrigin(intent);
        final String delayType = getDelayType(intent);
        final int delayValue = getDelayValue(intent, delayType, attempt);

        Runnable runnable = getRunnable(action, origin);

        if (delayValue <= 0) {
            // should start action immediately
            mExecutorService.execute(runnable);
        } else {
            // creates and executes a one-shot action that becomes enabled after the given delay
            mExecutorService.schedule(runnable, delayValue, TimeUnit.SECONDS);
        }
    }

    @NonNull
    public Runnable getRunnable(final String action, final String origin) {

        switch (action) {

            // TODO: Implement other action cases

            default:
                return new Runnable() {
                    @Override
                    public void run() {
                        JsonApi.getIpAddress(origin);
                    }
                };
        }
    }

    @Nullable
    public String getOrigin(@NonNull Intent intent) {
        String tag = intent.getStringExtra(ARG_ORIGIN);

        // Make sure Hinge API events always have a request code
        if (tag == null || tag.trim().equals("")) {
            return null;
        }

        return tag;
    }

    @NonNull
    public String getDelayType(@NonNull Intent intent) {
        String type = intent.getStringExtra(ARG_DELAY_TYPE);

        if (type == null || type.trim().equals("")) {
            return DELAY_CONSTANT;
        }

        return type;
    }

    public int getDelayValue(@NonNull Intent intent, @NonNull String delayType, int attempt) {

        if (attempt == 0) {
            return DELAY_INITIAL;
        }

        int value = intent.getIntExtra(ARG_DELAY_VALUE, DELAY_DEFAULT);

        switch (delayType) {
            case DELAY_CONSTANT:
                break;
            case DELAY_EXPONENTIAL:

                // Exponential requires a base of at least 2.
                if (value <= 1) {
                    value = 2;
                }

                return (int) Math.pow(value, attempt);
            default:
                break;
        }

        return value;
    }
}
