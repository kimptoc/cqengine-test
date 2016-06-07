import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;

import java.util.Map;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateOnTransIndexColl extends TestUpdateBase {

    public static void main(String[] args) {
        IndexedCollection<Map> ic = new TransactionalIndexedCollection<>(Map.class);

        runTheTests(ic);
    }

}
