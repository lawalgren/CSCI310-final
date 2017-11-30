package com.example.lucas.whogoesthere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyAttendance extends AppCompatActivity {

    @BindView(R.id.firstName) EditText firstName;
    @BindView(R.id.lastName) EditText lastName;
    @BindView(R.id.role) EditText role;
    @BindView(R.id.go) Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_attendance);
        ButterKnife.bind(this);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameText = firstName.toString();
                String lastNameText = lastName.toString();
                String roleText = role.toString();

            }
        });
    }
}
