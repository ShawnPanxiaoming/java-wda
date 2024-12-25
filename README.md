# java-wda
iOS wda api based on Java

## Description
This repository is to integrate iOS ui automation testing framework called WebDriverAgent with Java language.It is easy to integrate with your Spring project
This repository helps to connect to your iphone with usbmuxd protocol and can call wda api without iproxy forward

### How to use
init the CyberWdaClient, where first param is your iphone's udid
```
CyberWdaClient client = new CyberWdaClient("cfbdab10ffaa92fac93394d03c23e9b25976f3c5");
```
if your wda server is listening on customized port,your can call it with your specific port
```
CyberWdaClient client = new CyberWdaClient("cfbdab10ffaa92fac93394d03c23e9b25976f3c5",8888);
```

Go to Home
```
client.home()
```

Get the source
```
client.getSource()
```
Click on coordinator
```
client.click(100,100)
```

Enjoy!

### About the result format
I just output the result with the original wda response, you should serialize yourself according to your actual testing demand.


## Thanks 
https://github.com/mogaleaf/java-usbmuxd

https://github.com/openatx/facebook-wda

