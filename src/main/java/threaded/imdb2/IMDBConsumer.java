package threaded.imdb2;

import threaded.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

/**
 * Created by kimptonc on 08/06/2016.
 */
public class IMDBConsumer implements Callable<Boolean> {

    private final InMemoryDatabaseCQEngine imdb;

    public IMDBConsumer(InMemoryDatabaseCQEngine db) {
        imdb = db;
    }

    @Override
    public Boolean call() throws Exception {
        boolean nullEncountered = false;
        long testsPassed = 0;
        long testsFailed = 0;
        for (int i = 0; i < 5000; i++) {
            List<Map<String, Object>> result = null;
            try {
                if (i % 100 == 0) System.out.print(".");

//            if (coll.size() == 0) System.out.println("Collection is empty!");
                result = imdb.queryByField("cars", Constants.KEY_FIELD,Constants.ID);

                if (result == null) {
                    System.out.println("null result");
                    nullEncountered = true;
                } else if (result.isEmpty()) {
                    testsFailed++;
                    System.out.println("empty result:" + result + ", coll size:" + imdb.size("cars") + ", tests passed:" + testsPassed + ", failed:" + testsFailed);
                    nullEncountered = true;
//            } else if (result.uniqueResult().id == 0){
//                System.out.println("incomplete result:"+result);
//                nullEncountered=true;
                } else {
                    testsPassed++;
//                System.out.println("result is good:"+result);
                }


                sleep(2);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        System.out.println("threaded.map.Consumer done!");
        return nullEncountered;
    }
}
