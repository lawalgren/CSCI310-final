package com.example.lucas.whogoesthere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CreateGroup extends AppCompatActivity {
    @BindView(R.id.groupname) TextView gname;
    @BindView(R.id.go_create_group) Button go;
    String username;
    String password;
    String groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setTitle("Create Group");
        ButterKnife.bind(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupname = gname.getText().toString();
                try {
                    doGetRequest("http://adm-store.com/AttendanceDB/add_group.php",username, password, groupname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    OkHttpClient client = new OkHttpClient();
    void doGetRequest(String url, String username, String password, String groupname) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("username", username);
        urlBuilder.addQueryParameter("password", password);
        urlBuilder.addQueryParameter("groupname", groupname);
        String built = urlBuilder.build().toString();
        Log.i("built", built);

        Request request = new Request.Builder()
                .url(built)
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
    void processResults(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            int success = obj.getInt("success");
            String message = obj.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            if(success == 1) {
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
