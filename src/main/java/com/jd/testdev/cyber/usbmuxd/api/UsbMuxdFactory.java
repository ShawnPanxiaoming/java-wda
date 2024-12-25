package com.jd.testdev.cyber.usbmuxd.api;

import com.jd.testdev.cyber.usbmuxd.protocol.linux.UsbMuxdLinux;
import com.jd.testdev.cyber.usbmuxd.protocol.win.UsbMuxdWindows;

public class UsbMuxdFactory {

	public static IUsbMuxd getInstance(String serialNo, int wdaPort) {
		String property = System.getProperty("os.name");
		if (property.startsWith("Window")) {
			return new UsbMuxdWindows(serialNo, wdaPort);
		} else {
			return new UsbMuxdLinux(serialNo, wdaPort);
		}
	}
}
