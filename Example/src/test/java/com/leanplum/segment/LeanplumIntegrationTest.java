package com.leanplum.segment;

import android.app.Application;

import com.leanplum.Leanplum;
import com.segment.analytics.Analytics;
import com.segment.analytics.Traits;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.IdentifyPayload;
import com.segment.analytics.integrations.Logger;
import com.segment.analytics.integrations.ScreenPayload;
import com.segment.analytics.integrations.TrackPayload;
import com.segment.analytics.test.IdentifyPayloadBuilder;
import com.segment.analytics.test.ScreenPayloadBuilder;
import com.segment.analytics.test.TrackPayloadBuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static com.segment.analytics.Utils.createTraits;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(Leanplum.class)
public class LeanplumIntegrationTest {
  @Rule
  public PowerMockRule rule = new PowerMockRule();

  @Mock
  Application context;
  @Mock
  Analytics analytics;

  LeanplumIntegration integration;
  Logger logger;

  @Before
  public void setUp() {
    initMocks(this); // initializes the mockes objecs by annoation
    mockStatic(Leanplum.class);
    logger = Logger.with(Analytics.LogLevel.DEBUG);
    when(analytics.logger("Leanplum")).thenReturn(logger);
    when(analytics.getApplication()).thenReturn(context);

    integration =
        new LeanplumIntegration(context, "appId", "key", logger, true);
  }

  @Test
  public void integrationConstructor() {
    assertNotNull(integration);
  }

  @Test
  public void factory() {
    ValueMap settings = new ValueMap()
        .putValue("appId", "foo")
        .putValue("clientKey", "bar")
        .putValue("devMode", true);

    LeanplumIntegration integration = (LeanplumIntegration)
        LeanplumIntegration.FACTORY.create(settings, analytics);
    assertNotNull(integration);
  }

  @Test
  public void identify() throws Exception {
    Traits traits = createTraits("foo").putEmail("foo@bar.com");
    IdentifyPayload identifyPayload = new IdentifyPayloadBuilder().traits
        (traits).build();

    PowerMockito.doNothing()
        .when(Leanplum.class, "setUserAttributes", "", new HashMap());
    Leanplum.setUserAttributes(identifyPayload.userId(), identifyPayload.traits());

    PowerMockito.verifyStatic(Mockito.times(1));
    integration.identify(identifyPayload);
  }

  @Test
  public void track() throws Exception {
    Traits traits = createTraits("foo").putEmail("foo@bar.com");
    TrackPayload trackPayload =
        new TrackPayloadBuilder().traits(traits).build();

    PowerMockito.doNothing().when(Leanplum.class, "track", "", "");
    Leanplum.track(trackPayload.event(), trackPayload.properties());

    PowerMockito.verifyStatic(Mockito.times(1));
    integration.track(trackPayload);
  }

  @Test
  public void screen() throws Exception {
    Traits traits = createTraits("foo").putEmail("foo@bar.com");
    ScreenPayload screenPayload = new ScreenPayloadBuilder().traits(traits)
        .build();

    PowerMockito.doNothing().when(Leanplum.class, "advanceTo", "", "");
    Leanplum.advanceTo(screenPayload.event(), screenPayload.properties());

    PowerMockito.verifyStatic(Mockito.times(1));
    integration.screen(screenPayload);
  }
}
