package com.jd.testdev.cyber.usbmuxd.protocol;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.jd.testdev.cyber.service.WDAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DeviceConnecter {
	private static final Logger logger = LoggerFactory.getLogger(DeviceConnecter.class);

	public ConnectedMessage getConnectionResult(InputStream inputStream) {
		try {
			int size = PlistMessageService.getSize(inputStream);
			if (size > 0) {
				NSDictionary dico = PlistMessageService.getNsDictionary(inputStream, size);
				PlistMessageService.ResultType messageTypeEnum = PlistMessageService.retrieveMsgType(dico);
				switch (messageTypeEnum) {
					case Result:
						NSNumber statusS = (NSNumber) dico.get("Number");
						int status = statusS.intValue();
						ConnectedMessage connectedMessage = new ConnectedMessage();
						connectedMessage.result = status;
						return connectedMessage;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getConnectionResult(inputStream);
	}


	public Map<String,String> getListResult(InputStream inputStream) {
		Map<String,String> deviceList = new HashMap<>();
		try {
			int size = PlistMessageService.getSize(inputStream);
			if (size > 0) {
				NSDictionary dico = PlistMessageService.getNsDictionary(inputStream, size);
				NSArray array = (NSArray) dico.get("DeviceList");
				size = array.count();
				if (size > 0){
					for(int i=0;i<size;i++){
						NSDictionary item = (NSDictionary) array.objectAtIndex(i);
						PlistMessageService.ResultType messageTypeEnum = PlistMessageService.retrieveMsgType(item);
						if (messageTypeEnum == PlistMessageService.ResultType.Attached){
							NSDictionary innerDic = (NSDictionary) item.get("Properties");
							deviceList.put(innerDic.get("SerialNumber").toString(),innerDic.get("DeviceID").toString());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceList;
	}

	public String getWDAResult(InputStream inputStream, boolean anymore, int continue_size) throws IOException, WDAException {
		if (anymore){
			byte[] bb = new byte[continue_size];
			inputStream.read(bb);
			return new String(bb).trim().replaceAll("\r", "").replaceAll("\n", "");
		}else{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line= br.readLine();
			if (line.startsWith("HTTP/1.1")){
				if (!line.contains("200 OK")){
					throw new WDAException("接口请求失败");
				}
			}else{
				throw new WDAException("wda数据返回异常");
			}
			int size = 0;
			while ((line = br.readLine()) != null){
				if (line.startsWith("Content-Length")){
					size = Integer.valueOf(line.split(":")[1].trim())+2;
				}
				if (line.startsWith("Connection")){
					break;
				}
			}
			char[] char_arr = new char[size];
			br.read(char_arr);
			String result = String.valueOf(char_arr).trim().replaceAll("\r", "").replaceAll("\n", "");
			if (result.isEmpty()) {
				//logger.info("翻页了，重新解析");
				byte[] bb = new byte[size - 2];
				inputStream.read(bb);
				result = new String(bb);
			}
			return result.trim().replaceAll("\r", "").replaceAll("\n", "");
		}

	}
}
