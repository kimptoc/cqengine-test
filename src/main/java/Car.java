import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Map;

/**
 * Created by kimptoc on 25/05/2016.
 */
public class Car {
    String colour;
    Double engineSize;
    String make;
    String model;

    Car(Map m)
    {
        colour = (String) m.get("colour");
        make = (String) m.get("make");
        model = (String) m.get("model");
        engineSize = (Double) m.get("engineSize");
    }

    static Attribute COLOUR = new SimpleNullableAttribute<Car, String>("colour") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.colour; }
    };
    static Attribute MAKE = new SimpleNullableAttribute<Car, String>("make") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.make; }
    };
    static Attribute MODEL = new SimpleNullableAttribute<Car, String>("model") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
    };
}