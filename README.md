This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform
  applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part,
      the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose
  Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run
widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run
widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

### Add iOS native dependencies

1. Open project in Xcode. File -> Target -> Framework
2. The previous step will create a new framework folder in the iosApp directory with the name of
   your Framework you provided.
3. In the Framework folder, create a Swift file (e.g., AdsManagerBridge.swift).
4. With the help of xcode package manager, add the native dependency you want to use in your
   project. (e.g. GoogleMobileAds)
5. Write your bridge:

```swift
import Foundation
import GoogleMobileAds

@objc public class AdsManagerBridge: NSObject {
    private var bannerView: GADBannerView?

    @objc public func initializeAds(_ adUnitId: String) {
        bannerView = GADBannerView(adSize: kGADAdSizeBanner)
        bannerView?.adUnitID = adUnitId
        bannerView?.load(GADRequest())
    }
}
```

6. Click on the file manager project (at the root of your project `iosApp`), find your Framework in
   the list, select it -> go to Build Settings ->
    - Swift Compiler Settings -> Install Generated Header -> **Set to Yes**
    - Packaging -> Defines Module -> **Set to Yes**
    - Deployment -> Skip Install -> **Set to No**
7. Clean (CMD + Shift + K) and Build (CMD + B) the project. If needed File -> Packages -> Resolve
   Package Versions
8. In Finder, go to the derived data folder of your project
   (~
   /Library/Developer/Xcode/DerivedData/YourProjectName-xxxxxx/Build/Products/Debug-iphonesimulator/)
   and find your Framework there. The folder should be called: `AddsManagerBridge.framework` and it
   should contain `Headers` folder.
9. Copy the `AddsManagerBridge.framework` folder to the `composeApp/iosFrameworks` folder of your
   project.
10. In the `composeApp/src/nativeInterop/cinterop/` create a new `.def` file (e.g.
    `AdsBridge.def`) with the following content:

```
language = Objective-C
modules = AddsManagerBridge
package = analytics.bridge
```

11. Update the `composeApp/build.gradle.kts` file to include the new framework:

```kotlin
kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.compilations.getByName("main") {
            val analyticsBridge by cinterops.creating {
                definitionFile.set(project.file("src/nativeInterop/cinterop/AddsBridge.def"))
                compilerOpts(
                    "-framework", "AddsManagerBridge",
                    "-F", "${projectDir}/iosFrameworks"
                )

            }
        }

        iosTarget.binaries.all {
            linkerOpts(
                "-framework", "AddsManagerBridge",
                "-F", "${projectDir}/iosFrameworks"
            )
        }

        extraOpts += listOf("-compiler-option", "-fmodules")
    }
}
```

12. Sync project with Gradle files.
13. Now you can use the bridge in your shared code:

```kotlin
import adds.bridge.AddsManagerBridge
```

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…