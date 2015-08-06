package io.kaeawc.rxapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.kaeawc.rxapp.R;
import io.kaeawc.rxapp.events.RxBus;
import io.kaeawc.rxapp.events.Tap;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView mHelloText;

    public MainActivityFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        mHelloText = (TextView) view.findViewById(R.id.hello_text);

        RxBus.getDefault().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                Timber.v("onNext");
                long time = System.currentTimeMillis();
                mHelloText.setText(String.valueOf(time));
            }
        });

        return view;
    }

    @OnClick(R.id.hello_button)
    public void onHelloTapped() {
        RxBus.getDefault().post(new Tap());
    }

    @OnLongClick(R.id.hello_button)
    public boolean onHelloPressed() {
        RxBus.getDefault().post(new Tap());
        return true;
    }
}
