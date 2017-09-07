# bt-scan-selector
Android dialog for displaying and selecting nearby bluetooth devices

## Usage
```java
BTScanSelectorBuilder.build(MainActivity.this, new IBTScanSelectorEvents() {
	@Override
	public void onDeviceSelected(BluetoothDevice device) {
		Log.d("DEBUG", String.format("%s\t%s", device.getName(), device.getAddress()));
	}
});
```
