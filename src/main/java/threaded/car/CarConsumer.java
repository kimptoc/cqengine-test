package threaded.car;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.resultset.ResultSet;
import threaded.Constants;
import threaded.ConsumerBase;
import threaded.car.Car;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static java.lang.Thread.sleep;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class CarConsumer extends ConsumerBase {

    CarConsumer(IndexedCollection ic)
    {
        super(ic);
    }

    public ResultSet<Car> doQuery() {
        return coll.retrieve(equal(Car.ID, Constants.ID_CAR));
    }
}
