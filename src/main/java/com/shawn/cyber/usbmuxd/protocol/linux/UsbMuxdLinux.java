package com.shawn.cyber.usbmuxd.protocol.linux;

import com.shawn.cyber.usbmuxd.protocol.UsbMuxdImpl;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;


public class UsbMuxdLinux extends UsbMuxdImpl {

	public UsbMuxdLinux(String serial, int wdaPort){
		super(serial, wdaPort);
	}

	@Override
	protected SocketAddress getAddress() throws IOException {
		return new AFUNIXSocketAddress(new File("/var/run/usbmuxd"));
	}

	@Override
	protected Socket getSocketImpl() throws IOException {
		return AFUNIXSocket.newInstance();
	}
}
