package com.example.appattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    Button MarkAttendance,ChkRecords;
    FirebaseAuth firebaseAuth;
    TextView Name,Id,Class;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();
        Name = findViewById(R.id.tvName);
        Id = findViewById(R.id.tvId);
        Class = findViewById(R.id.tvClass);
        progressDialog = new ProgressDialog(this);

        MarkAttendance = findViewById(R.id.btnAttendance);
        MarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicesOk())
                {
                    init();
                }
            }
        });
        ChkRecords = findViewById(R.id.btnChkRecords);
        ChkRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Activity_Record.class));
            }
        });

        progressDialog.setMessage("Loading");
        progressDialog.show();
        setValue();

    }

    private void setValue() {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).child("Details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String id = dataSnapshot.child("UserId").getValue().toString();
                String uClass = dataSnapshot.child("Class").getValue().toString();

                Name.setText(name);
                Id.setText(id);
                Class.setText(uClass);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void init()
    {
        startActivity(new Intent(SecondActivity.this,MapsActivity.class));
    }

    public boolean isServicesOk()
    {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SecondActivity.this);
        if(available == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SecondActivity.this,available,9001);
            dialog.show();
        }
        else
            Toast.makeText(SecondActivity.this,"Play Services not available",Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signout: {
                firebaseAuth.signOut();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}


