# rxandroidble2-leak

Project show casing a memory leak when scanning ble devices using RxAndroidBle2.

Steps to reproduce:
- Enable Bluetooth on device
- Enable GPS
- Open app
- Hit back button
- Pull system drop down
- Wait for Canary to analyze heap
- Canary momentarily shows leak.

## Issue
https://github.com/Polidea/RxAndroidBle/issues/607

## Update
Fixed in https://github.com/Polidea/RxAndroidBle/pull/708 🎉
