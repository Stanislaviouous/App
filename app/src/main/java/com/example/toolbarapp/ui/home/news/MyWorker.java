package com.example.toolbarapp.ui.home.news;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.toolbarapp.ui.home.HomeFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {

        String url = "https://matesapi.herokuapp.com/getNews?groups" + HomeFragment.newsUrl;
        try {
            run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Worker.Result.success();
    }
    void run(String url) throws IOException {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);

        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            HomeFragment.news = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}