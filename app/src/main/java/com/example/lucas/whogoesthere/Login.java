package com.example.lucas.whogoesthere;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class Login extends AppCompatActivity {
    public final boolean REGISTER = false;
    public final boolean LOGIN = true;
    @BindView(R.id.togglereglogin)
    ToggleButton togglereglogin;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.login_go)
    Button login_go;
    @BindView(R.id.Description)
    TextView Description;
    boolean register = LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        togglereglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!togglereglogin.isChecked()) {
                    register = LOGIN;
                    Description.setText("Login");
                } else {
                    register = REGISTER;
                    Description.setText("Register");
                }
            }
        });
        login_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                if (register == REGISTER)
                    registerUser(usernameText, passwordText);
                else
                    loginUser(usernameText, passwordText);
            }
        });
    }

    OkHttpClient client = new OkHttpClient();

    void doGetRequest(String url, String username, String password) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("username", username);
        urlBuilder.addQueryParameter("password", password);
        String built = urlBuilder.build().toString();

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
                                String res = null;
                                try {
                                    res = response.body().string();
                                    processResults(res);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    public void registerUser(String username, String password) {
        try {
            doGetRequest("http://adm-store.com/AttendanceDB/add_user.php", username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginUser(String username, String password) {
        try {
            doGetRequest("http://adm-store.com/AttendanceDB/login.php", username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processResults(String response) {
        try {
            JSONObject res = new JSONObject(response);
            String message = res.getString("message");
            int success = res.getInt("success");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            if (success == 1) {
                startActivity(new Intent(this, GroupsView.class) );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
