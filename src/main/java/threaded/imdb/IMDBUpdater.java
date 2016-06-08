package threaded.imdb;

import com.googlecode.cqengine.resultset.ResultSet;
import threaded.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.equal;

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
            m.put("Size", 1+i);
            m.put("Distance", 2+i);
            m.put("Height", 3+i);
            m.put("Width", 4+i);
            m.put("Version", Constants.VERSION_GENERATOR.incrementAndGet());

            imdb.put("cars",Constants.ID, m);

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Updates are now done!");

    }
}
