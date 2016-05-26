import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import javafx.util.converter.BigDecimalStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.endsWith;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.or;

/**
 * Created by kimptoc on 20/05/2016.
 */
public class Test1 {

    static String[] COLOURS = {"Red", "Blue","Green","Yellow"};
    static String[] MAKES = {"Toyota", "Ford","Honda","Audi"};
    static String[] MODELS = {"1", "2","3","4"};

    public static void main(String[] args) {
        System.out.println("CQEngine Test - starting");
        int secondsForCreation = 1;

        // test 1 - use specific Car class
        // create indexed collection, populate index, query index

        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();

        cars.addIndex(HashIndex.onAttribute(Car.COLOUR));
        cars.addIndex(HashIndex.onAttribute(Car.MAKE));
        cars.addIndex(HashIndex.onAttribute(Car.MODEL));

        int testSize = usingCarObjects(secondsForCreation, cars, Car::new, or(equal(Car.MAKE, "Ford"), equal(Car.COLOUR, "Red")), -1);

        // test 2 - use Map as wrapper for car attribs, about 10 times slower than above, when timing is 10.

        IndexedCollection<Map> cars2 = new ConcurrentIndexedCollection<Map>();

        cars2.addIndex(HashIndex.onAttribute(getAttrib("make")));
        cars2.addIndex(HashIndex.onAttribute(getAttrib("model")));
        cars2.addIndex(HashIndex.onAttribute(getAttrib("colour")));

        usingCarObjects(secondsForCreation, cars2, (m) -> m, or(equal(getAttrib("make"), "Ford"), equal(getAttrib("colour"), "Red")), testSize);

        // test 3 - use MapEntity

        System.out.println("CQEngine Test - done");

    }

    private static DecimalFormat formatter = new DecimalFormat("0.0000");

    private static <T> int usingCarObjects(int secondsForCreation, IndexedCollection<T> cars, Function<Map,T> builder, Query<T> query1, int size) {
        long startTime = System.currentTimeMillis();
        while ((size == -1 && System.currentTimeMillis() < (startTime + secondsForCreation*1000)) || (size != -1 && cars.size() < size))
        {
            Map car = new HashMap();
            addField(car, "colour", COLOURS);
            addField(car, "make", MAKES);
            addField(car, "model", MODELS);
            car.put("engineSize", Math.random()*1500+1000);
            cars.add(builder.apply(car));
        }
        System.out.println("cars created = " + cars.size());

        // warmup
        doSomeQueries(cars, query1);

        long start = System.nanoTime();
        int numQueries = doSomeQueries(cars, query1);
        long elapsed = System.nanoTime() - start;
        double elapsedMillis = elapsed / 1000000.0;
        System.out.println("elapsed = " + formatter.format(elapsedMillis) +"ms for "+ numQueries +" queries. Time per query:"+ formatter.format(elapsedMillis/numQueries)+"ms");
        return cars.size();
    }

    private static <T> int doSomeQueries(IndexedCollection<T> cars, Query<T> query1) {
        int numFound = -1;
        int numQueries = cars.size() / 1000;
        for (int i = 0; i< numQueries; i++)
        {
            ResultSet<T> resultSet = cars.retrieve(query1);
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
}

