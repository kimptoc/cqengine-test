package threaded.imdb2;

import threaded.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kimptonc on 08/06/2016.
 */
public class TestIMDB {
    public static void main(String[] args) {
        InMemoryDatabaseCQEngine db = new InMemoryDatabaseCQEngine();

        Map m = new HashMap<>();
        m.put(Constants.KEY_FIELD, Constants.ID);

        db.initTable("cars", Constants.KEY_FIELD);

        db.put("cars", Constants.ID, m);

        new Thread(new IMDBUpdater(db)).start();

        ExecutorService pool = Executors.newFixedThreadPool(15);

        Future<Boolean> result1 = pool.submit(new IMDBConsumer(db));
        Future<Boolean> result2 = pool.submit(new IMDBConsumer(db));
        Future<Boolean> result3 = pool.submit(new IMDBConsumer(db));
        Future<Boolean> result4 = pool.submit(new IMDBConsumer(db));

//        TestUpdateBaseBase.endOfTest(result1, result2, result3);

    }
}
