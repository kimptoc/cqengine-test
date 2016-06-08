package threaded.map;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import threaded.map.TestUpdateBase;

import java.util.Map;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateOnConcIndexColl extends TestUpdateBase {

    public static void main(String[] args) {
        IndexedCollection<Map> ic = new ConcurrentIndexedCollection<>();

        runTheTests(ic);
    }

}
