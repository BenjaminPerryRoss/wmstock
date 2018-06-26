package wmstock;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;


import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class wmstock {

    public static void main(String args[]) {

        Scanner scanner = new Scanner(System. in);

        String option = "";
        while(option != "exit")
        {
            option = scanner.nextLine();

            switch (option) {
                case "refresh":
                    break;
                case "search":

                    break;
                case "exit":
                    System.out.print("Bye");
                    break;
                default:
                    System.out.println("Please enter a valid command.");
                    break;
            }
        }
    }
}