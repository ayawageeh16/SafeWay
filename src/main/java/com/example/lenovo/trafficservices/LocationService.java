package com.example.lenovo.trafficservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("MissingPermission")
public class LocationService extends Service {

    private LocationListener locationListener ;
    private LocationManager locationManager ;
    private DatabaseReference mdatabase;
    FirebaseUser currentFirebaseUser;
    String mUserId;
    double latitude, longitude;
    public LocationService() {

    }


    @Override
    public void onCreate() {

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mUserId = currentFirebaseUser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mdatabase.child("users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("locationLat").setValue(latitude);
                        dataSnapshot.getRef().child("locationLong").setValue(longitude);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        } ;

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission   // we did these permissions in mainacrivity
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER ,3000 , 0 ,locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        return START_STICKY ;
    }
    //for kitkat version


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //recall the service if it stopped
        Intent restartServiceIntent = new Intent(getApplicationContext() ,this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
