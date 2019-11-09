package com.example.appattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "RegisterActivity";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    Button register;
    EditText email,name,password,id,contact;

    ProgressDialog progressDialog;

    String mail, uname, userid, pass,mobile,userClass;
    String[] Class = {"FYCS","SYCS","TYCS","FYIT","SYIT","TYIT"};


    private static final String Tag = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        contact = findViewById(R.id.edContact);
        register = findViewById(R.id.btn_Register);
        email = findViewById(R.id.edRemail);
        name = findViewById(R.id.edRuname);
        password = findViewById(R.id.edRpassword);
        id = findViewById(R.id.edRid);

        progressDialog = new ProgressDialog(this);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter classArray = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,Class);
        classArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(classArray);


    }

    //Spinner's Data
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userClass = Class[position];
    }
    //Spinner's Data
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Register ButtonOnClick
    public void register_user(View view) {
        if(validate())
        {
            String u_email = email.getText().toString().trim();
            String u_pass = password.getText().toString().trim();
            progressDialog.setMessage("Registering user ! Please Wait");
            progressDialog.show();

            new ImportData(uname,mail,userid,mobile,userClass);

            firebaseAuth.createUserWithEmailAndPassword(u_email,u_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Log.d(Tag,"User Registered Successfully");
                        setData();
                        Log.d(Tag,"User inserted in database");
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Registration successful",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                    }
                    else
                    {
                        Log.d(Tag,"Registration failed !");
                        progressDialog.dismiss();
                        clearViews();
                        Toast.makeText(RegisterActivity.this,"Registration unsuccessful",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Checks if all fields are filled
    private boolean validate() {
        boolean result = false;
        mail = email.getText().toString();
        uname = name.getText().toString();
        pass = password.getText().toString();
        mobile = contact.getText().toString();
        userid = id.getText().toString();

        if(mail.isEmpty() || uname.isEmpty() || pass.isEmpty() || userid.isEmpty() || mobile.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Please enter all fields",Toast.LENGTH_LONG).show();
        }
        if(mobile.length() < 10)
        {
            Toast.makeText(RegisterActivity.this,"Enter Valid Mobile Number",Toast.LENGTH_LONG).show();
        }
        else {
            result = true;
        }
        return  result;
    }

    //Clears the Data after registration
    private void clearViews(){
        email.setText("");
        name.setText("");
        password.setText("");
        id.setText("");
        contact.setText("");
    }

    //Updates Database
    private void setData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        UserData userData = new UserData(uname,mail,userClass,mobile,userid);
        ref.child("Details").setValue(userData);
    }
}