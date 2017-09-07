# bt-scan-selector
[![](https://jitpack.io/v/phearme/bt-scan-selector.svg)](https://jitpack.io/#phearme/bt-scan-selector)

Android dialog for displaying and selecting nearby bluetooth devices

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
	compile 'com.github.phearme:bt-scan-selector:1.0.0'
}
```

## Usage
```java
BTScanSelectorBuilder.build(MainActivity.this, new IBTScanSelectorEvents() {
	@Override
	public void onDeviceSelected(BluetoothDevice device) {
		Log.d("DEBUG", String.format("%s\t%s", device.getName(), device.getAddress()));
	}
});
```
