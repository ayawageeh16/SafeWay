package com.example.lenovo.trafficservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.iid.FirebaseInstanceId;

import kidnaping.GPSTracker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
    private EditText editTextUserName;
    private Button buttonSignup;
    private TextView textViewSignin;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    String userName, email, password;
    int counter=1;
    SessionManagement session;


    private DatabaseReference firebaseReferenceToGetUsersCount;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManagement(getApplicationContext());

        // Get Users Count
        firebaseReferenceToGetUsersCount= FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        firebaseReferenceToGetUsersCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    counter++;
                }
                counter++;
                Log.d("MainActivity",counter+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void registerUser() {

        //getting email and password from edit texts
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();
        userName = editTextUserName.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(password.equals(passwordConfirm))) {
            Toast.makeText(this, "passwords not matching", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userName.matches("[A-Za-z0-9]+")) {
            Toast.makeText(this, "only alphabet or number allowed for user name", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();


        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here
                            Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                            GPSTracker tracker = new GPSTracker(MainActivity.this);
                            double LATITUDE = tracker.getLatitude();
                            double LONGITUDE = tracker.getLongitude();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                            String mUserId = currentFirebaseUser.getUid();
                            mDatabase.child("users").child(mUserId).child("Email").setValue(email);
                            mDatabase.child("users").child(mUserId).child("Password").push().setValue(password);
                            mDatabase.child("users").child(mUserId).child("userName").setValue(userName);
                            mDatabase.child("users").child(mUserId).child("uid").push().setValue(mUserId);
                            mDatabase.child("users").child(mUserId).child("phone").setValue("");
                            mDatabase.child("users").child(mUserId).child("privateNumber").setValue(LATITUDE);
                            mDatabase.child("users").child(mUserId).child("locationLat").setValue(LONGITUDE);
                            mDatabase.child("users").child(mUserId).child("locationLong").setValue("");
                            mDatabase.child("users").child(mUserId).child("image").setValue("https://firebasestorage.googleapis.com/v0/b/login-304ac.appspot.com/o/Testimage%2Fcapture.png?alt=media&token=6bc5db37-4e9e-4b23-b189-afd97babd1ab");
                            String token = FirebaseInstanceId.getInstance().getToken();
                            if(token != null){
                                mDatabase.child("users").child(mUserId).child("token").setValue(token);

                            }
                            session.createLoginSession(userName, email);

                            Intent n = new Intent(MainActivity.this, MapActivity.class);
                            startActivity(n);
                            finish();
                        } else {
                            //display some message here
                            Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        if(view == buttonSignup) {
            registerUser();
        }
        if(view == textViewSignin){
            Intent n = new Intent(this, LoginActivity.class);
            startActivity(n);
            finish();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
