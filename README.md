## safe_app_java

**Maintainer:** Krishna Kumar (krishna.kuamr@maidsafe.net)

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

[safe-app-android](safe-app-android) project builds platform specific AAR files (mock and non-mock).

#####  Build Commands

Make sure ANDROID_HOME environment variable is set.

Execute task
```
gradlew :safe-app-android:build
```


# License

Licensed under either of

* the MaidSafe.net Commercial License, version 1.0 or later ([LICENSE](LICENSE))
* the General Public License (GPL), version 3 ([COPYING](COPYING) or http://www.gnu.org/licenses/gpl-3.0.en.html)

at your option.

# Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the
work by you, as defined in the MaidSafe Contributor Agreement, version 1.1 ([CONTRIBUTOR]
(CONTRIBUTOR)), shall be dual licensed as above, and you agree to be bound by the terms of the
MaidSafe Contributor Agreement, version 1.1.
