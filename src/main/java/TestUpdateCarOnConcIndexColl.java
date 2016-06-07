import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.unique.UniqueIndex;

import java.util.Map;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateCarOnConcIndexColl extends TestUpdateCarBase {

    public static void main(String[] args) {
        IndexedCollection<Car> ic = new ConcurrentIndexedCollection<>();
        runTheTests(ic);
    }

}
