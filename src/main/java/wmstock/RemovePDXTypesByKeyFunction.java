package wmstock;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.internal.cache.GemFireCacheImpl;
import org.apache.geode.pdx.internal.PdxType;
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
//            Collection<PdxType> pdxTypeIdsToRemove = (Collection)arguments[1];
            Cache cache = CacheFactory.getAnyInstance();
            TypeRegistry registry = ((GemFireCacheImpl)cache).getPdxRegistry();
            for (Integer typeId : pdxTypeIdsToRemove) {
                    registry.removeType(typeId);
                    stringBuilder.append("Removed type with ID " + typeId + "on member " + context.getMemberName()+ "\n");
            }
        } catch (Exception ex) {
            Object[] arguments = (Object[])((Object[])context.getArguments());
//            Collection<PdxType> types = (Collection)arguments[1];
            Collection<Integer> typeIds = (Collection)arguments[1];
            StackTraceElement elements[] = ex.getStackTrace();
            stringBuilder.append("Caught Exception " + ex + " while trying to remove PDXType "
                    + typeIds.toArray()[0] + " on Member " + context.getMemberName() + "\n");
            for (StackTraceElement element : elements) {
                stringBuilder.append(element.toString() + "\n");
            }
//            context.getResultSender().lastResult("Caught Exception " + ex + " while trying to remove PDXType "
//                    + ((PdxType)types.toArray()[0]).getClassName() + " on Member " + context.getMemberName());
        }
        stringBuilder.append("Finished removing types on member "+ context.getMemberName());
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
