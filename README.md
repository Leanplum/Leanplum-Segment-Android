# Leanplum Segment Integration for Android
A Segment integration for the Leanplum Android SDK.

## Installation
To install the Leanplum Segment integration, simply add these lines to your
modules build.gradle file:

```java
compile 'com.segment.analytics.android:analytics:4.0.4'
compile 'com.leanplum.segment:LeanplumIntegration:1.0.0'
```
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

In addition to that you can also use the advanced features of Leanplum, e.g.:
```java
Leanplum.addVariablesChangedHandler( ... );
```

## Example
We have included a sample application.

1. To run the sample app, open this folder in Android Studio.
2. Choose & run target `Example`

## Tests
We have included unit tests for the integration.

1. To run the unit tests, open this folder in Android Studio.
2. Choose & run target `LeanplumIntegrationTests`

## Install Specific Version of SDK's
By default this integration pulls in the latest versions of the Leanplum SDK and the Segment SDK. If you rather want to use a specific version, simply exclude them from the integration and specify the required versions in your build.gradle file directly.
```java
compile('com.leanplum.segment:LeanplumIntegration:1.0.0') {
    exclude group: 'com.segment.analytics.android', module: 'analytics'
    exclude group: 'com.leanplum', module: 'Leanplum'
}
compile 'com.segment.analytics.android:analytics:4.0.0'
compile 'com.leanplum:Leanplum:1.2.+'
```

## Deploy to jCenter and mavenCentral
To upload a new version to jCenter or mavenCentral we use bintray.com.

1. Get your API Key from bintray.com (See Left Menu in Edit Profile) and add it to your `~/.gradle/gradle.properties` file, e.g.:
  
  ```groovy
  bintrayUser=benmarten
  bintrayApiKey= [...]
  ```
3. Increase the package version in gradle.properties project file:
  
  ```groovy
  bintrayPackageVersion=1.0.0
  ```
4. Run gradle build & upload scripts
  
  ```bash
  gradle install
  gradle bintrayUpload
  ```
5. Go to bintray.com to double check your new release.

## License
See LICENSE file.