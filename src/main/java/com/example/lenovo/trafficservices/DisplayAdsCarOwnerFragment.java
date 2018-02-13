package com.example.lenovo.trafficservices;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class DisplayAdsCarOwnerFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private RecyclerView mBlogList;
    Query recentData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.recycler_view, container, false);
        mBlogList = (RecyclerView)view.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("car owner");
        recentData = myRef;

        FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder>(
                        ModelClassCarOwner.class,
                        R.layout.design_row_car_owner,
                        BlogViewHolder.class,
                        recentData)  {



                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClassCarOwner model, int position) {
                        viewHolder.setUserName(model.getUserName());
                        viewHolder.setImage(getContext(),model.getImage());
                        viewHolder.carNumber = model.getCarNumber();
                        viewHolder.carPrice = model.getCarPrice();
                        viewHolder.carSeats = model.getCarSeats();
                        viewHolder.phoneNumber = model.getPhoneNumber();
                        viewHolder.setTime(model.getTime());
                        viewHolder.userUid = model.getUserUid();
                        viewHolder.carImage = model.getCarImage();
                        viewHolder.setFrom(model.getFrom());
                        viewHolder.setTo(model.getTo());
                        viewHolder.setDate(model.getDate());
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);




        return view;
    }

    //View Holder For Recycler View
    public static class BlogViewHolder extends RecyclerView.ViewHolder  {

        String userName,image, carNumber, carPrice, carSeats, phoneNumber, time, userUid, carImage, from, to, date;
        View mView;
        public BlogViewHolder(final View itemView) {
            super(itemView);
            mView= itemView;
            ImageView callImgView, chatImgView, infoImgView;
            callImgView = (ImageView)mView.findViewById(R.id.callImgView);
            chatImgView = (ImageView)mView.findViewById(R.id.chatImgView);
            infoImgView = (ImageView)mView.findViewById(R.id.infoImgView);

            callImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));
                    if (ActivityCompat.checkSelfPermission(itemView.getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    itemView.getContext().startActivity(callIntent);
                }
            });
            chatImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Chat.class);
                    Bundle extras= new Bundle();
                    extras.putString("EXTRA_USERUID",userUid);
                    intent.putExtras(extras);
                    itemView.getContext().startActivity(intent);
                }
            });

            infoImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Context mContext = view.getContext();
                    // Initialize a new instance of LayoutInflater service
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.car_asd_popup,null);
                    final PopupWindow mPopupWindow = new PopupWindow(
                            customView,
                            600,
                            600
                    );
                    if(Build.VERSION.SDK_INT>=21){
                        mPopupWindow.setElevation(5.0f);
                    }

                    // Get a reference for the custom view close button
                    ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                    TextView CarNumberTextView, CarSeatsTextView, PriceTextView;
                    ImageView carImageView;

                    CarNumberTextView = (TextView) customView.findViewById(R.id.CarNumberTextView);
                    CarNumberTextView.setText("Car Number : "+carNumber);

                    CarSeatsTextView = (TextView) customView.findViewById(R.id.CarSeatsTextView);
                    CarSeatsTextView.setText("Number of Seats : "+carSeats);
                    PriceTextView = (TextView) customView.findViewById(R.id.PriceTextView);
                    PriceTextView.setText("Price : "+carPrice+" LE");

                    carImageView = (ImageView) customView.findViewById(R.id.carImageView);
                    Picasso.with(mContext).load(carImage).resize(200, 100).into(carImageView);

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                }
            });
        }

        public void setUserName(String userName){
            TextView post_userName = (TextView)mView.findViewById(R.id.userNameText);
            post_userName.setText(userName);
        }
        public void setImage(Context ctx, String image){
            CircleImageView userImage = (CircleImageView)mView.findViewById(R.id.UserImageView);
            Picasso.with(ctx).load(image).into(userImage);
        }
        public void setFrom(String from){
            TextView source = (TextView)mView.findViewById(R.id.sourceTxt);
            source.setText(from);
        }
        public void setTo(String to){
            TextView destination = (TextView)mView.findViewById(R.id.destinationTxt);
            destination.setText(to);
        }
        public void setDate(String date){
            TextView datee = (TextView)mView.findViewById(R.id.dateTxt);
            datee.setText(date);
        }
        public void setTime(String time){
            TextView timee = (TextView)mView.findViewById(R.id.timeTxt);
            timee.setText(time);
        }
    }
}
