package com.shawn.cyber.usbmuxd.protocol;

import com.dd.plist.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParseException;

public class PlistMessageService {

	public static byte[] buildListenConnectionMsg() {
		byte[] bytes = buildPlistConnectionMsg();
		return buildByteMsg(bytes).array();
	}

	public static byte[] buildConnectMsg(int deviceId,int port) {
		byte[] bytes = buildDeviceConnectMsg(deviceId,port);
		return buildByteMsg(bytes).array();
	}

	public static byte[] buildListMsg(){
		byte[] bytes = buildDeviceListMsg();
		return buildByteMsg(bytes).array();
	}

	public static byte[] buildStatsMsg(){
		byte[] bytes = "GET /status HTTP/1.1\rHost: localhost:8100\rAccept-Encoding: identity\r\r".getBytes();
		return buildByteMsg(bytes).array();
	}

	protected static byte[] buildDeviceListMsg() {
		NSDictionary root = new NSDictionary();
		root.put("MessageType", "ListDevices");
		root.put("ClientVersionString", "cyber-usbmux-driver");
		root.put("ProgName", "cyber-usbmux-driver");
		root.put("kLibUSBMuxVersion", new NSNumber(3));
		String s = root.toXMLPropertyList();
		//System.out.println(s);
		return s.getBytes(Charset.forName("UTF-8"));
	}

	protected static byte[] buildDeviceConnectMsg(int deviceId, int port) {
		NSDictionary root = new NSDictionary();
		root.put("MessageType", "Connect");
		root.put("ClientVersionString", "mogaleaf-usbmux-driver");
		root.put("ProgName", "mogaleaf-usbmux-driver");
		root.put("DeviceID", new NSNumber(deviceId));
		root.put("PortNumber", new NSNumber(swapPortNumber(port)));
		String s = root.toXMLPropertyList();
		//System.out.println(s);
		return s.getBytes(Charset.forName("UTF-8"));
	}

	protected static int swapPortNumber(int port) {
		return ((port << 8) & 0xFF00) | (port >> 8);
	}

	protected static byte[] buildPlistConnectionMsg(){
		NSDictionary root = new NSDictionary();
		root.put("MessageType", "Listen");
		root.put("ClientVersionString", "mogaleaf-usbmux-driver");
		root.put("ProgName", "mogaleaf-usbmux-driver");
		String s = root.toXMLPropertyList();
		//System.out.println(s);
		return s.getBytes(Charset.forName("UTF-8"));
	}

	protected static ByteBuffer buildByteMsg(byte[] bytes) {
		int len = (16 + bytes.length);
		int version = 1;
		int request = 8;
		int tag = 1;
		ByteBuffer buffer = ByteBuffer.allocate(len);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(0, len);
		buffer.putInt(4, version);
		buffer.putInt(8, request);
		buffer.putInt(12, tag);
		int i = 16;
		for (byte aByte : bytes) {
			buffer.put(i++, aByte);
		}
		return buffer;
	}

	public static NSDictionary getNsDictionary(InputStream input, int size) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
		byte[] body = new byte[size];
		input.read(body);
		NSObject parse = PropertyListParser.parse(body);
		return (NSDictionary) parse;
	}

	public static int getSize(InputStream input) throws IOException {
		byte[] header = new byte[16];
		input.read(header);
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(header);
		return buffer.getInt(0) - 16;
	}


	public static ResultType retrieveMsgType(NSDictionary dico) {
		NSString messageType = (NSString) dico.get("MessageType");
		return ResultType.valueOf(messageType.getContent());
	}

	public enum ResultType {
		Attached, Detached, Paired, Result, Error
	}

	public static byte[] tryLockDown() {
		NSDictionary root = new NSDictionary();
		root.put("Label", "iTunesHelper");
		root.put("Request", "QueryType");
		String s = root.toXMLPropertyList();
		System.out.println(s);
		return s.getBytes(Charset.forName("UTF-8"));
	}

}
