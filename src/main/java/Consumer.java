import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.sun.corba.se.impl.util.RepositoryId.cache;
import static java.lang.Thread.sleep;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class Consumer extends ConsumerBase {

    private final SimpleNullableAttribute<Map, Comparable> attribute;

    Consumer(IndexedCollection<Map> ic)
    {
        super(ic);
        attribute = new SimpleNullableAttribute<Map, Comparable>(Constants.KEY_FIELD) {
            @Override
            public Comparable getValue(Map o, QueryOptions queryOptions) {
                Object value = o.get(Constants.KEY_FIELD);
                return (Comparable) value;
            }
        };

    }

    @Override
    ResultSet doQuery() {
        return coll.retrieve(equal(attribute, Constants.ID));
    }

}
