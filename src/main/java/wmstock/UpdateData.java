package wmstock;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ServerOperationException;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Map;

public class UpdateData implements Function {

    public static final String ID = UpdateData.class.getSimpleName();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(FunctionContext context) {
        context.getResultSender().lastResult(true);
        Cache serverCache = ((RegionFunctionContext) context).getCache();
        Region<Integer, Item> itemsRegion = ((RegionFunctionContext) context).getDataSet();

        if(itemsRegion == null) {
            context.getResultSender().lastResult(new ServerOperationException("No /items region defined"));
        }
        itemsRegion.clear();

        Map<Integer, Item> resultMap = getItems(serverCache, context);

        itemsRegion.putAll(resultMap);

        context.getResultSender().lastResult(true);
    }

    private Map<Integer, Item> getItems(Cache serverCache, FunctionContext context) {
        Region<Integer, Item> dataRegion = serverCache.getRegion("/mockedData");

        if(dataRegion == null) {
            context.getResultSender().lastResult(new ServerOperationException("No /mockedData region defined"));
        }

        Map<Integer, Item> resultSet;
        try {
            resultSet = dataRegion.getAll(dataRegion.keySet());
        } catch (Exception e) {
            context.getResultSender().lastResult(new ServerOperationException("Exception caught in data query"));
            throw new ServerOperationException("Exception caught in data query");
        }

        return resultSet;
    }
}
