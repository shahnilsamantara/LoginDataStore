package com.shahnil.logindata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private String mEmail;
    private String mPassword;
    private EditText editTextemail;
    private EditText editTextpassword;
    private FirebaseAuth firebaseAuth ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        editTextemail = findViewById(R.id.editTextemail);
        editTextpassword = findViewById(R.id.editTextpassword);







    }

    private void registeruser() {

        mEmail = editTextemail.getText().toString().trim();
        mPassword= editTextpassword.getText().toString().trim();

        if(TextUtils.isEmpty(mEmail)){
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mPassword)){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "complete ", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button1:
                registeruser();

                break;

            case R.id.button2:
                Intent intent = new Intent(MainActivity.this, options_page.class);
                startActivity(intent);
                break;


        }
    }

}
