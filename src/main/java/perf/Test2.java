package perf;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.entity.KeyedMapEntity;
import com.googlecode.cqengine.entity.MapEntity;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import threaded.car.Car;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Created by kimptoc on 20/05/2016.
 */
public class Test2 {

    private static boolean loggingEnabled = true;

    public static void main(String[] args) {
        log("CQEngine perf.Test2 - lots of queries - starting");
        int testSize;

        loggingEnabled = false;
        int warmupLoops = 1;
        for (int j = 0; j <= warmupLoops; j++) {
            if (j == warmupLoops) loggingEnabled = true;
            {
                logForced(j+")CQEngine Test - using threaded.car.Car objects");
                IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(Car.COLOUR));
                cars.addIndex(HashIndex.onAttribute(Car.MAKE));
                cars.addIndex(HashIndex.onAttribute(Car.MODEL));

                testSize = testCollection(cars, Car::new, or(equal(Car.MAKE, "Ford"), equal(Car.COLOUR, "Red")), -1);
            }
            {
                logForced(j+")CQEngine Test - using threaded.car.Car objects/nullable attribs");
                IndexedCollection<CarNullableAttribs> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.COLOUR));
                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.MAKE));
                cars.addIndex(HashIndex.onAttribute(CarNullableAttribs.MODEL));

                testSize = testCollection(cars, CarNullableAttribs::new, or(equal(CarNullableAttribs.MAKE, "Ford"), equal(CarNullableAttribs.COLOUR, "Red")), -1);
            }
            {
                logForced(j+")CQEngine Test - using Map objects");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(getAttrib("make")));
                cars.addIndex(HashIndex.onAttribute(getAttrib("model")));
                cars.addIndex(HashIndex.onAttribute(getAttrib("colour")));

                testCollection(cars, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);
            }
            {
                logForced(j+")CQEngine Test - using Map objects with nullable attribs");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("make")));
                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("model")));
                cars.addIndex(HashIndex.onAttribute(getNullableAttrib("colour")));

                testCollection(cars, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);
            }
            {
                logForced(j+")CQEngine Test - using Map objects and MapAttribs");
                IndexedCollection<Map> cars = new ConcurrentIndexedCollection<>();

                Attribute<Map, String> makeAttrib = mapAttribute("make", String.class);
                Attribute<Map, String> colourAttrib = mapAttribute("colour", String.class);

                cars.addIndex(HashIndex.onAttribute(makeAttrib));
                cars.addIndex(HashIndex.onAttribute(mapAttribute("model", String.class)));
                cars.addIndex(HashIndex.onAttribute(colourAttrib));

                testCollection(cars, (m) -> m, or(equal(makeAttrib, "Ford"), equal(colourAttrib, "Red")), testSize);
            }
            {
                logForced(j+")CQEngine Test - using MapEntity objects and MapEntityAttribs");
                IndexedCollection<MapEntity> cars = new ConcurrentIndexedCollection<>();

                Attribute<MapEntity, String> makeAttrib = mapEntityAttribute("make", String.class);
                Attribute<MapEntity, String> colourAttrib = mapEntityAttribute("colour", String.class);

                cars.addIndex(HashIndex.onAttribute(makeAttrib));
                cars.addIndex(HashIndex.onAttribute(mapEntityAttribute("model", String.class)));
                cars.addIndex(HashIndex.onAttribute(colourAttrib));

                testCollection(cars, QueryFactory::mapEntity,
                        or(equal(makeAttrib, "Ford"), equal(colourAttrib, "Red")), testSize);
            }
            {
                logForced(j+")CQEngine Test - using KeyedMapEntity objects and MapEntityAttribs");
                IndexedCollection<KeyedMapEntity> cars = new ConcurrentIndexedCollection<>();

                Attribute<KeyedMapEntity, String> makeAttrib = keyedMapEntityAttribute("make", String.class);
                Attribute<KeyedMapEntity, String> colourAttrib = keyedMapEntityAttribute("colour", String.class);

                cars.addIndex(HashIndex.onAttribute(makeAttrib));
                cars.addIndex(HashIndex.onAttribute(keyedMapEntityAttribute("model", String.class)));
                cars.addIndex(HashIndex.onAttribute(colourAttrib));

                testCollection(cars, (m) -> keyedMapEntity(m, "id"),
                        or(equal(makeAttrib, "Ford"), equal(colourAttrib, "Red")), testSize);
            }
        }
        log("CQEngine Test - done");

    }

    private static void logForced(String m) {
        log(m, true);
    }

    private static void log(String m) {
        log(m, false);
    }

    private static DateFormat timestampFormat = DateFormat.getTimeInstance();

    private static void log(String m, boolean forceLogging) {
        if (loggingEnabled || forceLogging) {

            String timestamp = timestampFormat.format(new Date());
            System.out.println(timestamp +">" + m);
        }
    }

    private static DecimalFormat formatter = new DecimalFormat("0.0000");

    private static <T> int testCollection(IndexedCollection<T> cars, Function<Map, T> builder, Query<T> query1, int size) {
        for (int i = 0; i < 20000; i++) {
            Map car = Car.randomCarMap(i);
            cars.add(builder.apply(car));
        }
        log("cars created = " + cars.size());

        long start = System.nanoTime();
        int numQueries = doSomeQueries(cars, query1);
        long elapsed = System.nanoTime() - start;
        double elapsedMillis = elapsed / 1000000.0;
        log("elapsed = " + formatter.format(elapsedMillis) + "ms for " + numQueries + " queries. Time per query:" + formatter.format(elapsedMillis / numQueries) + "ms\n");
        return cars.size();
    }

    private static <T> int doSomeQueries(IndexedCollection<T> cars, Query<T> query1) {
        int numFound = -1;
        int numQueries = (int) (cars.size() * 1);
        for (int i = 0; i < numQueries; i++) {
            ResultSet<T> resultSet = cars.retrieve(query1, queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE)));
//            int resultSetSize = resultSet.size();
            int resultSetSize = 0;
            for (T entity : resultSet) {
                resultSetSize++;
            }
            if (numFound == -1) {
                numFound = resultSetSize;
            } else {
                if (numFound != resultSetSize)
                    throw new RuntimeException("Expected " + numFound + " but got " + resultSetSize);
            }
        }
        return numQueries;
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

}

