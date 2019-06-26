package wmstock;

import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.pdx.internal.PdxType;

import org.junit.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class GeodeConnectorTest {

    private static GeodeConnector connector;
    private static final int ENTRY_COUNT = 1000000;
    private static final int PDX_TYPE_COUNT = 50;

    @Test
    public void pdxTest()
        throws NoSuchFieldException, IllegalAccessException {
        try {
            connector = new GeodeConnector();
            connector.connect();

        } catch (Exception e) {
            fail("Exception thrown during Connector initialization: " + e.getMessage());
        }

        for (int i = 0; i < PDX_TYPE_COUNT - 1; ++i){
            connector.createPdxType("ItemV"+i);
        }

        for(int i = 0; i < ENTRY_COUNT; i++) {
            Item item3 = new ItemV3("Entry" + i, i, "string" + i);
            connector.putItem(i, item3);
            if ((i % 100000 == 0) && i != 0) {
                System.out.println("Finished putting " + i + " entries");
            }
        }

        List<PdxType> pdxTypes = connector.getLocalPdxTypes();

        //assertThat(((ItemV3)connector.getItem(2)).string).isEqualTo("string-two");

        for (PdxType type : pdxTypes) {
            System.out.println(type.getClassName());
        }

        connector.restartClient();

        Long startTime = System.currentTimeMillis();

        connector.checkUnusedTypes();

        List<Integer> pdxTypeIds = connector.getLocalPdxTypeIds();
        Pool pool = (Pool) PoolManager.getAll().values().toArray()[0];
        List<String> completedFunctionResult = (List) FunctionService
            .onServer(pool).withArgs(new Object[]{true, pdxTypeIds}).execute("UnusedPdxTypeFunction").getResult();

        System.out.println("Result = " + completedFunctionResult.get(0));

        Long endTime = System.currentTimeMillis();

        System.out.println("Process of identifying unused pdxTypes took " + (endTime - startTime)/1000 + " seconds for " + ENTRY_COUNT + " entries and " + PDX_TYPE_COUNT + " pdx types.");

        try {
            connector.disconnect();
        } catch (Exception e) {
            fail("Exception thrown during Connector shutdown: " + e.getMessage());
        }
    }
}
