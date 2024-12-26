package com.shawn.cyber.service;

import com.shawn.cyber.utils.CyberWdaUtils;
import com.shawn.cyber.utils.JsonMapper;
import com.shawn.cyber.usbmuxd.api.IUsbMuxd;
import com.shawn.cyber.usbmuxd.api.UsbMuxdFactory;
import com.shawn.cyber.usbmuxd.api.exception.UsbMuxdException;
import com.shawn.cyber.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author Shawn Pan
 * Date  2024/12/5 17:49
 */

public class CyberWdaService {
    private static final Logger logger = LoggerFactory.getLogger(CyberWdaService.class);
    private IUsbMuxd usbMuxdDriver;
    private WDARequestBuilder wdaRequestBuilder;
    private String ip;
    private String osVersion;
    private int width;
    private int height;

    public CyberWdaService(String serialNo, int wdaPort) throws UsbMuxdException, IOException, WDAException {
        this.usbMuxdDriver = UsbMuxdFactory.getInstance(serialNo, wdaPort);
        this.wdaRequestBuilder = new WDARequestBuilder(wdaPort);
        this.getSession();
        StatusResponse statusResponse = JsonMapper.sharedInstance().makeDeserialize(getStatus(), StatusResponse.class);
        this.ip = statusResponse.getValue().getIos().getIp();
        this.osVersion = statusResponse.getValue().getOs().getVersion();
        this.wdaRequestBuilder.setSessionId(statusResponse.getSessionId());
        WindowSizeResponse windowSizeResponse = JsonMapper.sharedInstance().makeDeserialize(getWindowSize(), WindowSizeResponse.class);
        this.width = windowSizeResponse.getValue().getWidth();
        this.height = windowSizeResponse.getValue().getHeight();
        logger.debug("width:"+width+"    height: "+height);
    }

    public String getStatus() throws IOException, WDAException, UsbMuxdException {
        String request = wdaRequestBuilder.getRequest("/status");
        return usbMuxdDriver.getWDAResult(request);
    }

    public String getIP(){
        return this.ip;
    }

    public String getOSVersion(){
        return this.osVersion;
    }

    public String getSource() throws IOException, WDAException, UsbMuxdException {
        String request = wdaRequestBuilder.getRequest("/source");
        return usbMuxdDriver.getWDAResult(request);
    }

    private void getSession() throws IOException, WDAException, UsbMuxdException {
        String request = wdaRequestBuilder.postRequest("/session",new String(JsonMapper.sharedInstance().makeSerialize(new SessionBody())));
        usbMuxdDriver.getWDAResult(request);
    }

    public void dragFromToForDuration(int start_x, int start_y, int end_x, int end_y, int duration) throws IOException, UsbMuxdException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/dragfromtoforduration",new String(JsonMapper.sharedInstance().makeSerialize(new IOSSwipeRequest(start_x,start_y,end_x,end_y,duration))));
        usbMuxdDriver.getWDAResult(request);
    }

    public void tap(int x, int y) throws IOException, UsbMuxdException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/tap/0",new String(JsonMapper.sharedInstance().makeSerialize(new TapRequest(x, y))));
        usbMuxdDriver.getWDAResult(request);
    }

    public void keys(String text) throws IOException, UsbMuxdException, WDAException {
        List<String> list = new ArrayList<>();
        String[] charList = text.split("");
        for(String c: charList){
            c = CyberWdaUtils.toUnicode(c);
            list.add(c);
        }
        String body = new String(JsonMapper.sharedInstance().makeSerialize(new SendKeysRequest(list)));
        body = body.replaceAll("\\\\u","\\u");
        String request = wdaRequestBuilder.postRequestWithSession("/wda/keys",body);
        usbMuxdDriver.getWDAResult(request);
    }

    public void openApp(String url) throws IOException, UsbMuxdException, WDAException {
        if (url.contains("=")) {
            String[] array = url.split("=", 2);
            String encoded = URLEncoder.encode(array[1], "utf-8");
            url = array[0] + "=" + encoded;
            url = url.replaceAll("%3A\\+", ":");
            String request = wdaRequestBuilder.postRequestWithSession("/wda/app/openApp", new String(JsonMapper.sharedInstance().makeSerialize(new OpenAppRequest(url))));
            usbMuxdDriver.getWDAResult(request);
        }
    }

    public void homeScreen() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.postRequest("/wda/homescreen","{}");
        usbMuxdDriver.getWDAResult(request);
    }

    public void terminate(String bundleId) throws IOException, UsbMuxdException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/apps/terminate",new String(JsonMapper.sharedInstance().makeSerialize(new KillAppRequest(bundleId))));
        usbMuxdDriver.getWDAResult(request);
    }

    public String getPasteboard() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/getPasteboard","{}");
        String result = usbMuxdDriver.getWDAResult(request);
        logger.debug(result);
        PasteboardResponse pasteboardResponse = JsonMapper.sharedInstance().makeDeserialize(result, PasteboardResponse.class);
        return CyberWdaUtils.Base64Decoder(pasteboardResponse.getValue());
    }

    public void launch(String bundleId, Map<String,Object> env) throws IOException, UsbMuxdException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/apps/launch",new String(JsonMapper.sharedInstance().makeSerialize(new IOSLaunchAppRequest(bundleId,env))));
        usbMuxdDriver.getWDAResult(request);
    }

    public void touchAndHold(int x, int y) throws IOException, UsbMuxdException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/touchAndHold",new String(JsonMapper.sharedInstance().makeSerialize(new LongTapRequest(x, y))));
        usbMuxdDriver.getWDAResult(request);
    }

    public void touchAndHold(String elementId) throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/element/"+elementId+"/touchAndHold","{\"duration\":3.0}");
        usbMuxdDriver.getWDAResult(request);
    }

    public void elementClick(String elementId) throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.postRequestWithSession("/wda/element/"+elementId+"/tap","{}");
        usbMuxdDriver.getWDAResult(request);
    }

    public String findElementByXpath(String xpath) throws IOException, UsbMuxdException, WDAException {
        String unicodeXpath = CyberWdaUtils.toUnicode(xpath);
        String body =  new String(JsonMapper.sharedInstance().makeSerialize(new FindElementRequest("xpath",unicodeXpath)));
        body = body.replaceAll("\\\\u","\\u");
        logger.debug(body);
        String request = wdaRequestBuilder.postRequestWithSession("/element",body);
        return usbMuxdDriver.getWDAResult(request);
    }

    public String findElementByClassChain(String attribute, String value) throws IOException, UsbMuxdException, WDAException {
        String unicodeValue = CyberWdaUtils.toUnicode(value);
        String xpath = "**/XCUIElementTypeAny[`"+attribute+" == '"+unicodeValue+"'`]";
        String body =  new String(JsonMapper.sharedInstance().makeSerialize(new FindElementRequest("class chain",xpath)));
        body = body.replaceAll("\\\\u","\\u");
        logger.debug(body);
        String request = wdaRequestBuilder.postRequestWithSession("/element",body);
        return usbMuxdDriver.getWDAResult(request);
    }

    public String getElementRect(String elementId) throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.getRequestWithSession("/element/"+elementId+"/rect");
        return usbMuxdDriver.getWDAResult(request);
    }

    public String getWindowSize() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.getRequest("/window/size");
        return usbMuxdDriver.getWDAResult(request);
    }

    public void swiftUp() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(this.width/2, this.height/2, this.width/2, 1,0);
    }

    public void swiftDown() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(this.width/2, this.height/2, this.width/2, this.height-1,0);
    }

    public void swiftLeft() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(this.width, this.height/2, 1, this.height/2,0);
    }

    public void swiftRight() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(1, this.height/2, this.width, this.height/2,0);
    }

    public void swiftUpForLongDistance() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(this.width/2, this.height-200, this.width/2, 1,0);
    }

    public void swiftDownForLongDistance() throws UsbMuxdException, IOException, WDAException {
        this.dragFromToForDuration(this.width/2, 200, this.width/2, this.height-1,0);
    }

    public void swiftLeftAtPoint(int x, int y) throws UsbMuxdException, IOException, WDAException {
        int offset = x - 300;
        if (offset < 0){
            offset = 0;
        }
        this.dragFromToForDuration(x, y, offset, y,0);
    }

    public void swiftRightAtPoint(int x, int y) throws UsbMuxdException, IOException, WDAException {
        int offset = x + 300;
        this.dragFromToForDuration(x, y, offset, y,0);
    }

    public void triggerSiriByText(String text) throws IOException, UsbMuxdException, WDAException {
        String unicodeText = CyberWdaUtils.toUnicode(text);
        String body = new String(JsonMapper.sharedInstance().makeSerialize(new SiriRequest(unicodeText)));
        body = body.replaceAll("\\\\u","\\u");
        String request = wdaRequestBuilder.postRequestWithSession("/wda/siri/activate", body);
        usbMuxdDriver.getWDAResult(request);
    }

    public String screenshot() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.getRequest("/screenshot");
        return usbMuxdDriver.getWDAResult(request);
    }

    public String deviceInfo() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.getRequest("/wda/device/info");
        return usbMuxdDriver.getWDAResult(request);
    }

    public String currentAppInfo() throws UsbMuxdException, IOException, WDAException {
        String request = wdaRequestBuilder.getRequest("/wda/activeAppInfo");
        return usbMuxdDriver.getWDAResult(request);
    }
}