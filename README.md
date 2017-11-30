# bt-scan-selector
[![](https://jitpack.io/v/phearme/bt-scan-selector.svg)](https://jitpack.io/#phearme/bt-scan-selector)

Android dialog for displaying and selecting nearby bluetooth devices

<img src="art/s001.png"  width="45%" />

## Installation
Add the JitPack repository to your root **Project** gradle file at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency to the **Module** gradle file:
```gradle
dependencies {
	...
	compile 'com.github.phearme:bt-scan-selector:1.0.2'
}
```

## Usage
```java
BTScanSelectorBuilder.build(MainActivity.this, new ABTScanSelectorEventsHandler() {
	@Override
	public void onDeviceSelected(BluetoothDevice device) {
		Log.d("DEBUG", String.format("device selected by user: %s\t%s", device.getName(), device.getAddress()));
	}
});
```
### Filter out devices
Filtering out devices is possible by overriding the `onDeviceFound` method. Return `true` if you want the device to be included in the result, `false` otherwise.
```java
BTScanSelectorBuilder.build(MainActivity.this, new ABTScanSelectorEventsHandler() {
	@Override
    	public boolean onDeviceFound(BluetoothDevice device) {
        	return device.getName().equals("myDevice") || device.getAddress().equals("AA:BB:CC:DD:EE:FF");
    	}
    
    	@Override
	public void onDeviceSelected(BluetoothDevice device) {
		Log.d("DEBUG", String.format("device selected by user: %s\t%s", device.getName(), device.getAddress()));
	}
});

```
