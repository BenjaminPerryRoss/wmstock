package wmstock;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UnusedPdxTypeFunction implements Function {
  public UnusedPdxTypeFunction() {
  }

  public boolean hasResult() {
    return true;
  }

  public void execute(FunctionContext context) {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      Object[] arguments = (Object[])((Object[])context.getArguments());
      Collection<Integer> pdxTypeIdsToKeep = (Collection)arguments[1];
      Cache cache = CacheFactory.getAnyInstance();
      Region<Object, Object> pdxTypesRegion = cache.getRegion("/PdxTypes");
      stringBuilder.append("PdxTypes size before purge = " + pdxTypesRegion.size() + "\n");
      Set<Object> pdxTypeKeysToRemove = new HashSet(pdxTypesRegion.keySet());
      pdxTypeKeysToRemove.removeAll(pdxTypeIdsToKeep);
      stringBuilder.append("Amount of PdxTypes to remove = " + pdxTypeKeysToRemove.size() + "\n");
    } catch (Exception var8) {
      context.getResultSender().sendException(var8);
    }

    context.getResultSender().lastResult(stringBuilder.toString());
  }

  public String getId() {
    return "UnusedPdxTypeFunction";
  }

  public boolean optimizeForWrite() {
    return false;
  }

  public boolean isHA() {
    return true;
  }
}
