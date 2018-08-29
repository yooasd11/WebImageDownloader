package com.miles.webimagedownloader;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.Objects;

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

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WebClient webClient;
    private String destination;
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        destination = getFilesDir().getAbsolutePath() + "/image";

        webClient = new WebClient();

        compositeDisposable.add(RxView.clicks(button)
                .observeOn(Schedulers.io())
                .flatMap($ -> Observable.fromCallable(() -> downloadFile(destination))
                        .onErrorReturn(throwable -> new File("")))
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(file -> {
                    if (file == null || !file.exists()) {
                        Toast.makeText(this, "요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        return Observable.just(new ColorDrawable(Color.TRANSPARENT));
                    } else {
                        return Observable.just(Objects.requireNonNull(BitmapDrawable.createFromPath(file.getAbsolutePath())));
                    }
                })
                .subscribe(drawable -> {
                            image.setImageDrawable(drawable);
                            progressBar.setVisibility(View.GONE);
                        },
                        throwable -> Log.e(TAG, throwable.getLocalizedMessage())
                        ));
    }

    private File downloadFile(String destination) throws Exception {
        webClient.setUrl(input.getText().toString());
        return webClient.downloadFromUrl(destination);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
