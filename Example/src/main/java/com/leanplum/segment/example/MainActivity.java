// Copyright 2016, Leanplum, Inc.

package com.leanplum.segment.example;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.leanplum.Leanplum;
import com.leanplum.segment.LeanplumIntegration;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;

public class MainActivity extends AppCompatActivity {

  private static final String SEGMENT_WRITE_KEY =
      "5L3HqU7fveZSxm5OUtefU4r3zIO2pX56";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Create an analytics client with the given context and Segment write key.
    Analytics analytics = new Analytics
        .Builder(getApplicationContext(), SEGMENT_WRITE_KEY)
        .use(LeanplumIntegration.FACTORY)
        .build();

    analytics.onIntegrationReady(LeanplumIntegration.LEANPLUM_SEGMENT_KEY,
        new Analytics.Callback() {
          @Override
          public void onReady(Object instance) {
            Leanplum.track("test");
          }
        });

    // Set the initialized instance as a globally accessible instance.
    Analytics.setSingletonInstance(analytics);

    Traits traits = new Traits()
        .putName("First Last")
        .putEmail("first@last.com");
    Analytics.with(getApplicationContext())
        .identify("f4ca124298", traits, null);

    Properties properties = new Properties()
        .putValue("plan", "Enterprise");
    Analytics.with(getApplicationContext()).track("Signed up", properties);
    Analytics.with(getApplicationContext()).screen("Start");
  }

  public void trackButtonClicked(View view) {
    Properties properties = new Properties()
        .putPrice(0.99)
        .putCurrency("USD")
        .putTitle("InApp Purchase");
    Analytics.with(getApplicationContext())
        .track("Track Button Clicked!", properties);
  }

}
