import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Map;
import java.util.concurrent.Callable;

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

    ResultSet<Car> doQuery() {
        return coll.retrieve(equal(Car.ID, Constants.ID_CAR));
    }
}
