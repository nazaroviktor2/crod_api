package com.example.crod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {
Button btn_reg;
Button btn_login;
EditText email;
EditText pass;
    SharedPreferences pPref;
    SharedPreferences ePref;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_EditText_email);
        pass=findViewById(R.id.login_EditText_pass);
        btn_reg = findViewById(R.id.reg_button);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegActivity.class));

            }
        });
        btn_login = findViewById(R.id.login_button);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singing(email.getText().toString(),pass.getText().toString(),v);
            }
        });

    }

    public void singing(String email , String pass, final View v){//запрос к базе
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar.make(v,"Вход выполнин", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
                }
                else {
                    Snackbar.make(v,"Не верный логин или пароль ", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadText();
       FirebaseUser currentUser = mAuth.getCurrentUser();
       if (currentUser!=null){
           startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
        }
    }
    void loadText() {
        pPref = getSharedPreferences(getString(R.string.sp_pass),MODE_PRIVATE);
        ePref = getSharedPreferences(getString(R.string.sp_email),MODE_PRIVATE);

        pass.setText(pPref.getString(getString(R.string.sp_pass), ""));
        email.setText(ePref.getString(getString(R.string.sp_email), ""));


    }

    @Override
    public void onBackPressed() {

            finish();

        super.onBackPressed();
    }
}
