package uniqueindex;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.ObjectLockingIndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import threaded.car.Car;

import java.util.Collections;

/**
 * Created by kimptonc on 08/06/2016.
 */
public class UniqueWithConcurrent {

    public static void main(String[] args) {
//        IndexedCollection<Car> ic = new ConcurrentIndexedCollection<>();
        IndexedCollection<Car> ic = new TransactionalIndexedCollection<Car>(Car.class);
//        IndexedCollection<Car> ic = new ObjectLockingIndexedCollection<>();

        ic.addIndex(UniqueIndex.onAttribute(Car.ID));

        Car c1a = new Car(Car.randomCarMap(1));
        ic.add(c1a);
        Car c1b = new Car(Car.randomCarMap(1));
        ic.update(Collections.singleton(c1a), Collections.singleton(c1b));
    }
}
