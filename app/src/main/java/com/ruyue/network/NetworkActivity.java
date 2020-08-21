package com.ruyue.network;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkActivity extends AppCompatActivity {
    private static final String URL = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";
    private static final int DEFAULT_VALUE = 0;
    private static final String KEY = "count";

    @BindView(R.id.get_data)
    Button getDataBtn;
    @BindView(R.id.open_count)
    Button openCountBtn;
    List<Person> dataList;
    String firstName;
    PersonDao personDao;

    @OnClick({R.id.get_data,R.id.open_count})
    public void onClick(Button btn) {
        switch(btn.getId()) {
            case R.id.get_data:
                getData();
                break;
            case R.id.open_count:
                SharedPreferences sharedPref = NetworkActivity.this.getPreferences(Context.MODE_PRIVATE);
                int open_count = sharedPref.getInt(KEY, DEFAULT_VALUE);
                Toast.makeText(getApplicationContext(), Integer.toString(open_count), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ButterKnife.bind(this);

        MyApplication myApplication = (MyApplication)getApplication();
        personDao = myApplication.getLocalDataSource().personDao();

        changeOpenValue();
    }

    private void getData() {
        final Request request = new Request.Builder().url(URL).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                firstName = getFirstNameFromRoom();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), firstName, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = Objects.requireNonNull(response.body()).string();
                gsonAnalyzeJSONArray(result);
                insertDataInRoom(dataList);
                runOnUiThread(() -> {
                    if(dataList.size() > 0) {
                        Toast.makeText(getApplicationContext(), dataList.get(0).getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void changeOpenValue() {
        SharedPreferences sharedPref = NetworkActivity.this.getPreferences(Context.MODE_PRIVATE);
        int count = sharedPref.getInt(KEY, DEFAULT_VALUE);
        sharedPref.edit().putInt(KEY, ++count).apply();
    }

    public void gsonAnalyzeJSONArray( String result) {
        Gson gson = new Gson();
        Wrapper wrapper = gson.fromJson(result, Wrapper.class);
        dataList = wrapper.getData();
    }

    public String getFirstNameFromRoom() {
        return personDao.getPerson().get(0).getName();
    }

    public void insertDataInRoom(List<Person> list) {
        for (Person person : list) {
            personDao.insertPerson(person);
        }
    }
}

