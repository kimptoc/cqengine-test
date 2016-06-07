import com.googlecode.cqengine.IndexedCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateCarBase extends TestUpdateBaseBase {
    protected static void runTheTests(IndexedCollection<Car> ic) {

        Car c = new Car(Car.randomCarMap(Constants.ID_CAR));

        ic.add(c);

        new Thread(new CarUpdater(ic)).start();

        ExecutorService pool = Executors.newFixedThreadPool(30);

        Future<Boolean> result1 = pool.submit(new CarConsumer(ic));
        Future<Boolean> result2 = pool.submit(new CarConsumer(ic));
        Future<Boolean> result3 = pool.submit(new CarConsumer(ic));
        Future<Boolean> result4 = pool.submit(new CarConsumer(ic));
        Future<Boolean> result5 = pool.submit(new CarConsumer(ic));
        Future<Boolean> result6 = pool.submit(new CarConsumer(ic));

        endOfTest(result1, result2, result3);
    }

}
