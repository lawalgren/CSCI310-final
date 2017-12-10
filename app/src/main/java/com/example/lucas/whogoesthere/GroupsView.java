package com.example.lucas.whogoesthere;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupsView extends AppCompatActivity {
    @BindView(R.id.groups) ListView groups;
    @BindView(R.id.groupsToolbar) Toolbar groupsToolbar;
    String username;
    String password;
    class Group {
        public int id;
        public String name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
        ButterKnife.bind(this);
        setSupportActionBar(groupsToolbar);
        getSupportActionBar().setTitle("Groups");

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        try {
            doGetRequest("http://adm-store.com/AttendanceDB/get_groups.php", username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupsview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addgroup) {
            Intent i = new Intent(this, CreateGroup.class);
            i.putExtra("username", username);
            i.putExtra("password", password);
            startActivity(i);
        }
        return true;
    }


        OkHttpClient client = new OkHttpClient();
    void doGetRequest(String url, String username, String password) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("username", username);
        urlBuilder.addQueryParameter("password", password);
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

    void processResults(String res) {
        try {
            ArrayList<String> group = new ArrayList<>();
            Log.i("Result", res);
            JSONObject obj = new JSONObject(res);
            JSONArray arr = obj.getJSONArray("groups");
            if (arr.length() == 0) {
                Intent i = new Intent(this, CreateGroup.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
            }
            while(arr.length() != 0) {
                JSONObject o = arr.getJSONObject(0);
                group.add(o.getString("groupname"));
                arr.remove(0);
            }
            groups.setAdapter(new ArrayAdapter<String>(this, R.layout.group, group));
            groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String groupnameText = adapterView.getItemAtPosition(position).toString();

                    Log.i("groupname", groupnameText);
                    Intent i = new Intent(GroupsView.this, StatsView.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    i.putExtra("groupname", groupnameText);
                    startActivity(i);
                }
            });
            groups.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String groupnameText = adapterView.getItemAtPosition(position).toString();

                    Intent i = new Intent(GroupsView.this, EditGroup.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    i.putExtra("groupname", groupnameText);
                    startActivity(i);

                    return false;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}