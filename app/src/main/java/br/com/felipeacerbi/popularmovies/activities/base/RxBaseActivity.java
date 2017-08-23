package br.com.felipeacerbi.popularmovies.activities.base;


import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

public abstract class RxBaseActivity extends AppCompatActivity {

    private CompositeDisposable subscriptions;

    @Override
    protected void onResume() {
        super.onResume();
        subscriptions = new CompositeDisposable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!subscriptions.isDisposed()) {
            subscriptions.dispose();
        }
        subscriptions.clear();
    }
}
