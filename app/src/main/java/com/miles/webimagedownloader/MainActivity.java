package com.miles.webimagedownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;

import java.net.HttpURLConnection;
import java.util.Observable;

import butterknife.BindView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.input)
    EditText input;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.image)
    ImageView image;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WebClient webClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webClient = new WebClient();

        compositeDisposable.add(RxView.clicks(button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                $ -> {
                    webClient.setUrl(input.getText().toString());
                    webClient.downloadFromUrl();
                },
                throwable -> input.setText("요청에 실패했습니다.")
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
