# safe_app_java

safe_app_java is a Java wrapper for the [safe_app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) API.

> [safe_app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) is a native library which exposes low level API for application development on SAFE Network. It exposes APIs for authorisation and to manage data on the network.

**Maintainer:** Krishna Kumar (krishna.kumar@maidsafe.net)

|Android|Coverage Status|
|:---:|:---:|
|[![Build Status](https://travis-ci.com/maidsafe/safe_app_java.svg?branch=master)](https://travis-ci.com/maidsafe/safe_app_java)|[![Coverage Status](https://coveralls.io/repos/github/maidsafe/safe_app_java/badge.svg?branch=master)](https://coveralls.io/github/maidsafe/safe_app_java?branch=master)|


## Table of contents

1. [High level overview](#high-level-overview)
2. [Project Structure](#project-structure)
3. [Using the package](#using-the-packages)
    - [Android](#including-the-package-in-android-projects)
    - [Desktop](#including-the-package-in-your-desktop-projects)
    - [Documentation](#documentation)
4. [Build instructions](#build-instructions)
5. [Development](#development)
6. [Interfacing with the SAFE Client libraries](#interfacing-with-the-safe-client-libraries)
7. [Contributing](#contributing)
    - [Project board](#project-board)
    - [Issues](#issues)
    - [Commits and Pull Requests](#commits-and-pull-requests)
    - [Changelog and Releases](#changelog-and-releases)
    - [Copyrights](#copyrights)
8. [Further help](#further-help)
9. [License](#license)

## High level overview

The safe_app_java packages are Java bindings for the [safe_app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) Rust library which is needed by any desktop / mobile application to connect and read/write data on the [SAFE Network](https://safenetwork.tech).

The libraries contain API to authenticate with the [SAFE Authenticator](https://github.com/maidsafe/safe-authenticator-mobile) and then read/write data on the SAFE network.

![Authenticaton flow diagram](https://raw.githubusercontent.com/maidsafe/safe_app_nodejs/master/misc/auth-flow-diagram.png)

## Project Structure

The project is a multi-module gradle project with the following modules:

|Module|Content|
|------|-------|
|`api`|This module contains _manually written classes_ with the wrapper functions for the safe_app library that is shared across desktop and mobile|
|`lib`|This module contains the models for safe_app and safe_authenticator that are _automatically generated_ by [safe_bindgen](https://github.com/maidsafe/safe_bindgen).|
|`safe-app`|This module consists of the following:<br> - Desktop-specific `Client` class with a single function - to load the native libraries into memory.<br> - The `Authenticator` class which exposes the Authenticator API for the mock network.<br> - Unit test classes.|
|`safe-app-android`|This module contains the mobile-specific `Client` class with a single function - to load the native libraries into memory.

## Using the packages

### Supported Platforms

- Desktop platforms with 64-bit architecture.
- Android devices with API 24 and above (armeabi-v7a, x86_64 support).

### Including the package in Android projects

Two flavours of the android library are available: `safe-app-android` and `safe-app-android-dev` for the non-mock and mock network respectively.

The libraries are available on the `jcenter()` maven repository. To include the library in your android project, add the required dependency in the `build.gradle` file.

```
dependencies {
    implementation 'net.maidsafe:safe-app-android-dev:0.1.0' // mock network
    implementation 'net.maidsafe:safe-app-android:0.1.0' // non-mock network
}
```
We recommend using the [product flavours](https://developer.android.com/studio/build/build-variants#product-flavors) feature to include dependencies for the mock and non-mock variants of your application as demonstrated in the [safe-getting-started-android](https://github.com/maidsafe/safe-getting-started-android/blob/master/app/build.gradle#L32) application.

> Note: Do not add both the dependencies in the same application/flavour. Gradle will throw an error due to conflicting classes.

### Including the package in desktop projects

For desktop projects, the JAR file can be [manually built from source](#building-for-desktop) and added as a dependency.

### Documentation

The usage of this library in an android application is demonstrated in the [safe-getting-started-android project](https://github.com/maidsafe/safe-getting-started-android). This [step-by-step tutorial](https://hub.safedev.org/platform/android) will guide you through the process of authorising your application to send and receive data on the SAFE network. The API documentation for the safe_app_java library is available at [docs.maidsafe.net/safe_app_java](https://docs.maidsafe.net/safe_app_java).


## Build Instructions

### Pre-requisites

safe_app_java requires

 -  Gradle 4+
 -  Java 8 or above
 -  Android SDK (only for the android library)


### Building for Android

To build the AAR packages for Android, first add the `safe-app-android` module to the `settings.gradle` file in the repository's root. Then make sure that the `ANDROID_HOME` environment variable is set and run the following commands inside the root directory.
```
gradlew :safe-app-android:download-nativelibs
gradlew :safe-app-android:build
```
_For windows, use gradlew.bat_

On a successful build the AAR libraries will be generated in the `safe-app-android/build/outputs/aar` folder.

### Building for Desktop

To build the JAR packages for desktop, run the following commands from the root directory.
```
gradlew :safe-app:download-nativelibs
gradlew :safe-app:build
```
_For windows, use gradlew.bat_

On a successful build the JAR libraries will be generated in the `safe-app/build/libs` folder.

### Building the API documentation

To build the API documentation locally
```
gradlew :safe-app-android:javadoc
```
The generated documentation will be available in the `safe-app-android/build/docs/javadoc` directory.

## Development

### Using gradle:

[Gradle](https://gradle.org) is used as a build tool to automate the various tasks in the project(build, running tests, generating docs etc.). These tasks are defined in the build.gradle files in the `safe-app` and `safe-app-android` modules. The repository includes a [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) using which the defined gradle tasks can be run without a system-wide installation of gradle.

### Running tests:

The unit tests are available in the in [safe-app](safe-app) module and are shared with `safe-app-android`. The following command will run the unit tests for desktop platforms:
```
gradlew :safe-app:test
```

To run the tests as Android instrumentation tests:

```
gradlew :safe-app-android:runInstrumentationTests
```
This will require a supported emulator / Android device (with [USB debugging](https://developer.android.com/studio/debug/dev-options#debugging) enabled)

### Interfacing with the SAFE Client libraries

- The safe_app_java project uses native code that is written in Rust and compiled into platform specific code. You can find more information on the client libraries in [the SAFE client libraries wiki](https://github.com/maidsafe/safe_client_libs/wiki).
- The `download-nativelibs` gradle task is configured to download the native libraries and unzip them to the required directories.

####  Exposing the API:

When compiling the SAFE client libraries with the `bindings` feature, the Java bindings will be generated in the `bindings/java/` folder. These files should be copied into the `lib` module under their respective packages. The native functions available are listed in the `NativeBindings` class. The `api` module should contain wrappers for all of these native functions.

#### Native Handles and garbage collection:

The native functions sometimes perform operations on complex objects in native code. These complex objects are referenced by a native handle which contains a pointer to the object that is held in memory. When this object is no longer needed its memory has to be freed. For this reason, there are a few native functions that need to be called when the native handle Java object is garbage collected. This is done by overriding the `finalize()` function of the `NativeHandle` class to include the native calls to free memory.

## Contributing

Want to contribute? Great :tada:

There are many ways to give back to the project, whether it be writing new code, fixing bugs, or just reporting errors. All forms of contributions are encouraged!

For instructions on how to contribute, see our [Guide to contributing](https://github.com/maidsafe/QA/blob/master/CONTRIBUTING.md).

## Copyrights

Copyrights in the SAFE Network are retained by their contributors. No copyright assignment is required to contribute to this project.

## Further help

Get your developer related questions clarified on [SAFE Dev Forum](https://forum.safedev.org/). If you're looking to share any other ideas or thoughts on the SAFE Network you can reach out on [SAFE Network Forum](https://safenetforum.org/)

## License

This SAFE Network library is dual-licensed under

* the Modified BSD ([LICENSE-BSD](https://opensource.org/licenses/BSD-3-Clause)) or
* the MIT license ([LICENSE-MIT](http://opensource.org/licenses/MIT))

at your option.
