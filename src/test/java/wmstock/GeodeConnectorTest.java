package wmstock;

import org.apache.geode.cache.execute.ResultCollector;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class GeodeConnectorTest {

    private static GeodeConnector connector;
    private WMLogger logger = new WMLogger();
    @BeforeClass
    public static void before () {
        try {
            connector = new GeodeConnector();
            connector.connect();
        } catch (Exception e) {
            fail("Exception thrown during Connector initialization: " + e.getMessage());
        }
    }

    @AfterClass
    public static void after() {
        try {
            connector.disconnect();
        } catch (Exception e) {
            fail("Exception thrown during Connector shutdown: " + e.getMessage());
        }
    }

    @Test
    public void initConnector () throws IOException, InterruptedException {
        assertThat(connector.isConnected()).isEqualTo(true);

        connector.disconnect();

        assertThat(connector.isConnected()).isEqualTo(false);

        connector.connect();

        assertThat(connector.isConnected()).isEqualTo(true);
    }

    @Test
    public void putToAndGetFromItems() {
        Item item = new Item( "MyVal", 14);
        connector.putItem(4, item);

        Item myVal = connector.getItem(4);

        assertThat(myVal.equals(item));

        Item wrongVal = connector.getItem(5);

        assertThat(wrongVal == null);
    }

    @Test
    public void refreshTest() {
        ResultCollector rc = connector.refresh();
        assertThat(((ArrayList)rc.getResult()).get(0)).isEqualTo("Success!");

        Item myVal = connector.getItem(0);
        Item expectedVal = new Item("Item0", 0);

        assertThat(myVal.equals(expectedVal));
    }

}
