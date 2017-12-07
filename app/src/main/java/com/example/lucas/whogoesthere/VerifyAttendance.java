package com.example.lucas.whogoesthere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerifyAttendance extends AppCompatActivity {

    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.lastName)
    EditText lastName;
    @BindView(R.id.role)
    EditText role;
    @BindView(R.id.go)
    Button go;
    String firstNameText;
    String lastNameText;
    String roleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_attendance);
        ButterKnife.bind(this);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameText = firstName.getText().toString();
                lastNameText = lastName.getText().toString();
                roleText = role.getText().toString();
                scanBarcode(view);
            }
        });
    }

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
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
                                try {
                                    String res = response.body().string();
                                    processResults(res);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String str1 = "http://adm-store.com/AttendanceDB/add_stat.php?username=anon&password=1234P@ssw0rd&" + result.getContents() + "&firstName=" + firstNameText + "&lastName=" + lastNameText + "&role=" + roleText;
                    Log.i("getUrl", str1);
                    doGetRequest(str1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void processResults(String res) {
        try {
            JSONObject obj = new JSONObject(res);
            int success = obj.getInt("success");
            String message = obj.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            if (success == 1) {
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
