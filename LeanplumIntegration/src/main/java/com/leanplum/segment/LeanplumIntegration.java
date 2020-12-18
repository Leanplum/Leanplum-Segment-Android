// Copyright 2020, Leanplum, Inc.

package com.leanplum.segment;

import android.app.Application;
import android.text.TextUtils;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.LeanplumException;
import com.segment.analytics.Analytics;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.IdentifyPayload;
import com.segment.analytics.integrations.Integration;
import com.segment.analytics.integrations.Logger;
import com.segment.analytics.integrations.ScreenPayload;
import com.segment.analytics.integrations.TrackPayload;

/**
 * Leanplum Segment Integration
 *
 * @author Ben Marten
 */
public class LeanplumIntegration extends Integration {

  public static final String LEANPLUM_SEGMENT_KEY = "Leanplum";
  public static final Factory FACTORY = new Factory() {
    @Override
    public Integration<?> create(ValueMap settings, Analytics analytics) {
      Logger logger = analytics.logger(LEANPLUM_SEGMENT_KEY);
      String appId = settings.getString("appId");
      String key = settings.getString("clientKey");
      Boolean isDevelopmentMode = settings.getBoolean("devMode", false);

      if (TextUtils.isEmpty(appId)) {
        throw new LeanplumException("Please add Leanplum app id in Segment " +
            "settings.");
      }
      if (TextUtils.isEmpty(key)) {
        throw new LeanplumException("Please add Leanplum client key in " +
            "Segment settings.");
      }

      return new LeanplumIntegration(analytics.getApplication(), appId, key,
          logger, isDevelopmentMode);
    }

    @Override
    public String key() {
      return LEANPLUM_SEGMENT_KEY;
    }
  };
  private Logger logger;

  public LeanplumIntegration(Application application, String appId, String key,
      Logger logger, Boolean isDevelopmentMode) {
    try {
      this.logger = logger;
      logger.verbose("Registering Leanplum Integration, appId: %s, key: %s, " +
          "devMode: %b", appId, key, isDevelopmentMode);

      if (isDevelopmentMode) {
        Leanplum.setAppIdForDevelopmentMode(appId, key);
      } else {
        Leanplum.setAppIdForProductionMode(appId, key);
      }

      LeanplumActivityHelper.enableLifecycleCallbacks(application);
      Leanplum.start(application);
      logger.verbose("Leanplum started.");
    } catch (Throwable t) {
      logger.error(t, "Failed to start Leanplum Segment Integration.");
    }
  }

  @Override
  public void identify(final IdentifyPayload identify) {
    try {
      logger.verbose("Identify: %s", identify);
      // Set user ID & map traits to user attributes
      Leanplum.setUserAttributes(identify.userId(), identify.traits());
    } catch (Throwable t) {
      logger.error(t, "Failed to set user attributes.");
    }
  }

  @Override
  public void track(TrackPayload track) {
    try {
      logger.verbose("Track: %s", track);
      // Since Leanplum has value field that can be associated with any event,
      // we have to extract that field from Segment and send it with our event as a value.
      Double value = 0.0D;

      if (track.properties() != null) {
        value = track.properties().getDouble("value", 0.0D);
      }

      if (value != 0.0D) {
        Leanplum.track(track.event(), value, track.properties());
      } else {
        Leanplum.track(track.event(), track.properties());
      }
    } catch (Throwable t) {
      logger.error(t, "Failed to track event with Leanplum.");
    }
  }

  @Override
  public void screen(ScreenPayload screen) {
    try {
      logger.verbose("Screen: %s", screen);
      Leanplum.advanceTo(screen.event(), screen.properties());
    } catch (Throwable t) {
      logger.error(t, "Failed to screen event with Leanplum.");
    }
  }

  @Override
  public void flush() {
    // no implementation, because Segment is flushing on a number of events instead of time
  }
}
