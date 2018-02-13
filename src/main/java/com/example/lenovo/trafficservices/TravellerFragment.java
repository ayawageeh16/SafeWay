package com.example.lenovo.trafficservices;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.support.design.widget.CoordinatorLayout;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;


public class TravellerFragment extends Fragment {

    Button btnSearch, btnClear;
    AutoCompleteTextView txtOrigin, txtDestination,txtDate, txtTime, txtPhoneNumber;
    String from, to, date, time, phoneNumber;
    private CoordinatorLayout coordinatorLayout;
    private DatabaseReference firebaseReferenceToGetUsersCount;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    SessionManagement session;
    int counter=0;
    String name, email, userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.traveller, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);

        session = new SessionManagement(getContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.KEY_NAME);
        email = user.get(SessionManagement.KEY_EMAIL);

        // Get Users Count
        firebaseReferenceToGetUsersCount= FirebaseDatabase.getInstance().getReference().getRoot().child("traveller");
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

        txtOrigin = (AutoCompleteTextView) view.findViewById(R.id.txtorigin);
        txtDestination = (AutoCompleteTextView) view.findViewById(R.id.txtdestination);
        txtDate = (AutoCompleteTextView) view.findViewById(R.id.txtDate);
        txtTime = (AutoCompleteTextView) view.findViewById(R.id.txtTime);
        txtPhoneNumber = (AutoCompleteTextView) view.findViewById(R.id.txtmobileno);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                from = txtOrigin.getText().toString();
                to = txtDestination.getText().toString();
                date = txtDate.getText().toString();
                time = txtTime.getText().toString();
                phoneNumber = txtPhoneNumber.getText().toString();

                if (from.trim().equals(""))
                    txtOrigin.setError("Please enter your origin");
                else if (to.trim().equals(""))
                    txtDestination.setError("Please enter your destination");
                else if (date.trim().equals(""))
                    txtTime.setError("Please enter a date of travelling");
                else if (time.trim().equals(""))
                    txtTime.setError("Please enter a time for travilling");
                else if (phoneNumber.trim().length() < 10)
                    txtPhoneNumber.setError("Not a valid mobile no"); // I'm finding solution to validate mobile no
                else {
                    Snackbar snackbar = Snackbar.make(view, "success", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    String mUserId = currentFirebaseUser.getUid();

                    mDatabase.child("users").child(mUserId).child("image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userImage = dataSnapshot.getValue(String.class);
                            mDatabase.child("traveller").child("traveller"+counter).child("image").setValue(userImage);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDatabase.child("traveller").child("traveller"+counter).child("email").setValue(email);
                    mDatabase.child("traveller").child("traveller"+counter).child("userUid").setValue(mUserId);
                    mDatabase.child("traveller").child("traveller"+counter).child("userName").setValue(name);
                    mDatabase.child("traveller").child("traveller"+counter).child("from").setValue(from);
                    mDatabase.child("traveller").child("traveller"+counter).child("to").setValue(to);
                    mDatabase.child("traveller").child("traveller"+counter).child("date").setValue(date);
                    mDatabase.child("traveller").child("traveller"+counter).child("time").setValue(time);
                    mDatabase.child("traveller").child("traveller"+counter).child("phoneNumber").setValue(phoneNumber);
                    mDatabase.child("traveller").child("traveller"+counter).child("from_to_date").setValue(from+"_"+to+"_"+date);
                    Intent i = new Intent(getActivity(), RecyclerViewCarOwner.class);
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_FROM",from);
                    extras.putString("EXTRA_TO",to);
                    extras.putString("EXTRA_DATE",date);
                    i.putExtras(extras);
                    startActivity(i);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOrigin.setText("");
                txtDestination.setText("");
                txtTime.setText("");
                txtPhoneNumber.setText("");
                txtOrigin.setError(null);
                txtDestination.setError(null);
                txtTime.setError(null);
                txtPhoneNumber.setError(null);
            }
        });
        return view;
    }

    private void showTimePicker() {
        TimePickerFragment timeFragment = new TimePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("Hour", calender.get(Calendar.HOUR));
        args.putInt("Minute", calender.get(Calendar.MINUTE));
        timeFragment.setArguments(args);
        timeFragment.setCallBack(ontime);
        timeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }
    };

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            txtTime.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
    };

}
