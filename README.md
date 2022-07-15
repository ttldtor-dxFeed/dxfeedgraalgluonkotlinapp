# dxFeed Gluon (GraalVM) + Kotlin Application Sample

https://gluonhq.com/  
https://www.graalvm.org/

## Pre-requisites

- MacOS\Windows\Linux
- GraalVM bundle from the https://github.com/gluonhq/graal/releases/
- OpenJDK 11

## How to build

### Linux:

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-linux-gluon-linux-gluon-22.1.0.1-Final
./gradlew clean build
```

### Linux Native Build

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-linux-gluon-linux-gluon-22.1.0.1-Final
./gradlew clean build nativeBuild
```

## How to run

```
./gradlew run
```

### Native run

```
./gradlew nativeRun
```
