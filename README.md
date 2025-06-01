# My Phone

📱 An Android Auto app that displays real-time telemetry about your connected phone—battery, signal, connectivity, and more.

## Status

🚧 Under development

## Features

- Battery level and charging status
- Mobile network type and signal strength
- Battery saver mode and more to come

## How to configure Android Auto on a Mac

1. Install the Desktop Head Unit. This will run an emulated head unit on your Mac.
    - Install the _Android Auto Desktop Head Unit Emulator_
    - See [Test using the Desktop Head Unit](https://developer.android.com/training/cars/testing/dhu)
2. Set up a modern Android phone emulator. Pixel 9 with API 35+ with Play Store is a good place to start.
3. Enable the developer settings on the phone emulator
4. Download and install the _Android Auto_ APK from APKMirror onto your phone emulator
    - https://www.apkmirror.com/apk/google-inc/android-auto/
    - Sideload and install the APK
5. Launch the _Android Auto_ app on your phone.
    - The app doesn't have a launcher icon, so you can access _Android Auto_ via **Settings** > **Apps** > **See all apps** > **Android Auto** > **Additional settings in the app**
6. Enable developer settings in the _Android Auto_ app.
    - Scroll down to **Version**
    - Tap 10 times on **Version**
7. Turn on the head unit server
    - Tap the ... menu in the _Android Auto_ app
    - Start head unit server
8. Enable adb port forwarding
    - `adb forward tcp:5277 tcp:5277`
9. Launch the desktop head unit
    - `~/Library/Android/sdk/extras/google/auto/desktop-head-unit`

## License

[MIT](LICENSE)
