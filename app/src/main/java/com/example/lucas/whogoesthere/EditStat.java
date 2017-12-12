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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditStat extends AppCompatActivity {
    @BindView(R.id.firstnameedit) TextView firstnameedit;
    @BindView(R.id.lastnameedit) TextView lastnameedit;
    @BindView(R.id.roleedit) TextView roleedit;
    @BindView(R.id.dateedit) TextView dateedit;
    @BindView(R.id.editupdate) Button editupdate;
    @BindView(R.id.editcancel) Button editcancel;
    String username;
    String password;
    String groupname;
    String firstName;
    String lastName;
    String role;
    String date;
    String newFirstname;
    String newLastname;
    String newrole;
    String newdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        groupname = intent.getStringExtra("groupname");
        firstName = intent.getStringExtra("firstname");
        lastName = intent.getStringExtra("lastname");
        role = intent.getStringExtra("role");
        date = intent.getStringExtra("date");
        date = date.substring(0,4) + date.substring(5,7) + date.substring(8);
        firstnameedit.setText(firstName);
        lastnameedit.setText(lastName);
        roleedit.setText(role);
        dateedit.setText(date);
        getSupportActionBar().setTitle("Edit Stat for " + groupname);
        editupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFirstname = firstnameedit.getText().toString();
                newLastname = lastnameedit.getText().toString();
                newrole = roleedit.getText().toString();
                newdate = dateedit.getText().toString();

                try {
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/update_stat.php").newBuilder();
                    urlBuilder.addQueryParameter("username", username);
                    urlBuilder.addQueryParameter("password", password);
                    urlBuilder.addQueryParameter("groupname", groupname);
                    urlBuilder.addQueryParameter("firstName", firstName);
                    urlBuilder.addQueryParameter("lastName", lastName);
                    urlBuilder.addQueryParameter("role", role);
                    urlBuilder.addQueryParameter("day", date);
                    urlBuilder.addQueryParameter("newfirstName", newFirstname);
                    urlBuilder.addQueryParameter("newlastName", newLastname);
                    urlBuilder.addQueryParameter("newrole", newrole);
                    urlBuilder.addQueryParameter("newday", newdate);
                    String built = urlBuilder.build().toString();
                    doGetRequest(built);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        editcancel.setOnClickListener(new View.OnClickListener() {
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
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/remove_stat.php").newBuilder();
                urlBuilder.addQueryParameter("username", username);
                urlBuilder.addQueryParameter("password", password);
                urlBuilder.addQueryParameter("groupname", groupname);
                urlBuilder.addQueryParameter("firstName", firstName);
                urlBuilder.addQueryParameter("lastName", lastName);
                urlBuilder.addQueryParameter("day", date);
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
