package threaded.map;

import com.googlecode.cqengine.IndexedCollection;
import threaded.Constants;
import threaded.TestUpdateBaseBase;
import threaded.map.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateBase extends TestUpdateBaseBase {
    protected static void runTheTests(IndexedCollection<Map> ic) {
        Map m = new HashMap<>();
        m.put(Constants.KEY_FIELD, Constants.ID);
        m.put("Size", 1);
        m.put("Distance", 2);
        m.put("Height", 3);
        m.put("Width", 4);
        m.put("Version", Constants.VERSION_GENERATOR.incrementAndGet());

        ic.add(m);

        new Thread(new Updater(ic)).start();

        ExecutorService pool = Executors.newFixedThreadPool(5);

        Future<Boolean> result1 = pool.submit(new Consumer(ic));
        Future<Boolean> result2 = pool.submit(new Consumer(ic));
        Future<Boolean> result3 = pool.submit(new Consumer(ic));

        TestUpdateBaseBase.endOfTest(result1, result2, result3);
    }
}
