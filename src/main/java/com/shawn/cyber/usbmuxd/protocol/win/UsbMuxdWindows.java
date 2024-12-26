package com.shawn.cyber.usbmuxd.protocol.win;

import com.shawn.cyber.usbmuxd.protocol.UsbMuxdImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UsbMuxdWindows extends UsbMuxdImpl {

	public UsbMuxdWindows(String serialNo, int wdaPort){
		super(serialNo, wdaPort);
	}


	@Override
	public SocketAddress getAddress() {
		return  new InetSocketAddress("127.0.0.1",getWdaPort() );
	}

	@Override
	protected Socket getSocketImpl() throws IOException {
		return new Socket();
	}


}
