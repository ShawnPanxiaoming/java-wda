package com.jd.testdev.cyber.usbmuxd.api.model;

import java.io.InputStream;
import java.io.OutputStream;

/*
* I/O of the established connection and device information.
 */
public class UsbMuxdConnection {
	public InputStream inputStream;
	public OutputStream outputStream;
	public Device device;
	public static UsbMuxdConnection usbMuxdConnection;

	private UsbMuxdConnection(){}

	public static UsbMuxdConnection sharedInstance(){
		if (usbMuxdConnection == null){
			usbMuxdConnection = new UsbMuxdConnection();
		}
		return usbMuxdConnection;
	}
}
