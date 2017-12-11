package com.example.lucas.whogoesthere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StatsView extends AppCompatActivity {

    String username;
    String password;
    String groupname;
    @BindView(R.id.stats) ListView stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        groupname = intent.getStringExtra("groupname");
        getSupportActionBar().setTitle("Stats for " + groupname);
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/get_stats.php").newBuilder();
            urlBuilder.addQueryParameter("username", username);
            urlBuilder.addQueryParameter("password", password);
            urlBuilder.addQueryParameter("groupname", groupname);
            String built = urlBuilder.build().toString();
            doGetRequest(built);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statsview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addstat) {
            Intent i = new Intent(this, CreateStat.class);
            i.putExtra("username", username);
            i.putExtra("password", password);
            i.putExtra("groupname", groupname);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/get_stats.php").newBuilder();
            urlBuilder.addQueryParameter("username", username);
            urlBuilder.addQueryParameter("password", password);
            urlBuilder.addQueryParameter("groupname", groupname);
            String built = urlBuilder.build().toString();
            doGetRequest(built);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Log.i("JSON result", resp);
            final ArrayList<HashMap<String, String>> statArray = new ArrayList<HashMap<String, String>>();
            JSONObject jsonresult = new JSONObject(resp);
            JSONArray jsonStats = jsonresult.getJSONArray("stats");
            while(jsonStats.length() != 0) {
                HashMap<String, String> h1 = new HashMap<>();
                JSONObject obj = jsonStats.getJSONObject(jsonStats.length() - 1);
                String fN = obj.getString("firstName");
                String lN = obj.getString("lastName");
                String role = obj.getString("role");
                String day = obj.getString("day");
                h1.put("First Name", fN);
                h1.put("Last Name", lN);
                h1.put("Role", role);
                h1.put("Date", day);
                statArray.add(h1);
                jsonStats.remove(jsonStats.length() - 1);
            }
            Log.i("StatArray sample", statArray.get(0).get("First Name") + " " + statArray.get(0).get("Last Name") + " " + statArray.get(0).get("Role") + " " + statArray.get(0).get("Date"));
            stats.setAdapter(new SimpleAdapter(
                    this,
                    statArray,
                    R.layout.stat,
                    new String[] {"First Name", "Last Name", "Role", "Date"},
                    new int[] {R.id.firstnameview, R.id.lastnameview, R.id.roleview, R.id.dayview}
            ));
            stats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String firstName = statArray.get(position).get("First Name");
                    String lastName = statArray.get(position).get("Last Name");
                    String role = statArray.get(position).get("Role");
                    String date = statArray.get(position).get("Date");
                    Intent i = new Intent(StatsView.this, EditStat.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    i.putExtra("groupname", groupname);
                    i.putExtra("firstname", firstName);
                    i.putExtra("lastname", lastName);
                    i.putExtra("role", role);
                    i.putExtra("date", date);
                    startActivity(i);
                    return true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
