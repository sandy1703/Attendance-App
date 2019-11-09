package com.example.appattendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public FusedLocationProviderClient fusedLocationProviderClient;

    private boolean Loc_permission = false;
    private String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference;
    public FirebaseAuth firebaseAuth;

    private static final String TAG = "MapsActivity";

    Button check;
    List<LatLng> isPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseAuth = FirebaseAuth.getInstance();

        check = findViewById(R.id.btncheck);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked Check Inside");
                checkInside();
            }
        });

        getLocationPermission();

        isPoints.add(new LatLng(19.458585, 72.804908));
        isPoints.add(new LatLng(19.458401, 72.804647));
        isPoints.add(new LatLng(19.458342, 72.804691));
        isPoints.add(new LatLng(19.458299, 72.804631));
        isPoints.add(new LatLng(19.458163, 72.804732));
        isPoints.add(new LatLng(19.458102, 72.804650));
        isPoints.add(new LatLng(19.457748, 72.804930));
        isPoints.add(new LatLng(19.457809, 72.805024));
        isPoints.add(new LatLng(19.457768, 72.805061));
        isPoints.add(new LatLng(19.457791, 72.805102));
        isPoints.add(new LatLng(19.457765, 72.805127));
        isPoints.add(new LatLng(19.457886, 72.805306));
        isPoints.add(new LatLng(19.457777, 72.805400));
        isPoints.add(new LatLng(19.457841, 72.805485));
        isPoints.add(new LatLng(19.458585, 72.804908));


    }

    //This checks if location access permissions are granted
    public void getLocationPermission()
    {
        String[] permissions = {Fine_Location,Coarse_Location};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Fine_Location) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Coarse_Location) == PackageManager.PERMISSION_GRANTED)
            {
                Loc_permission = true;
                initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this,permissions,1234);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions,1234);
        }
    }

    //Handles if permissions are not granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Loc_permission = false;
        switch (requestCode)
        {
            case 1234:
            {
                if(grantResults.length > 0)
                {
                    for(int i=0;i < grantResults.length;i++)
                    {
                        if(PackageManager.PERMISSION_GRANTED != grantResults[i])
                        {
                            Loc_permission = false;
                            return;
                        }
                        Loc_permission = true;
                        initMap();
                    }
                }
            }
        }
    }

    public void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG,"Map initialized successfully");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        getDeviceLocation();

        final PolylineOptions points = new PolylineOptions()
                .add(new LatLng(19.458585, 72.804908))
                .add(new LatLng(19.458401, 72.804647))
                .add(new LatLng(19.458342, 72.804691))
                .add(new LatLng(19.458299, 72.804631))
                .add(new LatLng(19.458163, 72.804732))
                .add(new LatLng(19.458102, 72.804650))
                .add(new LatLng(19.457748, 72.804930))
                .add(new LatLng(19.457809, 72.805024))
                .add(new LatLng(19.457768, 72.805061))
                .add(new LatLng(19.457791, 72.805102))
                .add(new LatLng(19.457765, 72.805127))
                .add(new LatLng(19.457886, 72.805306))
                .add(new LatLng(19.457777, 72.805400))
                .add(new LatLng(19.457841, 72.805485))
                .add(new LatLng(19.458585, 72.804908));
        mMap.addPolyline(points);

    }

    public void getDeviceLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(Loc_permission){
                Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG,"Getting Device Location");
                            Location current_location = task.getResult();
                            moveCamera(new LatLng(current_location.getLatitude(),current_location.getLongitude()),15f);
                        }
                    }
                });
            }
        }
        catch (SecurityException e) {

        }
    }

    public void moveCamera(LatLng latLng,float zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    public void checkInside() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    boolean b = PolyUtil.containsLocation(location.getLatitude(),location.getLongitude(),isPoints,false);
                    if(b == true) {
                        Log.d(TAG,"Inside College");
                        Marking();
                        Log.d(TAG,"Database Updated");
                        Toast.makeText(MapsActivity.this,"Attendance Marked",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MapsActivity.this,"Outside College",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Marking(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        LocalDate localDate = LocalDate.now();
        Month month = localDate.getMonth();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        String date = dateTimeFormatter.format(localDate);
        reference.child("Attendance Record").child(month.toString()).child(localDate.toString()).setValue(date + " : "+ localDate.getDayOfWeek());
    }

}
