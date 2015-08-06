package io.kaeawc.rxapp.events;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import timber.log.Timber;

public class RxBus {

    private static final Map<String, Subject<Object, Object>> sBus = new HashMap<>();
    private static final String DEFAULT = "default";
    private String mName;

    private static RxBus sInstance;

    private RxBus(@NonNull String name) {
        mName = name;

        if (!sBus.containsKey(mName)) {
            Timber.v("Created new RxBus: %s", mName);
            sBus.put(mName, new SerializedSubject<>(PublishSubject.create()));
        }
    }

    public static RxBus getDefault() {
        if (sInstance == null) {
            sInstance = new RxBus(DEFAULT);
        }
        return sInstance;
    }

    public void post(@NonNull Object o) {
        Timber.v("Posted event %s", o.getClass().getSimpleName());
        sBus.get(mName).onNext(o);
    }

    @NonNull
    public Observable<Object> observe() {
        return sBus.get(mName);
    }

    @NonNull
    public Subscription subscribe(@NonNull Action1<Object> onNext) {
        return observe().subscribe(onNext);
    }

    @NonNull
    public Subscription subscribe(
            @NonNull Action1<Object> onNext,
            @NonNull Action1<Throwable> onError) {

        return observe().subscribe(onNext, onError);
    }

    @NonNull
    public Subscription subscribe(
            @NonNull Action1<Object> onNext,
            @NonNull Action1<Throwable> onError,
            @NonNull Action0 onCompleted) {

        return observe().subscribe(onNext, onError, onCompleted);
    }
}
