package com.example.lenovo.trafficservices;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdsPublishAndDisplay extends BaseActivity {

    Button displayAds, publishAd;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_publish_and_display);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        displayAds = (Button) findViewById(R.id.btnDisplayAds);
        publishAd = (Button) findViewById(R.id.btnPublishAds);
        displayAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DisplayAdsActivity.class);
                startActivity(intent);
            }
        });

        publishAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), TravelActivity.class);
                startActivity(intent);
            }
        });
    }
}
