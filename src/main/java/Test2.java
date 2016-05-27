import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableMapAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Created by kimptoc on 20/05/2016.
 */
public class Test2 {

    static String[] COLOURS = {"Red", "Blue","Green","Yellow"};
    static String[] MAKES = {"Toyota", "Ford","Honda","Audi"};
    static String[] MODELS = {"1", "2","3","4"};
    private static boolean loggingEnabled = true;

    public static void main(String[] args) {
        log("CQEngine Test2 - lots of queries - starting");
        int testSize;

        loggingEnabled = false;
        int warmupLoops = 1;
        for (int j = 0; j <= warmupLoops; j++) {
            if (j == warmupLoops) loggingEnabled = true;
            {
                // test 1 - use specific Car class
                // create indexed collection, populate index, query index
                log("CQEngine Test - using Car objects");
                IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(Car.COLOUR));
                cars.addIndex(HashIndex.onAttribute(Car.MAKE));
                cars.addIndex(HashIndex.onAttribute(Car.MODEL));

                testSize = testCollection(cars, Car::new, or(equal(Car.MAKE, "Ford"), equal(Car.COLOUR, "Red")), -1);
            }
            {
                // test 1b - use specific Car class nullable attribs
                // create indexed collection, populate index, query index
                log("CQEngine Test - using Car objects/nullable attribs");
                IndexedCollection<CarNullableAttribs> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.COLOUR));
                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.MAKE));
                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.MODEL));

                testSize = testCollection(cars, CarNullableAttribs::new, or(equal(CarNullableAttribs.MAKE, "Ford"), equal(CarNullableAttribs.COLOUR, "Red")), -1);
            }
            {
                // test 2 - use Map as wrapper for car attribs, about 10 times slower than above, when timing is 10.

                log("CQEngine Test - using Map objects");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(getAttrib("make")));
                cars.addIndex(HashIndex.onAttribute(getAttrib("model")));
                cars.addIndex(HashIndex.onAttribute(getAttrib("colour")));

                testCollection(cars, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);
            }
            {
                // test 2b - use Map as wrapper for car nullable attribs, about 10 times slower than above, when timing is 10.

                log("CQEngine Test - using Map objects with nullable attribs");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("make")));
                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("model")));
                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("colour")));

                testCollection(cars, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);
            }
            {
                // test 3a - use Map as wrapper for car new MapAttribs, about 10 times slower than above, when timing is 10.

                log("CQEngine Test - using Map objects and MapAttribs");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(getMapAttrib("make")));
                cars.addIndex(HashIndex.onAttribute(getMapAttrib("model")));
                cars.addIndex(HashIndex.onAttribute(getMapAttrib("colour")));

                testCollection(cars, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);
            }
            {
                // test 3 - use MapEntity
            }
        }
        log("CQEngine Test - done");

    }

    private static void log(String m)
    {
        if (loggingEnabled) System.out.println(m);
    }

    private static DecimalFormat formatter = new DecimalFormat("0.0000");

    private static <T> int testCollection(IndexedCollection<T> cars, Function<Map,T> builder, Query<T> query1, int size) {
        for(int i=0; i<10000; i++)
        {
            Map car = new HashMap();
            addField(car, "colour", COLOURS);
            addField(car, "make", MAKES);
            addField(car, "model", MODELS);
            car.put("engineSize", Math.random()*1500+1000);
            cars.add(builder.apply(car));
        }
        log("cars created = " + cars.size());

        // warmup
//        for (int j=0; j<3; j++) {
//            doSomeQueries(cars, query1);
//        }

        long start = System.nanoTime();
        int numQueries = doSomeQueries(cars, query1);
        long elapsed = System.nanoTime() - start;
        double elapsedMillis = elapsed / 1000000.0;
        log("elapsed = " + formatter.format(elapsedMillis) +"ms for "+ numQueries +" queries. Time per query:"+ formatter.format(elapsedMillis/numQueries)+"ms\n");
        return cars.size();
    }

    private static <T> int doSomeQueries(IndexedCollection<T> cars, Query<T> query1) {
        int numFound = -1;
        int numQueries = cars.size()*1;
        for (int i = 0; i< numQueries; i++)
        {
            ResultSet<T> resultSet = cars.retrieve(query1, queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE)));
//            int resultSetSize = resultSet.size();
            int resultSetSize = 0;
            for (T entity : resultSet) {
                resultSetSize++;
            }
            if (numFound == -1)
            {
                numFound = resultSetSize;
            } else {
                if (numFound != resultSetSize) throw new RuntimeException("Expected "+numFound+" but got "+resultSetSize);
            }
        }
        return numQueries;
    }

    private static void addField(Map car, String colour, String value) {
        car.put(colour, value);
    }

    private static void addField(Map car, String key, String[] things) {
        car.put(key, things[(int) (Math.random() * things.length)]);
    }

    private static SimpleAttribute<Map, String> getAttrib(final String attributeName) {
        return new SimpleAttribute<Map, String>(attributeName) {
            @Override
            public String getValue(Map o, QueryOptions queryOptions) {
                return (String) o.get(attributeName);
            }
        };
    }

    private static SimpleNullableAttribute<Map, String> getNullableAttrib(final String attributeName) {
        return new SimpleNullableAttribute<Map, String>(attributeName) {
            @Override
            public String getValue(Map o, QueryOptions queryOptions) {
                return (String) o.get(attributeName);
            }
        };
    }

    private static SimpleNullableMapAttribute<String, String> getMapAttrib(final String attributeName) {
        return new SimpleNullableMapAttribute<>(attributeName, String.class);
    }
}

