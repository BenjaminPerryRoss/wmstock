package wmstock;

import org.apache.geode.cache.execute.ResultCollector;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class GeodeConnectorTest {

    private static GeodeConnector connector;

    @Test
    public void initConnector () throws IOException, InterruptedException {
        try {
            connector = new GeodeConnector();
            connector.connect();

        } catch (Exception e) {
            fail("Exception thrown during Connector initialization: " + e.getMessage());
        }
        Item item;
        for (int i = 0; i < 2; ++i){
            if (i == 0) {
                item = new ItemV1("string", 1, false);
            }
            else {
                item = new ItemV2("string", 2, 42);
            }
            connector.putItem(i, item);
            connector.removeItem(i);
        }

        Item item3 = new ItemV3("string", 3, "string-two");
        connector.putItem(2,item3);

        System.out.println(((ItemV3) connector.getItem(2)).string);

        try {
            connector.disconnect();
        } catch (Exception e) {
            fail("Exception thrown during Connector shutdown: " + e.getMessage());
        }
    }
}
