## safe_app_java

**Maintainer:** Krishna Kumar (krishna.kumar@maidsafe.net)

safe_app_java library for generating JAR and AAR packages.

### Prerequisites

safe_app_java requires

 -  Gradle 4.3
 -  Java 8
 -  Android SDK (target SDK Version 26)

### Build Instructions

safe_app_java has sub projects for generating JAR and AAR files for exposing [safe-app](https://github.com/maidsafe/safe_client_libs/tree/master/safe_app) API.

Download the native libraries for desktop and android by executing,
```
gradle download-nativelibs
```

#### safe-app

[safe-app](safe-app) project builds platform specific JAR files for linux, osx and windows (mock and non-mock).

#####  Build Commands

Execute `pack` task to get the JAR files for all possible combinations
```
gradlew :safe-app:pack
```
Run pack-<platform>-<arch>-<mock/non-mock> command to build a specific target using,
```
gradlew :safe-app:pack-<platform>-<arch>-<mock/non-mock>
```
For example,
```
gradlew :safe-app:pack-win-x64-mock
gradlew :safe-app:pack-win-x64-non-mock
```

The JAR files would be placed in `safe-app/build/libs` if the build is successful

### safe-app-android

[safe-app-android](safe-app-android) project builds AAR files (mock and non-mock).

#####  Build Commands

Make sure ANDROID_HOME environment variable is set.

Execute task
```
gradlew :safe-app-android:build
```


# License

This SAFE Network library is dual-licensed under

* the Modified BSD ([LICENSE-BSD](https://opensource.org/licenses/BSD-3-Clause)) or
* the MIT license ([LICENSE-MIT](http://opensource.org/licenses/MIT))

at your option.

# Contribution

Copyrights in the SAFE Network are retained by their contributors. No copyright assignment is required to contribute to this project.
