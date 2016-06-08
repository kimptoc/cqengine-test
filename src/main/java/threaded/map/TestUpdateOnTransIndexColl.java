package threaded.map;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import threaded.Constants;
import threaded.map.TestUpdateBase;

import java.util.Map;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateOnTransIndexColl extends TestUpdateBase {

    public static void main(String[] args) {
        IndexedCollection<Map> ic = new TransactionalIndexedCollection<>(Map.class);
        SimpleNullableAttribute<Map, Comparable> attribute = new SimpleNullableAttribute<Map, Comparable>(Constants.KEY_FIELD) {
            @Override
            public Comparable getValue(Map o, QueryOptions queryOptions) {
                Object value = o.get(Constants.KEY_FIELD);
                return (Comparable) value;
            }
        };
        ic.addIndex(UniqueIndex.onAttribute(attribute));
        runTheTests(ic);
    }

}
