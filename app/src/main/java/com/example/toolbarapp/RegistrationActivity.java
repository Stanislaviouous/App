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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    ImageView end3;
    TextView login;
    MaterialEditText password;
    MaterialEditText email;
    Button RegIn;
    MaterialEditText nameFull;
    MaterialEditText log;

    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference referenceRating;
    DatabaseReference referenceToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        end3 = (ImageView) findViewById(R.id.imageView3);
        login = (TextView) findViewById(R.id.logup);
        RegIn = (Button) findViewById(R.id.RegIn);

        password = (MaterialEditText) findViewById(R.id.passwordd);
        email = (MaterialEditText) findViewById(R.id.emaill);
        nameFull = (MaterialEditText) findViewById(R.id.nameFull);
        log = (MaterialEditText) findViewById(R.id.log);


        auth = FirebaseAuth.getInstance();

        end3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MatesActivity.class);
                startActivity(intent);
            }
        });


        RegIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = Objects.requireNonNull(password.getText()).toString();
                String em = Objects.requireNonNull(email.getText()).toString();
                String nFull = Objects.requireNonNull(nameFull.getText()).toString();
                String lg = Objects.requireNonNull(log.getText()).toString();

                if (TextUtils.isEmpty(em) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(nFull) || TextUtils.isEmpty(lg)){
                    Toast.makeText(RegistrationActivity.this, "Все поля пусты", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 8) { // Добавить условия
                    Toast.makeText(RegistrationActivity.this, "Пароль ненадёжен", Toast.LENGTH_SHORT).show();
                } else {
                    registered(pass, em, nFull, lg);
                }
            }
        });
    }

    private void registered(String pass, final String emi, final String nFull, final String lg) {
        auth.createUserWithEmailAndPassword(emi, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = null;
                    if (firebaseUser != null)
                        userId = firebaseUser.getUid();

                    assert userId != null;
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    referenceRating = FirebaseDatabase.getInstance().getReference("Rating").child(userId + " " + lg);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", nFull);
                    hashMap.put("email", emi);
                    hashMap.put("login", lg);
                    hashMap.put("imageUrl", "default");
                    hashMap.put("count", "0");

                    final HashMap<String, String> toDoList = new HashMap<>();
                    toDoList.put("1", "");

                    hashMap.put("TODO", "");
                    hashMap.put("PROGRESS","");
                    hashMap.put("COMPLETE", "");

                    FirebaseDatabase.getInstance().getReference("Task").child(userId).child("0").setValue("0");

                    referenceRating.setValue(0).isSuccessful();
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                referenceToDo = reference.child("TODO");
                                referenceToDo.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                                referenceToDo = reference.child("PROGRESS");
                                referenceToDo.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                                referenceToDo = reference.child("COMPLETE");
                                referenceToDo.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Проблемы с данными", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
