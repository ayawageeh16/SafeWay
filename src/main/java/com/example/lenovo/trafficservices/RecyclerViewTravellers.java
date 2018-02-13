package com.example.lenovo.trafficservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import android.view.ViewGroup.LayoutParams;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewTravellers extends AppCompatActivity {

    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recentData;
    String from, to, date;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.r1);

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
        myRef = database.getReference("traveller");
        recentData = myRef.orderByChild("from_to_date").equalTo(from+"_"+to+"_"+date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClass,BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClass,BlogViewHolder>(
                        ModelClass.class,
                        R.layout.design_row_recycleview,
                        BlogViewHolder.class,
                        recentData)  {


                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClass model, int position) {
                        viewHolder.setUserName(model.getUserName());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.userUid = model.getUserUid();
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    //View Holder For Recycler View
    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
        String userUid;
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Chat.class);
                    Bundle extras= new Bundle();
                    extras.putString("EXTRA_USERUID",userUid);
                    intent.putExtras(extras);
                    v.getContext().startActivity(intent);
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
