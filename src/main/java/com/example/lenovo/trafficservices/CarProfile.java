package com.example.lenovo.trafficservices;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CarProfile extends BaseActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri filepath = null;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    Button btnSaveCar;
    AutoCompleteTextView txtCarNumber, txtCarSeats, txtCarPrice;
    ImageView loadImageView, imageViewPicure;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference firebaseReferenceToGetUsersCount;
    private StorageReference mStorage;
    int counter=0;
    String carNumber, carSeats, carPrice, from, to, date, time, phonenumber, userImage;
    Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_profile);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        from = extras.getString("EXTRA_FROM");
        to = extras.getString("EXTRA_TO");
        date = extras.getString("EXTRA_DATE");
        time = extras.getString("EXTRA_TIME");
        phonenumber = extras.getString("EXTRA_PHONENUMBER");

        mStorage = FirebaseStorage.getInstance().getReference();

        session = new SessionManagement(this);
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.KEY_NAME);
        email = user.get(SessionManagement.KEY_EMAIL);

        // Get Users Count
        firebaseReferenceToGetUsersCount= FirebaseDatabase.getInstance().getReference().getRoot().child("car owner");
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtCarNumber = (AutoCompleteTextView)findViewById(R.id.txtCarNumber);
        txtCarSeats = (AutoCompleteTextView)findViewById(R.id.txtCarSeats);
        txtCarPrice = (AutoCompleteTextView)findViewById(R.id.txtCarPrice);
        loadImageView = (ImageView) findViewById(R.id.loadImageView);
        imageViewPicure = (ImageView)findViewById(R.id.imageViewPicure);
        btnSaveCar = (Button)findViewById(R.id.btnSaveCar);
        imageViewPicure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });
        loadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });
        btnSaveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carNumber = txtCarNumber.getText().toString();
                carPrice = txtCarPrice.getText().toString();
                carSeats = txtCarSeats.getText().toString();
                if (carNumber.trim().equals(""))
                    txtCarNumber.setError("Please enter car number");
                else if (carSeats.trim().equals(""))
                    txtCarSeats.setError("Please enter number of seats");
                else if (carPrice.trim().equals(""))
                    txtCarPrice.setError("Please enter the price");
                else{
                    try {
                        Snackbar snackbar = Snackbar.make(view, "success", Snackbar.LENGTH_LONG);
                        snackbar.show();

                        StorageReference fileref = mStorage.child("Testimage").child(filepath.getLastPathSegment());
                        fileref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUri = taskSnapshot.getDownloadUrl();
                                final String carImage = downloadUri.toString();

                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String mUserId = currentFirebaseUser.getUid();

                                mDatabase.child("users").child(mUserId).child("image").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        userImage = dataSnapshot.getValue(String.class);
                                        mDatabase.child("car owner").child("owner" + counter).child("image").setValue(userImage);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                mDatabase.child("car owner").child("owner" + counter).child("email").setValue(email);
                                mDatabase.child("car owner").child("owner" + counter).child("userName").setValue(name);
                                mDatabase.child("car owner").child("owner" + counter).child("from").setValue(from);
                                mDatabase.child("car owner").child("owner" + counter).child("to").setValue(to);
                                mDatabase.child("car owner").child("owner" + counter).child("date").setValue(date);
                                mDatabase.child("car owner").child("owner" + counter).child("time").setValue(time);
                                mDatabase.child("car owner").child("owner" + counter).child("phoneNumber").setValue(phonenumber);
                                mDatabase.child("car owner").child("owner" + counter).child("from_to_date").setValue(from + "_" + to + "_" + date);
                                mDatabase.child("car owner").child("owner" + counter).child("image").setValue(userImage);
                                mDatabase.child("car owner").child("owner" + counter).child("carNumber").setValue(carNumber);
                                mDatabase.child("car owner").child("owner" + counter).child("carSeats").setValue(carSeats);
                                mDatabase.child("car owner").child("owner" + counter).child("carPrice").setValue(carPrice);
                                mDatabase.child("car owner").child("owner" + counter).child("userUid").setValue(mUserId);
                                mDatabase.child("car owner").child("owner" + counter).child("carImage").setValue(carImage);

                                Intent i = new Intent(CarProfile.this, RecyclerViewTravellers.class);
                                Bundle extras = new Bundle();
                                extras.putString("EXTRA_FROM",from);
                                extras.putString("EXTRA_TO",to);
                                extras.putString("EXTRA_DATE",date);
                                i.putExtras(extras);
                                startActivity(i);

                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Load car image please..", Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar.make(view, "Failure", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }

            }
        });


        }

    private void loadImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // load image from gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            filepath= data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(filepath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // ImageView imageView = (ImageView) findViewById(R.id.imgview);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 200, 100, false);

            // set circle bitmap
            ImageView mImage = (ImageView) findViewById(R.id.loadImageView);
            mImage.setImageBitmap(resizedBitmap);


        }
    }
}
