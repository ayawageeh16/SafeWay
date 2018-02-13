package com.example.lenovo.trafficservices;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import kidnaping.GPSTracker;


public class MessagesPopupActivity extends Activity {
    ImageButton b1, b2, b3, b4, b5, b6, b7, b8, b9;
    ImageView btnClose;
    private LocationListener locationListener ;
    private LocationManager locationManager ;
    double latitude, longitude;
    int phi = 1 , omega = 1;
    private DatabaseReference firebaseReferenceToGetUsersCount;
    int counter=0;
    String adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_popup_activity);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String mUserId = currentFirebaseUser.getUid();
        firebaseReferenceToGetUsersCount= FirebaseDatabase.getInstance().getReference()
                .child("users").child(mUserId).child("notifications");
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

        GPSTracker tracker = new GPSTracker(this);
        latitude = tracker.getLatitude();
        longitude = tracker.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                    adress= strReturnedAddress.toString();
            } else {
                adress="No Address returned!";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           adress="Canont get Address!";
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        b1 = (ImageButton) findViewById(R.id.imageButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 180));
                if (ActivityCompat.checkSelfPermission(MessagesPopupActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        b2 = (ImageButton) findViewById(R.id.imageButton1);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 122));
                if (ActivityCompat.checkSelfPermission(MessagesPopupActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        b3 = (ImageButton) findViewById(R.id.imageButton2);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 123));
                if (ActivityCompat.checkSelfPermission(MessagesPopupActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        b4 = (ImageButton) findViewById(R.id.imageButton3);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trafficJam = "There is a heavy traffic jam in your area";
                nearest_users(trafficJam);
            }
        });
        b5 = (ImageButton) findViewById(R.id.imageButton4);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accident = "There is an accident in your area";
                nearest_users(accident);
            }
        });
        b6 = (ImageButton) findViewById(R.id.imageButton5);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roadConstruction = "There is a road Construction in your area";
                nearest_users(roadConstruction);
            }
        });
        b7 = (ImageButton) findViewById(R.id.imageButton6);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roadClosure = "There is a road closure in your area";
                nearest_users(roadClosure);
            }
        });
        b8 = (ImageButton) findViewById(R.id.imageButton7);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fires = "There is fires in your area";
                nearest_users(fires);
            }
        });
        b9 = (ImageButton) findViewById(R.id.imageButton8);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hazard = "There is a hazard in your area";
                nearest_users(hazard);
            }
        });
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void nearest_users(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        final mylocation location = child.getValue(mylocation.class);

                          /* Toast.makeText(getApplicationContext(), String.valueOf(location.getLocationLat())
                                    + "," + String.valueOf(location.getLocationLong()), Toast.LENGTH_LONG).show();*/
                       /* if(location.getLocationLat()>latitude-.1 && location.getLocationLat()<latitude+.1
                                && location.getLocationLong()>longitude-.1&&location.getLocationLong()<longitude+.1) {*/
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        pushNotification(location.getToken(), message);
                                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                        String date = df.format(Calendar.getInstance().getTime());
                                        database.child("users").child(location.getUid()).child("notifications")
                                                .child("notification" + counter).child("notification")
                                                .setValue(message + "\n" + "Location:\n" + adress);
                                        database.child("users").child(location.getUid()).child("notifications")
                                                .child("notification" + counter).child("date").setValue(date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();
                       // }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class mylocation {

        public Double locationLat;
        public Double locationLong;
        public String token;
        public String uid;

        public mylocation(Double locationLat, Double locationLong, String token, String uid) {
            this.locationLat = locationLat;
            this.locationLong = locationLong;
            this.token = token;
            this.uid = uid;
        }

        public mylocation() {
        }

        public void setLocationLat(Double locationLat) {
            this.locationLat = locationLat;
        }

        public void setLocationLong(Double locationLong) {
            this.locationLong = locationLong;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setUid(String uid){
            this.uid = uid;
        }

        public Double getLocationLat() {
            return locationLat;
        }

        public Double getLocationLong() {
            return locationLong;
        }

        public String getToken() {
            return token;
        }

        public String getUid(){
            return uid;
        }


    }

    private void pushNotification(String token, String message) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Safe Way");
            jNotification.put("text", message);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
          //
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            jData.put("picture_url", "http://opsbug.com/static/google-io.jpg");

            jPayload.put("to", token);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AIzaSyAwADULmRdNfLw3Rft-yjvRfA1eXSue8Ow");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("Response", resp);
                    //txtStatus.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    static String convertStreamToString(InputStream inputStream) {
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
    }


    public class MyFirebaseMessagingService extends FirebaseMessagingService {
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);

            // If the application is in the foreground handle both data and notification messages here.
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> map = remoteMessage.getData();

            sendNotification(notification.getTitle(), notification.getBody(), map);
        }

        private void sendNotification(String title, String body, Map<String, String> map) {
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher);

            try {
                String picture_url = map.get("picture_url");
                if (picture_url != null && !"".equals(picture_url)) {
                    URL url = new URL(picture_url);
                    Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            notificationBuilder.setLights(Color.YELLOW, 1000, 300);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }

    }

}

