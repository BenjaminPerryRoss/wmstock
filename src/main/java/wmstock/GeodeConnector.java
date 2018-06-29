package wmstock;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeodeConnector {

    private ClientCache clientCache;
    private Region<Integer, Item> itemsRegion;
    private boolean isConnected = false;
    public final String itemsRegionName = "items";
    Execution execution;

    public GeodeConnector() throws IOException, InterruptedException {
        //pass
    }

    public boolean connect() throws IOException, InterruptedException
    {
        isConnected = initializeCluster();
        clientCache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
                .set("log-level", "WARN").create();
        createRegion(itemsRegionName);

        UpdateData myFun = new UpdateData();
        FunctionService.registerFunction(myFun);

        execution = FunctionService.onRegion(itemsRegion);

        return isConnected;
    }

    public GeodeConnector(boolean autoConnect) throws IOException, InterruptedException
    {
        if(autoConnect) {
            isConnected = initializeCluster();
            clientCache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
                    .set("log-level", "WARN").create();
            createRegion(itemsRegionName);
        }
    }

    private boolean initializeCluster()throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder("scripts/startGfsh.sh");
        Process p = processBuilder.start();

        p.waitFor(45, TimeUnit.SECONDS);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getErrorStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String stdError = builder.toString();

        if (stdError != null && !stdError.isEmpty()) {
            throw new IOException("Errors encountered initializing Gfsh cluster: " + stdError);
        }

        if (p.exitValue() != 0) {
            throw new IOException("Errors encountered initializing Gfsh cluster: startGfsh.sh exit value of " + p.exitValue());
        }

        return true;
    }


    private boolean createRegion(String newRegionName)
    {
        itemsRegion = clientCache.<Integer, Item>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                        .create(newRegionName);

        if(itemsRegion == null) { return false;}

        System.out.print("Region " + newRegionName + " Created.");
        return true;
    }

    public boolean isConnected() {return isConnected;}

    public Item getItem(int key)
    {
        return itemsRegion.get(key);
    }

    public void putItem(int key, Item value)
    {
        itemsRegion.put(key, value);
    }

    public void disconnect() throws IOException, InterruptedException {
        if(isConnected)
        {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/stopGfsh.sh");
            Process p = processBuilder.start();

            p.waitFor();

            clientCache.close();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String stdError = builder.toString();

            if (stdError != null && !stdError.isEmpty()) {
                throw new IOException("Errors encountered shutting down Gfsh cluster: " + stdError);
            }

            if (p.exitValue() != 0) {
                throw new IOException("Errors encountered shutting down Gfsh cluster: stopGfsh.sh exit value of " + p.exitValue());
            }

            isConnected = false;
        }
    }

    public ResultCollector refresh() {
        return execution.execute(UpdateData.ID);
    }
}
