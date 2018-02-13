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

public class DisplayAdsTravellerFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private RecyclerView mBlogList;
    Query recentData;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.recycler_view, container, false);
        mBlogList = (RecyclerView)view.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("traveller");
        recentData = myRef;

        FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassCarOwner, BlogViewHolder>(
                        ModelClassCarOwner.class,
                        R.layout.design_row_traveller,
                        BlogViewHolder.class,
                        recentData)  {



                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClassCarOwner model, int position) {
                        viewHolder.setUserName(model.getUserName());
                        viewHolder.setImage(getContext(),model.getImage());
                        viewHolder.phoneNumber = model.getPhoneNumber();
                        viewHolder.setTime(model.getTime());
                        viewHolder.setFrom(model.getFrom());
                        viewHolder.setTo(model.getTo());
                        viewHolder.setDate(model.getDate());
                        viewHolder.userUid = model.getUserUid();
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);




        return view;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        String userName, image, phoneNumber, time, userUid, from, to, date;
        View mView;

        public BlogViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            ImageView callImgView, chatImgView;
            callImgView = (ImageView)mView.findViewById(R.id.callImgView);
            chatImgView = (ImageView)mView.findViewById(R.id.chatImgView);

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
