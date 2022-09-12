# dxFeed Gluon (GraalVM) + Kotlin Application Sample

https://gluonhq.com/  
https://www.graalvm.org/

## Pre-requisites

- MacOS\Windows\Linux
- GraalVM bundle from the https://github.com/gluonhq/graal/releases/
- OpenJDK 11

Please use 22.0 version of a Gluon GraalVM and com.gluonhq.gluonfx-gradle-plugin 1.0.9
[Abount GraalVM environment variables](https://graalvm.github.io/native-build-tools/0.9.6/graalvm-setup.html#_2_setting_up_environment_variables)

## How to build

### Linux:

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-linux-gluon-22.0.0.2-Final
./gradlew clean build
```

### Linux Native Build

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-linux-gluon-22.0.0.2-Final
./gradlew clean build nativeBuild -PbuildProfile=android
```

### MacOS Native Build

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-darwin-gluon-22.0.0.2-Final/Contents/Home
./gradlew clean build nativeBuild -PbuildProfile=ios
```

### Win Native Build (PowerShell)

```shell
$env:GRAALVM_HOME = '/path/to/graalvm-svm-java11-windows-gluon-22.0.0.2-Final'
./gradlew clean build nativeBuild -PbuildProfile=android
```

### Win Native Build (cmd)

```shell
set GRAALVM_HOME='/path/to/graalvm-svm-java11-windows-gluon-22.0.0.2-Final'
gradlew clean build nativeBuild -PbuildProfile=android
```


## How to run

```
./gradlew run
```

### Native package + install + run

```shell
./gradlew nativePackage nativeInstall nativeRun -PbuildProfile=android
```

```shell
./gradlew nativePackage nativeInstall nativeRun -PbuildProfile=ios
```

### SUPER COMBO (rebuild + native build + package + install + run)

```shell
export GRAALVM_HOME=/path/to/graalvm-svm-java11-linux-gluon-22.0.0.2-Final
./gradlew clean build nativeBuild nativePackage nativeInstall nativeRun -PbuildProfile=android
```

Also, you can create iOS\Android packages and install them.
See: https://github.com/gluonhq/gluonfx-gradle-plugin
