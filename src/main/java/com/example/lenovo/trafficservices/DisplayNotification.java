package com.example.lenovo.trafficservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DisplayNotification extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseUser currentFirebaseUser;
    Query ref;
    String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Read from the database
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mUserId = currentFirebaseUser.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(mUserId).child("notifications");
        ref = myRef.orderByChild("date");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ModelClassNotification, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassNotification, BlogViewHolder>(
                        ModelClassNotification.class,
                        R.layout.notification_recycler_view_item,
                        BlogViewHolder.class,
                        ref){

                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClassNotification model, int position) {
                        viewHolder.setNotification(model.getNotification());
                        viewHolder.setDate(model.getDate());
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder  {

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
        }
        public void setNotification(String notification){
            TextView post_notification = (TextView)mView.findViewById(R.id.list_item_text);
            post_notification.setText(notification);
        }
        public void setDate(String date){
            TextView post_date = (TextView)mView.findViewById(R.id.dateText);
            post_date.setText(date);
        }
    }

}
