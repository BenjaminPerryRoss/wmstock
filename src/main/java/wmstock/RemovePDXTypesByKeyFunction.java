package wmstock;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.internal.cache.GemFireCacheImpl;
import org.apache.geode.pdx.internal.TypeRegistry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RemovePDXTypesByKeyFunction implements Function {
    public RemovePDXTypesByKeyFunction() {

    }

    public boolean hasResult() {
        return true;
    }

    public void execute(FunctionContext context) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Object[] arguments = (Object[])((Object[])context.getArguments());
            Collection<Integer> pdxTypeIdsToRemove = (Collection)arguments[1];
            Cache cache = CacheFactory.getAnyInstance();
            TypeRegistry registry = ((GemFireCacheImpl)cache).getPdxRegistry();
            for (int id : pdxTypeIdsToRemove) {
                registry.removeType(id);
                stringBuilder.append("Removed PDXType with ID: " + id + "\n");
            }
        } catch (Exception ex) {
            context.getResultSender().sendException(ex);
        }
        stringBuilder.append("Finished removing types");
        context.getResultSender().lastResult(stringBuilder.toString());
    }

    public String getId() {
        return "RemovePDXTypesByKeyFunction";
    }

    public boolean optimizeForWrite() {
        return false;
    }

    public boolean isHA() {
        return true;
    }


}
