package com.jd.testdev.cyber;

import com.jd.testdev.cyber.service.CyberWdaService;
import com.jd.testdev.cyber.service.WDAException;
import com.jd.testdev.cyber.usbmuxd.api.exception.UsbMuxdException;
import java.io.IOException;
import java.util.Map;

/**
 * Author Shawn Pan
 * Date  2024/12/5 17:28
 */
public class CyberWdaClient {
    private String serialNo;
    private int wdaPort = 8100;
    private CyberWdaService cyberWdaService;

    public CyberWdaClient(String serialNo, int wdaPort) throws UsbMuxdException, IOException, WDAException {
        this.serialNo = serialNo;
        this.wdaPort = wdaPort;
        this.cyberWdaService = new CyberWdaService(this.serialNo, this.wdaPort);
    }

    public CyberWdaClient(String serialNo) throws UsbMuxdException, IOException, WDAException {
        this.serialNo = serialNo;
        this.cyberWdaService = new CyberWdaService(this.serialNo, this.wdaPort);
    }

    public String getStatus() throws IOException, WDAException, UsbMuxdException {
        return this.cyberWdaService.getStatus();
    }

    public String getIP(){
        return this.cyberWdaService.getIP();
    }

    public String getOSVersion(){
        return this.cyberWdaService.getOSVersion();
    }

    public String getSource() throws IOException, WDAException, UsbMuxdException {
        return this.cyberWdaService.getSource();
    }

    public void drag(int start_x, int start_y, int end_x, int end_y, int duration) throws IOException, WDAException, UsbMuxdException {
        this.cyberWdaService.dragFromToForDuration(start_x,start_y,end_x,end_y,duration);
    }

    public void click(int x, int y) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.tap(x, y);
    }

    public void sendKeys(String text) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.keys(text);
    }

    public void openApp(String url) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.openApp(url);
    }

    public void home() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.homeScreen();
    }

    public void terminateApp(String bundleId) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.terminate(bundleId);
    }

    public String getPasteboard() throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.getPasteboard();
    }

    public void startApp(String bundleId, Map<String,Object> env) throws IOException, UsbMuxdException, WDAException {
        this.cyberWdaService.launch(bundleId,env);
    }

    public void longClick(int x, int y) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.touchAndHold(x, y);
    }

    public String findElementByXpath(String xpath) throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.findElementByXpath(xpath);
    }

    public String findElementByClassChain(String attribute, String value) throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.findElementByClassChain(attribute,value);
    }

    public String getRectByElementId(String elementId) throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.getElementRect(elementId);
    }

    public String getWindowSize() throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.getWindowSize();
    }

    public void swipeUp() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftUp();
    }

    public void swipeDown() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftDown();
    }

    public void swipeLeft() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftLeft();
    }

    public void swipeRight() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftRight();
    }

    public void swipeUpForLongDistance() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftUpForLongDistance();
    }

    public void swipeDownForLongDistance() throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftDownForLongDistance();
    }

    public void swipeLeftAtPoint(int x, int y) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftLeftAtPoint(x, y);
    }

    public void swipeRightAtPoint(int x, int y) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.swiftRightAtPoint(x, y);
    }

    public void launchSiriByText(String text) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.triggerSiriByText(text);
    }

    public String screenshot() throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.screenshot();
    }

    public String getDeviceInfo() throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.deviceInfo();
    }

    public String getCurrentAppInfo() throws UsbMuxdException, IOException, WDAException {
        return this.cyberWdaService.currentAppInfo();
    }

    public void longClickOnElement(String elementId) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.touchAndHold(elementId);
    }

    public void clickOnElement(String elementId) throws UsbMuxdException, IOException, WDAException {
        this.cyberWdaService.elementClick(elementId);
    }

}
