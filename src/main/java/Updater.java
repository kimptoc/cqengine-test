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
public class Updater implements Runnable {

    private final IndexedCollection<Map> coll;
    private final SimpleNullableAttribute<Map, Comparable> attribute;

    Updater(IndexedCollection<Map> ic)
    {
        coll = ic;
        attribute = new SimpleNullableAttribute<Map, Comparable>(Constants.KEY_FIELD) {
            @Override
            public Comparable getValue(Map o, QueryOptions queryOptions) {
                Object value = o.get(Constants.KEY_FIELD);
                return (Comparable) value;
            }
        };

    }

    @Override
    public void run() {
        System.out.println("Updates starting...");
        for (int i=0; i< 10000; i++)
        {
            Map m = new HashMap<>();
            m.put(Constants.KEY_FIELD,Constants.ID);
            m.put("Size", 1+i);
            m.put("Distance", 2+i);
            m.put("Height", 3+i);
            m.put("Width", 4+i);
            m.put("Version", Constants.VERSION_GENERATOR.incrementAndGet());

            ResultSet<Map> existingItems = coll.retrieve(equal(attribute, Constants.ID));
            coll.update(
                    Collections.singleton(existingItems.uniqueResult()),
                    Collections.singleton(m));
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Updates are now done!");
    }
}
