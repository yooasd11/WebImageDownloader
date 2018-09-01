package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.input)
    EditText input;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CachedBitmapCrawler cachedBitmapCrawler;
    private Bitmap failBitmap;
    private String destination;
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            cachedBitmapCrawler = new CachedBitmapCrawler(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        failBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);
        destination = getFilesDir().getAbsolutePath() + "/image";

        compositeDisposable.add(RxView.clicks(button)
                .doOnNext($ -> progressBar.setVisibility(View.VISIBLE))
                .observeOn(Schedulers.io())
                .flatMap($ -> Observable.fromCallable(() -> cachedBitmapCrawler.getBitmapWithUrl(input.getText().toString(), destination))
                        .onErrorReturn(throwable -> failBitmap))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                            image.setImageBitmap(bitmap);
                            progressBar.setVisibility(View.GONE);
                        },
                        throwable -> Log.e(TAG, throwable.getLocalizedMessage())
                        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
