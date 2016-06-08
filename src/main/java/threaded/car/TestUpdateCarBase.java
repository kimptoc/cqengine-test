package threaded.car;

import com.googlecode.cqengine.IndexedCollection;
import threaded.Constants;
import threaded.TestUpdateBaseBase;
import threaded.car.Car;
import threaded.car.CarConsumer;
import threaded.car.CarUpdater;

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
