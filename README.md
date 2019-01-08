# safe_app_java

safe_app_java is a Java wrapper for the [safe_app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) API.

> [safe_app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) is a native library which exposes low level API for application development on SAFE Network. It exposes APIs for authorisation and to manage data on the network.

**Maintainer:** Krishna Kumar (krishna.kumar@maidsafe.net)

|Android|
|:-:|
|[![Build Status](https://travis-ci.org/maidsafe/safe_app_java.svg?branch=master)](https://travis-ci.org/maidsafe/safe_app_java)|


## API Documentation

The API documentation for safe_app_java library is available at [docs.maidsafe.net/safe_app_java](https://docs.maidsafe.net/safe_app_java).

## Supported Platforms

Android API 24 and above (armeabi-v7a, x86_64 support).

## Getting started with Android

Two flavours of the android library are available: `safe-app-android` and `safe-app-android-dev` for the non-mock and mock network respectively.

### Usage:

The libraries are available on the `jcenter()` maven repository. To include the library in your android project add the following to the dependencies in the `build.gradle` file.

```
dependencies {
    implementation 'net.maidsafe:safe-app-android-dev:0.1.0' // mock network
    implementation 'net.maidsafe:safe-app-android:0.1.0' // non-mock network
}
```
We recommend using the [product flavours](https://developer.android.com/studio/build/build-variants#product-flavors) feature to include dependencies for the mock and non-mock variants of your application as demonstrated in the [safe-getting-started-android](https://github.com/maidsafe/safe-getting-started-android/blob/master/app/build.gradle#L22) application.

## Build Instructions

#### Pre-requisites

safe_app_java requires

 -  Gradle 4+
 -  Java 8
 -  Android SDK


To build the AAR packages for Android, first make sure that the `ANDROID_HOME` environment variable is set and then run the following commands inside the root directory.
```
gradlew :safe-app-android:download-nativelibs
gradlew :safe-app-android:build
```
On a successful build the AAR libraries will be generated in the `safe-app-android/build/outputs/aar` folder.

To build the API documentation locally
```
gradlew :safe-app-android:javadoc
```
The generated documentation will be available in the `safe-app-android/build/docs/javadoc` directory.

### Running tests:

The unit tests in [safe-app](safe-app) are shared with safe-app-android. To run the Instrumentation tests:
```
gradlew :safe-app-android:runInstrumentationTests
```
This will require a supported emulator / Android device (with [USB debugging](https://developer.android.com/studio/debug/dev-options#debugging) enabled)

## Further help

Get your developer related questions clarified on [SAFE Dev Forum](https://forum.safedev.org/). If your looking to share any other ideas or thoughts on the SAFENetwork you can reach out on [SAFE Network Forum](https://safenetforum.org/)

## License

This SAFE Network library is dual-licensed under

* the Modified BSD ([LICENSE-BSD](https://opensource.org/licenses/BSD-3-Clause)) or
* the MIT license ([LICENSE-MIT](http://opensource.org/licenses/MIT))

at your option.

## Contribution

Copyrights in the SAFE Network are retained by their contributors. No copyright assignment is required to contribute to this project.
