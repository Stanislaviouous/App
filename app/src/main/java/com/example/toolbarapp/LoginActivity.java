package com.example.toolbarapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    ImageView end;
    TextView login;
    MaterialEditText password;
    MaterialEditText email;
    Button loginIn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        end = (ImageView) findViewById(R.id.imageView5);
        login = (TextView) findViewById(R.id.login);
        password = (MaterialEditText) findViewById(R.id.password);
        email = (MaterialEditText) findViewById(R.id.email);
        loginIn = (Button) findViewById(R.id.loginIn);

        auth = FirebaseAuth.getInstance();

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MatesActivity.class);
                startActivity(intent);
            }
        });
        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = Objects.requireNonNull(password.getText()).toString();
                String em = Objects.requireNonNull(email.getText()).toString();

                if (TextUtils.isEmpty(em) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(LoginActivity.this, "Поля пусты", Toast.LENGTH_SHORT).show();
                } else {
                auth.signInWithEmailAndPassword(em, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }
}
