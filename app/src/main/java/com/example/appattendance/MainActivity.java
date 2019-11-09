package com.example.appattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email,Password;
    TextView register;
    Button login;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.edId);
        Password = findViewById(R.id.edPassword);
        register = findViewById(R.id.tvRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

    }

    public void login_user(View view) {
        String user_email = email.getText().toString();
        String user_pass = Password.getText().toString();
        if(user_email.isEmpty() || user_pass.isEmpty())
        {
            Toast.makeText(MainActivity.this,"All fields are required",Toast.LENGTH_LONG).show();
        }
        else{
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        finish();
                        Toast.makeText(MainActivity.this,"Log in Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,SecondActivity.class));
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Log in Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
