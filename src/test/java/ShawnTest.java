/**
 * Author Shawn Pan
 * Date  2024/12/5 18:53
 */
import com.jd.testdev.cyber.CyberWdaClient;
import com.jd.testdev.cyber.service.WDAException;
import com.jd.testdev.cyber.usbmuxd.api.exception.UsbMuxdException;
import com.jd.testdev.cyber.utils.CyberWdaUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShawnTest {
    private static final Logger logger = LoggerFactory.getLogger(ShawnTest.class);
    @Test
    public void Mytest() throws UsbMuxdException, IOException, WDAException {
        CyberWdaClient client = new CyberWdaClient("00008140-001915E22E33001C");
        logger.debug(client.getSource());
//        Map<String,Object> env = new HashMap<>();
//        env.put("url","https://storage.jd.com/shtestdev.com/cyber_hybrid/hybrid_screenshot/6HJDU19B08011106_1671469462697.jpg");
//        client.startApp("com.jd.testdev.cyber.JD.Wrapper",env);
        //client.clickOnElement("5C000000-0000-0000-3D00-000000000000");

    }


}
