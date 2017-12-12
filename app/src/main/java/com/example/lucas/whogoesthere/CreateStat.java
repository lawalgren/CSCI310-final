package com.example.lucas.whogoesthere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateStat extends AppCompatActivity {

    @BindView(R.id.go_create_stat) Button go_create_stat;
    @BindView(R.id.firstnamecreate) TextView firstnamecreate;
    @BindView(R.id.lastnamecreate) TextView lastnamecreate;
    @BindView(R.id.rolecreate) TextView rolecreate;
    @BindView(R.id.datecreate) TextView datecreate;
    String username;
    String password;
    String groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        groupname = intent.getStringExtra("groupname");
        getSupportActionBar().setTitle("Create Stat in " + groupname);
        go_create_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstnamecreate.getText().toString();
                String lastName = lastnamecreate.getText().toString();
                String role = rolecreate.getText().toString();
                String date = datecreate.getText().toString();

                try {
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/add_stat.php").newBuilder();
                    urlBuilder.addQueryParameter("username", username);
                    urlBuilder.addQueryParameter("password", password);
                    urlBuilder.addQueryParameter("groupname", groupname);
                    urlBuilder.addQueryParameter("firstName", firstName);
                    urlBuilder.addQueryParameter("lastName", lastName);
                    urlBuilder.addQueryParameter("role", role);
                    urlBuilder.addQueryParameter("day", date);
                    String built = urlBuilder.build().toString();
                    doGetRequest(built);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    OkHttpClient client = new OkHttpClient();
    void doGetRequest(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String resp = null;
                                try {
                                    resp = response.body().string();
                                    processResults(resp);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }
    void processResults(String resp) {
        try {
            JSONObject obj = new JSONObject(resp);
            String message = obj.getString("message");
            int success = obj.getInt("success");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            if (success == 1) {
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
