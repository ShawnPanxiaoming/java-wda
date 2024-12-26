/**
 * Author Shawn Pan
 * Date  2024/12/5 18:53
 */
import com.shawn.cyber.CyberWdaClient;
import com.shawn.cyber.service.WDAException;
import com.shawn.cyber.usbmuxd.api.exception.UsbMuxdException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShawnTest {
    private static final Logger logger = LoggerFactory.getLogger(ShawnTest.class);
    @Test
    public void Mytest() throws UsbMuxdException, IOException, WDAException {
        CyberWdaClient client = new CyberWdaClient("00008140-001915E22E33001C");
        logger.debug(client.getSource());

    }


}
