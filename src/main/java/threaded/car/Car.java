package threaded.car;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by kimptoc on 25/05/2016.
 */
public class Car {

    static final AtomicLong VERSION_GENERATOR = new AtomicLong();
    final long version = VERSION_GENERATOR.incrementAndGet();

    static int lastId = 0;
    static String[] COLOURS = {"Red", "Blue", "Green", "Yellow","Black","White","Orange","Pink"};
    static String[] MAKES = {"Toyota", "Ford", "Honda", "Audi","Chrysler","Jaguar","Porsche","Merecedes","BMW"};
    static String[] MODELS = {"1", "2", "3", "4","5","6","7","8","9","10"};
    int id;
    String colour;
    Double engineSize;
    String make;
    String model;

    public Car(Map m)
    {
        if (m.get("id") != null){
            id = (int) m.get("id");
        } else {
            id = ++lastId;
        }
        colour = (String) m.get("colour");
        make = (String) m.get("make");
        model = (String) m.get("model");
        engineSize = (Double) m.get("engineSize");
    }

    // Below helps performance, but doesn't seem to be required.
//    @Override
//    public int hashCode() {
//        return id;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        threaded.car.Car other = (threaded.car.Car) o;
//        if (this.id != other.id) return false;
//        if (this.version != other.version) return false;
//        return true;
//    }

    public static Attribute ID = new SimpleAttribute<Car, Integer>("id") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.id; }
    };
    public static Attribute COLOUR = new SimpleAttribute<Car, String>("colour") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.colour; }
    };
    public static Attribute MAKE = new SimpleAttribute<Car, String>("make") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.make; }
    };
    public static Attribute MODEL = new SimpleAttribute<Car, String>("model") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
    };

    public static void addField(Map car, String key, String[] things) {
        car.put(key, things[(int) (Math.random() * things.length)]);
    }

    public static Map randomCarMap(int i) {
        Map car = new HashMap();
        addField(car, "colour", COLOURS);
        addField(car, "make", MAKES);
        addField(car, "model", MODELS);
        car.put("id", i);
        car.put("engineSize", Math.random() * 1500 + 1000);
        return car;
    }
}
