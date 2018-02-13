package com.example.lenovo.trafficservices;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kidnaping.GPSTracker;


public class kidnap extends BaseActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private DatabaseReference mdatabase;
    FirebaseUser currentFirebaseUser;
    EditText txtphone;
    String mUserId;

    /*  double LATITUDE=39.01;
     double  LONGITUDE=32.0;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidnap);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);
        txtphone = (EditText) findViewById(R.id.editText);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mUserId = currentFirebaseUser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mUserId);
        mdatabase.child("privateNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String privateNumber = dataSnapshot.getValue(String.class);
                txtphone.setText(privateNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        GPSTracker tracker = new GPSTracker(this);
       /* if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {*/

        double LATITUDE = tracker.getLatitude();
        double LONGITUDE = tracker.getLongitude();

        TextView myAddress = (TextView) findViewById(R.id.myaddress);

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                myAddress.setText(strReturnedAddress.toString());
            } else {
                myAddress.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            myAddress.setText("Canont get Address!");
        }
    }




    public void busend(View view) throws IOException {

        SmsManager sendsms = SmsManager.getDefault();
        EditText messageEdt = (EditText)findViewById(R.id.messageEdt);
        String message = messageEdt.getText().toString();


        TextView myAddress = (TextView) findViewById(R.id.myaddress);

        //  EditText txtbody=(EditText)findViewById(R.id.editText2);
        // sendsms.sendTextMessage(txtphone.getText().toString(),null,txtbody.getText().toString(),null,null);
        sendsms.sendTextMessage(txtphone.getText().toString(),null , message+myAddress.getText().toString(),null,null);

    }

}









