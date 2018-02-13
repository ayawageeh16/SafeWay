package com.example.lenovo.trafficservices;

import android.content.res.TypedArray;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.SmileRating;

public class feedback extends BaseActivity {

    String rating, feedback;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText feedbackTxt = (EditText)findViewById(R.id.feedbackEdt);
        final SmileRating rate = (SmileRating)findViewById(R.id.smileyrating);
        Button sendBtn = (Button)findViewById(R.id.send);
        feedback = feedbackTxt.getText().toString();

       rate.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener(){

            @Override
            public void onSmileySelected(int smiley) {
                switch (smiley){
                    case SmileRating.BAD:
                        rating = "BAD";
                        return;
                    case SmileRating.GOOD:
                        rating = "Good";
                        return;
                    case SmileRating.GREAT:
                        rating = "Great";
                        return;
                    case SmileRating.OKAY:
                        rating = "Okay";
                        return;
                    case SmileRating.TERRIBLE:
                        rating = "Terrible";
                        return;

                }
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = feedbackTxt.getText().toString();
                if(!feedback.isEmpty())
                    Toast.makeText(getApplicationContext(), "Thanks for your feedback :))", Toast.LENGTH_LONG).show();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String mUserId = currentFirebaseUser.getUid();
                mDatabase.child("feedback").child(mUserId).child("feedback").setValue(feedback);
                mDatabase.child("feedback").child(mUserId).child("rate").setValue(rating);
            }
        });
    }
}
