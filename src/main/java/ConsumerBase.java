import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

/**
 * Created by kimptonc on 07/06/2016.
 */
public abstract class ConsumerBase implements Callable<Boolean> {
    protected final IndexedCollection coll;

    public ConsumerBase(IndexedCollection ic) {
        coll = ic;
    }

    abstract ResultSet doQuery();

        @Override
    public Boolean call() throws Exception {
        boolean nullEncountered = false;
        long testsPassed = 0;
        long testsFailed = 0;
        for (int i = 0; i < 5000; i++) {
//            if (i%100 == 0) System.out.print(".");

//            if (coll.size() == 0) System.out.println("Collection is empty!");
            ResultSet result = doQuery();

            if (result == null) {
                System.out.println("null result");
                nullEncountered = true;
            } else if (result.isEmpty()) {
                testsFailed++;
                System.out.println("empty result:" + result+", coll size:"+coll.size()+", tests passed:"+testsPassed+", failed:"+testsFailed);
                nullEncountered = true;
//            } else if (result.uniqueResult().id == 0){
//                System.out.println("incomplete result:"+result);
//                nullEncountered=true;
            } else {
                testsPassed++;
//                System.out.println("result is good:"+result);
            }

            try {
                sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Consumer done!");
        return nullEncountered;
    }
}
