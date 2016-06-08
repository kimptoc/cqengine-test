package threaded.imdb;

import com.sun.jndi.cosnaming.CNCtx;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import threaded.Constants;
import threaded.TestUpdateBaseBase;
import threaded.map.Consumer;
import threaded.map.Updater;

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
        m.put("Size", 1);
        m.put("Distance", 2);
        m.put("Height", 3);
        m.put("Width", 4);
        m.put("Version", Constants.VERSION_GENERATOR.incrementAndGet());

        db.initTable("cars", Constants.KEY_FIELD);

        db.put("cars", Constants.ID, m);

        new Thread(new IMDBUpdater(db)).start();

        ExecutorService pool = Executors.newFixedThreadPool(1);

        Future<Boolean> result1 = pool.submit(new IMDBConsumer(db));
//        Future<Boolean> result2 = pool.submit(new IMDBConsumer(db));
//        Future<Boolean> result3 = pool.submit(new IMDBConsumer(db));

//        TestUpdateBaseBase.endOfTest(result1, result2, result3);

    }
}
