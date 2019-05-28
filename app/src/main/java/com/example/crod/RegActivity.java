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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegActivity extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText Temail;
    EditText Tpass;
    public EditText TrepeatPass;
    private FirebaseAuth mAuth;
    SharedPreferences mName;
    SharedPreferences mSurnName;
    SharedPreferences mPass;
    SharedPreferences mEmail;
Button btn_to_login;
Button btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.reg_editText_name);
        surname = findViewById(R.id.reg_editText_surname);
        Temail = findViewById(R.id.reg_EditText_email);
        Tpass = findViewById(R.id.reg_EditText_pass);
        TrepeatPass = findViewById(R.id.reg_EditText_repeat_pass);
        btn_to_login = findViewById(R.id.reg_to_login);
        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        btn_reg = findViewById(R.id.reg_button);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() != 0 && surname.getText().length() != 0 && Temail.getText().length() != 0 && Tpass.getText().length() != 0 && TrepeatPass.getText().length() != 0) {
                    if (Tpass.getText().length() >= 6) {
                          //  if (Tpass.getText().toString() == TrepeatPass.getText().toString()) {
                        if (Tpass.getText().toString().equals(TrepeatPass.getText().toString())){
                                registr(Temail.getText().toString(),Tpass.getText().toString(),v);

                        }else
                            Snackbar.make(v,"Пароли не савподают",Snackbar.LENGTH_SHORT).show();
                        Log.e("Test","p1= "+ Tpass.getText().toString());
                           Log.e("Test","p2= "+TrepeatPass.getText().toString());

                    } else {
                        Snackbar.make(v, "Пароль должен быть не менее 6 символов ", Snackbar.LENGTH_SHORT).show();
                    }
                    } else{
                    Snackbar.make(v, "Заполните все поля", Snackbar.LENGTH_SHORT).show();}
            }
        });



    }
    public void registr (final String email , final String password, final View v){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    CreateUserDB(mAuth.getCurrentUser().getUid());
                    save();
                    Snackbar.make(v,"Пользователь зарегисрирован ", Snackbar.LENGTH_SHORT).show();


                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         startActivity(new Intent(getApplicationContext(), MainActivity.class));

                     }
                 }).start();

                }else {
                    Snackbar.make(v,"Данный пользователь уже зарегистрирован ",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void save() {
        mName =getSharedPreferences(getString(R.string.sp_name),MODE_PRIVATE);
        mSurnName =getSharedPreferences(getString(R.string.sp_surname),MODE_PRIVATE);
        mPass =getSharedPreferences(getString(R.string.sp_pass),MODE_PRIVATE);
        mEmail =getSharedPreferences(getString(R.string.sp_email),MODE_PRIVATE);

        mName.edit().putString(getString(R.string.sp_name),name.getText().toString()).commit();

        mSurnName.edit().putString(getString(R.string.sp_surname),surname.getText().toString()).commit();

        mPass.edit().putString(getString(R.string.sp_pass),Tpass.getText().toString()).commit();

        mEmail.edit().putString(getString(R.string.sp_email),Temail.getText().toString()).commit();
    }

    public void CreateUserDB(String Uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users");

        myRef.child(Uid).child(getString(R.string.db_email)).setValue(Temail.getText().toString());
        myRef.child(Uid).child(getString(R.string.db_name)).setValue(name.getText().toString());
        myRef.child(Uid).child(getString(R.string.db_surname)).setValue(surname.getText().toString());
        myRef.child(Uid).child(getString(R.string.db_tutor)).setValue(0);
        myRef.child(Uid).child(getString(R.string.tour)).setValue(0);
        myRef.child(Uid).child(getString(R.string.db_сomment)).setValue(0);
      /*  myRef.child(Uid).child(getString(R.string.db_name)).setValue(name);
        myRef.child(Uid).child(getString(R.string.db_surnName)).setValue(surname);
        myRef.child(Uid).child(getString(R.string.db_tutor)).setValue(tutor);*/
        //myRef.child(Uid).child(getString(R.string.db_idSmenа)).setValue(0);
    }


}
