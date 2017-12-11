package com.example.lucas.whogoesthere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditGroup extends AppCompatActivity {

    @BindView(R.id.cancel_button) Button cancel_button;
    @BindView(R.id.save_button) Button save_button;
    @BindView(R.id.groupnamefield) TextView groupnamefield;

    String username;
    String password;
    String groupname;
    String newgroupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        getSupportActionBar().setTitle("Edit Group");
        ButterKnife.bind(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        groupname = intent.getStringExtra("groupname");
        groupnamefield.setText(groupname);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newgroupname = groupnamefield.getText().toString();
                try {
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/update_group.php").newBuilder();
                    urlBuilder.addQueryParameter("username", username);
                    urlBuilder.addQueryParameter("password", password);
                    urlBuilder.addQueryParameter("groupname", groupname);
                    urlBuilder.addQueryParameter("newgroupname", newgroupname);
                    String built = urlBuilder.build().toString();
                    doGetRequest(built);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/remove_group.php").newBuilder();
                urlBuilder.addQueryParameter("username", username);
                urlBuilder.addQueryParameter("password", password);
                urlBuilder.addQueryParameter("groupname", groupname);
                String built = urlBuilder.build().toString();
                doGetRequest(built);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
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
