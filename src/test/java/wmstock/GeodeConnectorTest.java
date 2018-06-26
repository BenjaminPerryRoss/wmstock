package wmstock;

import org.junit.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class GeodeConnectorTest {

    private static GeodeConnector connector;
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

        assertThat(myVal).isEqualTo(item);

        Item wrongVal = connector.getItem(5);

        assertThat(wrongVal).isNotEqualTo(item);
    }

    @Test
    public void refreshTest() {
        connector.refresh();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
