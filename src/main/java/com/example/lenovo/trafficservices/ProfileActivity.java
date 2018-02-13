package com.example.lenovo.trafficservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private CircleImageView imageviewround;
    private final short RESULT_LOAD_IMAGE = 1;
    private EditText username;
    private EditText mphone;
    private EditText mphonePrivate;
    private TextView mailTxtview;
    private Button msubmit;
    private Uri filepath = null;
    private StorageReference mStorage;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;
    FirebaseUser currentFirebaseUser;
    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mUserId = currentFirebaseUser.getUid();


        mStorage = FirebaseStorage.getInstance().getReference();
        mdatabase = FirebaseDatabase.getInstance().getReference();

        imageviewround = (CircleImageView) findViewById(R.id.imgview);
        username = (EditText) findViewById(R.id.editUserName);
        mphonePrivate = (EditText) findViewById(R.id.editPhonePrivate);
        mphone=(EditText)findViewById(R.id.editphone);
        mailTxtview = (TextView)findViewById(R.id.mailTxtview);

        mdatabase.child("users").child(mUserId).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.getValue(String.class);
                Picasso.with(getApplicationContext()).load(image).into(imageviewround);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.child("users").child(mUserId).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phone = dataSnapshot.getValue(String.class);
                mphone.setText(phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.child("users").child(mUserId).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                username.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.child("users").child(mUserId).child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Email = dataSnapshot.getValue(String.class);
                mailTxtview.setText(Email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.child("users").child(mUserId).child("privateNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String privateNumber = dataSnapshot.getValue(String.class);
                mphonePrivate.setText(privateNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        msubmit = (Button) findViewById(R.id.subment);
        mprogress = new ProgressDialog(this);
        imageviewround.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View arg0) {

                                                  Intent i = new Intent(
                                                          Intent.ACTION_PICK,
                                                          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                  startActivityForResult(i, RESULT_LOAD_IMAGE);
                                              }

                                          }
        );


        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startpostimagr();
            }
        });
    }

    private void startpostimagr() {
        try {
            if (isNetworkAvailable() == true) {
                mprogress.setMessage("waiting...........");
                mprogress.show();
                final String usrName = username.getText().toString().trim();
                final String phonePrivate = mphonePrivate.getText().toString().trim();
                final String phone = mphone.getText().toString().trim();
                if (!TextUtils.isEmpty(usrName) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(phonePrivate)) {
                    StorageReference fileref = mStorage.child("Testimage").child(filepath.getLastPathSegment());
                    fileref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            final String image = downloadUri.toString();
                            mdatabase.child("users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().child("userName").setValue(usrName);
                                    dataSnapshot.getRef().child("privateNumber").setValue(phonePrivate);
                                    dataSnapshot.getRef().child("phone").setValue(phone);
                                    dataSnapshot.getRef().child("image").setValue(image);
                                    mprogress.dismiss();
                                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("User", databaseError.getMessage());
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //if the upload is not successfull
                                    //hiding the progress dialog
                                    mprogress.dismiss();

                                    //and displaying error message
                                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    //displaying percentage in progress dialog
                                    mprogress.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                } else {
                    mprogress.dismiss();
                    Toast.makeText(getApplicationContext(), "your data isn't complete", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("Error", "No network");
                Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            mprogress.dismiss();
            Toast.makeText(this, "Please upload image", Toast.LENGTH_LONG).show();
        }
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

            // set circle bitmap
            ImageView mImage = (ImageView) findViewById(R.id.imgview);
            mImage.setImageBitmap(bm);


        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}