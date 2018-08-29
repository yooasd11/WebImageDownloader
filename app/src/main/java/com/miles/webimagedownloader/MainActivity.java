package com.miles.webimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;

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

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WebClient webClient;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        destination = getFilesDir().getAbsolutePath() + "/image";

        webClient = new WebClient();

        compositeDisposable.add(RxView.clicks(button)
                .observeOn(Schedulers.io())
                .flatMap($ -> Observable.just(downloadFile(destination)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        file -> {
                            Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
                            image.setImageDrawable(drawable);
                        },
                        throwable -> {
                            input.setText("요청에 실패했습니다.");
                            Log.e(MainActivity.class.getName(), throwable.getLocalizedMessage());
                        }
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
