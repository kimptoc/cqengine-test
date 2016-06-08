package threaded.imdb;

import threaded.Constants;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kimptonc on 08/06/2016.
 */
public class IMDBUpdater implements Runnable {
    private final InMemoryDatabaseCQEngine imdb;

    public IMDBUpdater(InMemoryDatabaseCQEngine db) {
        imdb = db;
    }

    @Override
    public void run() {
        System.out.println("Updates starting...");
        for (int i=0; i< 10000; i++)
        {
            Map m = new HashMap<>();
            m.put(Constants.KEY_FIELD,Constants.ID);

            imdb.put("cars",Constants.ID, m);

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Updates are now done!");

    }
}
