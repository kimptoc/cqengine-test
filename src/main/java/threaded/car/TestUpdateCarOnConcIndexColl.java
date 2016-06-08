package threaded.car;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import threaded.car.Car;
import threaded.car.TestUpdateCarBase;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateCarOnConcIndexColl extends TestUpdateCarBase {

    public static void main(String[] args) {
        IndexedCollection<Car> ic = new ConcurrentIndexedCollection<>();
        runTheTests(ic);
    }

}
