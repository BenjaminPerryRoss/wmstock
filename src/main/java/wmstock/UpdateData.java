package wmstock;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ServerOperationException;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.HashMap;
import java.util.Map;

public class UpdateData implements Function {

    public static final String ID = UpdateData.class.getSimpleName();
    public WMLogger logger = new WMLogger();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(FunctionContext context) {
        //context.getResultSender().lastResult(true);
        Cache serverCache = ((RegionFunctionContext) context).getCache();
        Region<Integer, Item> itemsRegion = ((RegionFunctionContext) context).getDataSet();

        if(itemsRegion == null) {
            context.getResultSender().lastResult(new ServerOperationException("Items Region not Found"));
        }
        itemsRegion.clear();

        Map<Integer, Item> resultMap = getItems(serverCache, context);

        itemsRegion.putAll(resultMap);

        context.getResultSender().lastResult("Success!");
    }

    private Map<Integer, Item> getItems(Cache serverCache, FunctionContext context) {


        Map<Integer, Item> resultSet = new HashMap<Integer, Item>();

        for(int i = 0; i < 10; i++)
        {
            Item value = new Item("Item" + i, i);
            resultSet.put(i, value);
        }

        return resultSet;
    }
}
