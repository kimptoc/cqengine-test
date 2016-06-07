import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;

import java.util.Map;

/**
 * Created by kimptonc on 07/06/2016.
 */
public class TestUpdateCarOnTransIndexColl extends TestUpdateCarBase {

    public static void main(String[] args) {
        IndexedCollection<Car> ic = new TransactionalIndexedCollection<>(Car.class);

        runTheTests(ic);
    }

}
