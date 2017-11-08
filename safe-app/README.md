## safe-app-java

### Prerequisites

Java 8 should be installed and JAVA_HOME must be set. 

### Building the project

Execute the downloader script to download the native safe-app libraries.
```
$safe-app> gradlew download-libs
```

Execute `pack` command to get the JAR files for all possible combinations
```
$safe-app> gradlew pack --include-build ../api
```

Run `pack-<platform>-<arch>-<mock/non-mock>` command to build a specific target using,
```
$safe-app> gradlew pack-<platform>-<arch>-<mock/non-mock> --include-build ../api
```

For example,
```
$safe-app> gradlew pack-win-x64-mock --include-build ../api
$safe-app> gradlew pack-win-x64-non-mock --include-build ../api
```
