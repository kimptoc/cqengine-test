import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class CarUpdater implements Runnable {

    private final IndexedCollection<Car> coll;

    CarUpdater(IndexedCollection<Car> ic)
    {
        coll = ic;
    }

    @Override
    public void run() {
        System.out.println("Updates starting...");
        for (int i=0; i< 10000; i++)
        {
            Car c = new Car(Car.randomCarMap(Constants.ID_CAR));

            ResultSet<Car> existingItems = coll.retrieve(equal(Car.ID, Constants.ID_CAR));
            coll.update(
                    Collections.singleton(existingItems.uniqueResult()),
                    Collections.singleton(c));
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
//            if (i%100 == 0) System.out.print(".");
        }
        System.out.println("");
        System.out.println("Updates are now done!");
    }
}
