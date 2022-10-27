# Leanplum Segment Integration for Android
A Segment integration for the Leanplum Android SDK.

## Installation
To install the Leanplum Segment integration, simply add these lines to your
modules build.gradle file:
```groovy
dependencies {
  implementation 'com.segment.analytics.android:analytics:4.9.0'
  implementation 'com.leanplum.segment:LeanplumIntegration:1.2.0'
}
```
Please add at least the following permissions to your applications AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

If you want to use the advanced features of Leanplum, please add the additional permissions, as described [here](https://www.leanplum.com/docs#/setup/android).

That's it! Now you can use the Segment SDK and also the [advanced features](https://www.leanplum.com/docs#/docs) of the Leanplum SDK.

## Usage
Add the following lines to your Application or Controller:

```java
private static final String SEGMENT_WRITE_KEY = " ... ";

Analytics analytics = new Analytics
  .Builder(getApplicationContext(), SEGMENT_WRITE_KEY)
  .use(LeanplumIntegration.FACTORY)
  .build();
```

Now you can use Segment as you are used to, e.g.:
```java
analytics.track(" ... ", ... );
```

Note: There is no need to explicitly call Leanplum.start, as it is called within the LeanplumIntegration.

In addition to that you can also use the advanced features of Leanplum. Once the Leanplum SDK is successfully registered, Segement executes a callback:
```java
analytics.onIntegrationReady(LeanplumIntegration.LEANPLUM_SEGMENT_KEY,
    new Analytics.Callback() {
      @Override
      public void onReady(Object instance) {
        Leanplum.addVariablesChangedHandler( ... );
      }
    });
```

## Example
We have included a sample application.

1. To run the sample app, open this folder in Android Studio.
1. Choose & run target `Example`

## Tests
We have included unit tests for the integration.

1. To run the unit tests, open this folder in Android Studio.
1. Choose & run target `LeanplumIntegrationTests`

## Install Specific Version of SDK's
By default this integration pulls in the latest versions of the Leanplum SDK and the Segment SDK. If you rather want to use a specific version, simply exclude them from the integration and specify the required versions in your build.gradle file directly.
```groovy
implementation('com.leanplum.segment:LeanplumIntegration:1.2.0') {
    exclude group: 'com.segment.analytics.android', module: 'analytics'
    exclude group: 'com.leanplum', module: 'Leanplum'
}
implementation 'com.segment.analytics.android:analytics:4.9.0'
implementation 'com.leanplum:leanplum-core:6.0.0'
```

## Deploy
To upload a new version refer to the [SDK Release Process](https://wizrocket.atlassian.net/wiki/spaces/E2/pages/3618572675/SDK+Release+Process+Jul+2020) docs.

## License
See LICENSE file.
