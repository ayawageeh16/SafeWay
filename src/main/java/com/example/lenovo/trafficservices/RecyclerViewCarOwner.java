package com.example.lenovo.trafficservices;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecyclerViewCarOwner extends AppCompatActivity {

    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recentData;
    String from, to, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        from = extras.getString("EXTRA_FROM");
        to = extras.getString("EXTRA_TO");
        date = extras.getString("EXTRA_DATE");

        //Recycler View
        mBlogList = (RecyclerView)findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        // Send a Query to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("car owner");
        recentData = myRef.orderByChild("from_to_date").equalTo(from+"_"+to+"_"+date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder>(
                        ModelClassCarOwner.class,
                        R.layout.design_row_recycleview,
                        BlogViewHolder.class,
                        recentData)  {



                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClassCarOwner model,int position) {
                        viewHolder.setUserName(model.getUserName());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.carNumber = model.getCarNumber();
                        viewHolder.carPrice = model.getCarPrice();
                        viewHolder.carSeats = model.getCarSeats();
                        viewHolder.phoneNumber = model.getPhoneNumber();
                        viewHolder.time = model.getTime();
                        viewHolder.userUid = model.getUserUid();
                        viewHolder.carImage = model.getCarImage();
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

//View Holder For Recycler View
public static class BlogViewHolder extends RecyclerView.ViewHolder  {
    String carNumber, carPrice, carSeats, phoneNumber, time, userUid, carImage;
    View mView;
    public BlogViewHolder(final View itemView) {
        super(itemView);
        mView= itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final Context mContext = v.getContext();
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.carowner_popup_window,null);
                final PopupWindow mPopupWindow = new PopupWindow(
                        customView,
                        600,
                        700
                );
                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                TextView CarNumberTextView, CarSeatsTextView, PriceTextView, PhoneNumberTextView,
                        TimeTextView;
                ImageButton callImageButton, chatImageButton;
                ImageView carImageView;

                CarNumberTextView = (TextView) customView.findViewById(R.id.CarNumberTextView);
                CarNumberTextView.setText("Car Number : "+carNumber);

                CarSeatsTextView = (TextView) customView.findViewById(R.id.CarSeatsTextView);
                CarSeatsTextView.setText("Number of Seats : "+carSeats);
                PriceTextView = (TextView) customView.findViewById(R.id.PriceTextView);
                PriceTextView.setText("Price : "+carPrice+" LE");
                PhoneNumberTextView = (TextView) customView.findViewById(R.id.PhoneNumberTextView);
                PhoneNumberTextView.setText("Phone Number : "+phoneNumber);
                TimeTextView = (TextView) customView.findViewById(R.id.TimeTextView);
                TimeTextView.setText("Time to travel : "+time);

                callImageButton = (ImageButton) customView.findViewById(R.id.callImageButton);
                chatImageButton = (ImageButton) customView.findViewById(R.id.chatImageButton);

                carImageView = (ImageView) customView.findViewById(R.id.carImageView);
                Picasso.with(mContext).load(carImage).resize(200, 100).into(carImageView);

                callImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+phoneNumber));
                        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        v.getContext().startActivity(callIntent);
                    }
                });

                chatImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(v.getContext(),  Chat.class);
                        Bundle extras= new Bundle();
                        extras.putString("EXTRA_USERUID",userUid);
                        intent.putExtras(extras);
                        v.getContext().startActivity(intent);
                    }
                });


                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(v, Gravity.CENTER,0,0);

            }
        });
        /******** For More Android Tutorials .. Download "Master Android" Application From Play Store Free********/
    }
    public void setUserName(String userName){
        TextView post_userName = (TextView)mView.findViewById(R.id.titleText);
        post_userName.setText(userName);
    }
    public void setImage(Context ctx , String image){
        CircleImageView post_image = (CircleImageView) mView.findViewById(R.id.UserImageView);
        // We Need TO pass Context
        Picasso.with(ctx).load(image).into(post_image);
    }
}
}
