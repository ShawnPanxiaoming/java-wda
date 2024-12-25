package com.jd.testdev.cyber.usbmuxd.protocol;

import com.jd.testdev.cyber.service.WDAException;
import com.jd.testdev.cyber.usbmuxd.api.IUsbMuxd;
import com.jd.testdev.cyber.usbmuxd.api.exception.UsbMuxdException;
import com.jd.testdev.cyber.usbmuxd.api.model.Device;
import com.jd.testdev.cyber.usbmuxd.api.model.DeviceConnectionMessage;
import com.jd.testdev.cyber.usbmuxd.api.model.UsbMuxdConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Author Shawn Pan
 * Date  2024/12/5 17:10
 */
public abstract class UsbMuxdImpl implements IUsbMuxd {
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	protected DeviceListener deviceListener = new DeviceListener();
	protected DeviceConnecter deviceConnecter = new DeviceConnecter();
	private Map<Integer, Device> connectedDevices = Collections.synchronizedMap(new LinkedHashMap<>());
	protected boolean isStarted = false;
	private Socket connectionListeningSocket;
	private String registerUid;
	private UsbMuxdConnection usbMuxdConnection;
	private String serial;
	private int wdaPort;
	private static final Logger logger = LoggerFactory.getLogger(UsbMuxdImpl.class);


	protected abstract SocketAddress getAddress() throws IOException;
	protected abstract Socket getSocketImpl() throws IOException;

	public UsbMuxdImpl(String serial, int wdaPort){
		this.serial = serial;
		this.wdaPort = wdaPort;
	}

	public int getWdaPort(){
		return this.wdaPort;
	}

	@Override
	public Collection<Device> connectedDevices() {
		ArrayList<Device> copy = new ArrayList<>();
		copy.addAll(connectedDevices.values());
		return copy;
	}


//	public UsbMuxdConnection connectToFirstDevice(int port, long time, TimeUnit timeUnit) throws UsbMuxdException {
//		try {
//			if (isStarted && !connectedDevices.isEmpty()) {
//				return connectToDevice(port, connectedDevices.values().iterator().next());
//			}
//			CountDownLatch countDownLatch = new CountDownLatch(1);
//			Device[] singletonDevice = new Device[1];
//			String register = deviceListener.register(m -> {
//				singletonDevice[0] = m.device;
//				countDownLatch.countDown();
//			});
//			if (!isStarted) {
//				startListening();
//			}
//			if (time > 0) {
//				countDownLatch.await(time, timeUnit);
//			} else {
//				countDownLatch.await();
//			}
//			deviceListener.unregister(register);
//			return connectToDevice(port, singletonDevice[0]);
//		} catch (UsbMuxdConnectException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new UsbMuxdException(e);
//		}
//	}


//	public UsbMuxdConnection connectToFirstDevice(int port) throws UsbMuxdException {
//		return connectToFirstDevice(port, -1, null);
//	}


//	public UsbMuxdConnection connectToDevice(int port, Device device) throws UsbMuxdException {
//		return connectToDevice(port, device, -1, null);
//	}

	@Override
//	public UsbMuxdConnection connectToDevice(int port, Device device, long time, TimeUnit timeUnit) throws UsbMuxdException {
//		try {
//			Socket connectionSocket = getSocketImpl();
//			connectionSocket.connect(getAddress());
//			byte[] connectByteMessage = PlistMessageService.buildConnectMsg(device.deviceId, port);
//			InputStream inputStream = connectionSocket.getInputStream();
//			Future<ConnectedMessage> submit = executorService.submit(() -> deviceConnecter.getConnectionResult(inputStream));
//			connectionSocket.getOutputStream().write(connectByteMessage);
//			ConnectedMessage connectedMessage;
//			if (time > 0) {
//				connectedMessage = submit.get(time, timeUnit);
//			} else {
//				connectedMessage = submit.get();
//			}
//			if (connectedMessage.result != 0) {
//				connectedMessage.throwException();
//			}
//			UsbMuxdConnection usbMuxdConnection = new UsbMuxdConnection();
//			usbMuxdConnection.inputStream = inputStream;
//			usbMuxdConnection.outputStream = connectionSocket.getOutputStream();
//			usbMuxdConnection.device = device;
//			return usbMuxdConnection;
//		} catch (UsbMuxdConnectException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new UsbMuxdException(e);
//		}
//	}


	public void connectToDevice(String serial, int port) throws UsbMuxdException {
		Map<String,String> deviceList = listDevices();
		String deviceID = "";
		for(String key : deviceList.keySet()){
			if (key.equals(serial)){
				deviceID = deviceList.get(key);
				break;
			}
		}
		if (!deviceID.isEmpty()){
			try {
				Socket connectionSocket = getSocketImpl();
				connectionSocket.connect(getAddress());
				byte[] connectByteMessage = PlistMessageService.buildConnectMsg(Integer.valueOf(deviceID), port);
				InputStream inputStream = connectionSocket.getInputStream();
				Future<ConnectedMessage> submit = executorService.submit(() -> deviceConnecter.getConnectionResult(inputStream));
				connectionSocket.getOutputStream().write(connectByteMessage);
				ConnectedMessage connectedMessage = submit.get();
				if (connectedMessage.result != 0) {
					connectedMessage.throwException();
				}
				this.usbMuxdConnection = UsbMuxdConnection.sharedInstance();
				usbMuxdConnection.inputStream = inputStream;
				usbMuxdConnection.outputStream = connectionSocket.getOutputStream();
			} catch (Exception e) {
				throw new UsbMuxdException(e);
			}
		}else{
			throw new UsbMuxdException("未找到指定设备号");
		}
	}

	private Map<String,String> listDevices() throws UsbMuxdException {
		try {
			Socket connectionSocket = getSocketImpl();
			connectionSocket.connect(getAddress());
			byte[] connectByteMessage = PlistMessageService.buildListMsg();
			InputStream inputStream = connectionSocket.getInputStream();
			Future<Map<String, String>> submit = executorService.submit(() -> deviceConnecter.getListResult(inputStream));
			connectionSocket.getOutputStream().write(connectByteMessage);
			return submit.get();
		} catch (Exception e) {
			throw new UsbMuxdException(e);
		}
	}

	public String getWDAResult(String request) throws IOException, WDAException, UsbMuxdException {
		this.connectToDevice(this.serial, this.wdaPort);
		this.usbMuxdConnection.outputStream.write(request.getBytes());
		InputStream currentInputStream = this.usbMuxdConnection.inputStream;
		StringBuilder sb = new StringBuilder();
		sb.append(this.deviceConnecter.getWDAResult(currentInputStream,false, 0));
		while (this.usbMuxdConnection.inputStream.available() > 0 || !sb.toString().contains("sessionId")){
			sb.append(this.deviceConnecter.getWDAResult(this.usbMuxdConnection.inputStream,true, this.usbMuxdConnection.inputStream.available()));
		}
		return sb.toString();
	}

	@Override
	public void startListening() throws UsbMuxdException {
		try {
			connectionListeningSocket = getSocketImpl();
			connectionListeningSocket.connect(getAddress());
			byte[] connectByteMessage = PlistMessageService.buildListenConnectionMsg();
			InputStream inputStream = connectionListeningSocket.getInputStream();
			deviceListener.start(inputStream);
			isStarted = true;
			registerUid = deviceListener.register(m -> {
				switch (m.type) {
					case Add:
						connectedDevices.put(m.device.deviceId, m.device);
						break;
					case Remove:
						connectedDevices.remove(m.device.deviceId);
						break;
				}
			});
			connectionListeningSocket.getOutputStream().write(connectByteMessage);
			executorService.execute(deviceListener);
		} catch (Exception e) {
			throw new UsbMuxdException(e);
		}
	}

	@Override
	public void stopListening() throws UsbMuxdException {
		if (isStarted) {
			deviceListener.stop();
			deviceListener.unregister(registerUid);
			try {
				connectionListeningSocket.close();
			} catch (IOException e) {
				connectionListeningSocket = null;
			}
		}
		isStarted = false;
	}

	@Override
	public String registerDeviceConnectionListener(Consumer<DeviceConnectionMessage> consumer) {
		return deviceListener.register(consumer);
	}

	@Override
	public void unRegisterDeviceConnectionListener(String uid) {
		deviceListener.unregister(uid);
	}
}
